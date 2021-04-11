package se.nrm.dina.loan.admin.controllers;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nrm.dina.email.Mail;
import se.nrm.dina.email.NrmMail;
import se.nrm.dina.manager.dao.AccountDao;
import se.nrm.dina.manager.entities.TblGroups;
import se.nrm.dina.manager.entities.TblUsers;
import sun.misc.BASE64Encoder;

/**
 *
 * @author idali
 */
@Named(value = "user")
@SessionScoped
public class UserAccountController implements Serializable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String username;
    private String password;
    private String email;
    private boolean isOnVacation;
    private String groupname = "user";

    private List<TblGroups> filteredAccounts;
    private List<TblGroups> accounts;

    private TblUsers loggedinUser;
    private final List<String> excludeGroups;

    private String oldPassword = null;
    private String newPassword = null;
    private boolean isChangePasswordSuccessed;

    private final String servername;

    @EJB
    private AccountDao dao;

    @Inject
    private Mail mail;

    @Inject
    private NrmMail nrmMail;

    private HttpSession session;

    public UserAccountController() {
        excludeGroups = new ArrayList<>();
        excludeGroups.add("inventory");
        excludeGroups.add("superuser");
        excludeGroups.add("vegadare");
        excludeGroups.add("scientist");

        isChangePasswordSuccessed = false;
        servername = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
    }

    @PostConstruct
    public void init() {

        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        loggedinUser = (TblUsers) session.getAttribute("loginuser");

        if (accounts == null || accounts.isEmpty()) {
            accounts = dao.findGroupByNamedQuery("TblGroups.findNonInventoryGroups", excludeGroups);
        }
    }

    public void updatePassword() {
        logger.info("updatePassword");

//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
//        
//        TblUsers user = dao.findByUserName(request.getUserPrincipal().toString());
        isChangePasswordSuccessed = false;
        String encodedPasswordDigest;
        try {
            encodedPasswordDigest = hashAndEncodePassword(oldPassword);
            if (encodedPasswordDigest.equals(loggedinUser.getPassword())) {
                loggedinUser.setPassword(hashAndEncodePassword(newPassword));
                dao.mergeAccount(loggedinUser);
                isChangePasswordSuccessed = true;
                addInfo("Password changed", "Password changed");
            } else {
                addError("Incorrect old password", "Incorrect old password");
                isChangePasswordSuccessed = false;
            }
        } catch (NoSuchAlgorithmException | NoSuchProviderException | UnsupportedEncodingException ex) {
            isChangePasswordSuccessed = false;
            logger.warn(ex.getMessage());
        }

    }

    public void onRowEdit(RowEditEvent event) {

        logger.info("onRowEdit: {}", (TblGroups) event.getObject());

        TblGroups selectedGroup = (TblGroups) event.getObject();
        TblUsers user = selectedGroup.getUsername();

        List<TblGroups> groups = new ArrayList<>();
        groups.add(selectedGroup);

        user.setTblGroupsList(groups);
        selectedGroup.setUsername(user);
        dao.mergeAccount(user);

        if (user.getUsername().equals(loggedinUser.getUsername())) {
            loggedinUser.setOnvacation(user.getOnvacation());
        }
    }

    public void removeAccount(TblGroups group) {
        logger.info("removeAccount: {}", group);
        dao.delete(group.getUsername());
    }

    public void addUserAccount() {
        logger.info("addUserAccount");

        TblUsers user = new TblUsers();
        if (dao.validateUserName(username)) {
            if (dao.validateEmail(email)) {
                user.setUsername(username);
                user.setEmail(email);

                String encodedPasswordDigest;
                try {
                    encodedPasswordDigest = hashAndEncodePassword(password);
                    user.setPassword(encodedPasswordDigest);
                } catch (NoSuchAlgorithmException | NoSuchProviderException | UnsupportedEncodingException ex) {
                    logger.warn(ex.getMessage());
                }

                TblGroups group = new TblGroups();
                group.setGroupname(groupname);
                group.setUsername(user);

                List<TblGroups> groups = new ArrayList<>();
                groups.add(group);

                user.setTblGroupsList(groups);

                dao.createAccount(user);
                accounts = dao.findGroupByNamedQuery("TblGroups.findNonInventoryGroups", excludeGroups);

                if (servername.contains("localhost")) {
                    mail.sendNewAdminAccountMail(username, password, email);
                } else {
                    nrmMail.sendNewAdminAccountMail(username, password, email);
                }
//                nrmMail.sendNewAdminAccountMail(username, password, email);
                username = null;
                password = null;
                email = null;
                groupname = "user";
            } else {
                addError("Duplicate user", "User with email: " + email + " is exist in this group.");
            }

//            if(dao.validateEmail(email)) {
//                user.setUsername(username);
//                user.setEmail(email);
//
//                String encodedPasswordDigest; 
//                try {
//                    encodedPasswordDigest = hashAndEncodePassword(password);
//                    user.setPassword(encodedPasswordDigest);
//                } catch (NoSuchAlgorithmException | NoSuchProviderException | UnsupportedEncodingException ex) {
//                    logger.warn(ex.getMessage());
//                }
//
//                TblGroups group = new TblGroups();
//                group.setGroupname(groupname);
//                group.setUsername(user);
//
//                List<TblGroups> groups = new ArrayList<>();
//                groups.add(group);
//
//                user.setTblGroupsList(groups);
//
//                dao.createAccount(user);
//                accounts = dao.findGroupByNamedQuery("TblGroups.findNonInventoryGroups", excludeGroups);
//
//                if (servername.contains("localhost")) {
//                    mail.sendNewAdminAccountMail(username, password, email);
//                } else {
//                    nrmMail.sendNewAdminAccountMail(username, password, email);
//                }
//
//                username = null;
//                password = null;
//                email = null;
//                groupname = "user";
//            } else {
//                addError("Duplicate email", "Email: " + email + " is already exist in database.");
//            }
        } else {
            addError("Duplicate username", "Username: " + username + " is already exist in database.");
        }
    }

    public void updateUser(String user) {
        accounts = new ArrayList<>();

        accounts = dao.findGroupByNamedQuery("TblGroups.findNonInventoryGroups", excludeGroups);
    }

    public void checkonvacation() {
        logger.info("checkonvacation : {} -- {}", loggedinUser, loggedinUser.getOnvacation());

//        TblUsers user = dao.findByUserName(username);
//        user.setOnvacation(isOnVacation);
        dao.mergeAccount(loggedinUser);
        accounts = dao.findGroupByNamedQuery("TblGroups.findNonInventoryGroups", excludeGroups);
    }

    private String hashAndEncodePassword(String password) throws NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8"));
            byte[] passwordDigest = md.digest();

            return (new BASE64Encoder()).encode(passwordDigest);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Exception encoding password", e);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public List<TblGroups> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<TblGroups> accounts) {
        this.accounts = accounts;
    }

    public List<TblGroups> getFilteredAccounts() {
        return filteredAccounts;
    }

    public void setFilteredAccounts(List<TblGroups> filteredAccounts) {
        this.filteredAccounts = filteredAccounts;
    }

    public boolean isIsOnVacation() {
        return isOnVacation;
    }

    public void setIsOnVacation(boolean isOnVacation) {
        this.isOnVacation = isOnVacation;
    }

    public TblUsers getLoggedinUser() {
        return loggedinUser;
    }

    public void setLoggedinUser(TblUsers loggedinUser) {
        this.loggedinUser = loggedinUser;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public boolean isIsChangePasswordSuccessed() {
        return isChangePasswordSuccessed;
    }

    public void setIsChangePasswordSuccessed(boolean isChangePasswordSuccessed) {
        this.isChangePasswordSuccessed = isChangePasswordSuccessed;
    }

    private void addInfo(String infoTitle, String infoMsg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, infoTitle, infoMsg));
    }

    private void addError(String errorTitle, String errorMsg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorTitle, errorMsg));
    }
}
