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
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.RowEditEvent;
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
@Slf4j
public class UserAccountController implements Serializable {

    private final String passwordChangedMsg = "Password changed";
    private final String incorrectOldPassword = "Incorrect old password";

    private final String duplicateEmailErrorTitle = "Duplicate user";
    private final String duplicateEmailErrorMsg1 = "User with email: ";
    private final String duplicateEmailErrorMsg2 = " is exist in this group.";

    private final String defaultGroupname = "user";

    private String username;
    private String password;
    private String email;
    private boolean isOnVacation;
    private String groupname;

    private final String loginuserSessionKey = "loginuser";
    private final String findNonInventoryGroupNamedQuery = "TblGroups.findNonInventoryGroups"; 

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

    @Inject
    private Login login;

    @Inject
    private Navigater navigate;

    private HttpSession session;

    public UserAccountController() {
        excludeGroups = new ArrayList<>();
        excludeGroups.add("inventory");
        excludeGroups.add("superuser");
        excludeGroups.add("vegadare");
        excludeGroups.add("scientist");

        isChangePasswordSuccessed = false;
        servername = ((HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest()).getServerName();
    }

    @PostConstruct
    public void init() {
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        loggedinUser = (TblUsers) session.getAttribute(loginuserSessionKey);
 
        if (accounts == null || accounts.isEmpty()) {
            getLoanGroupAccount();
        }
    }

 

    public void updatePassword() {
        log.info("updatePassword : {} -- {}", oldPassword, newPassword + " -- " + password);

        isChangePasswordSuccessed = false;
        String encodedPasswordDigest;
        try {
            encodedPasswordDigest = hashAndEncodePassword(oldPassword);
            if (encodedPasswordDigest.equals(loggedinUser.getPassword())) {
                loggedinUser.setPassword(hashAndEncodePassword(newPassword));
                dao.mergeAccount(loggedinUser);
                isChangePasswordSuccessed = true;
                addInfo(passwordChangedMsg, passwordChangedMsg);

                login.logout();
                navigate.gotoHome();
            } else {
                addError(incorrectOldPassword, incorrectOldPassword);
                isChangePasswordSuccessed = false;
            }
        } catch (NoSuchAlgorithmException | NoSuchProviderException | UnsupportedEncodingException ex) {
            isChangePasswordSuccessed = false;
            log.warn(ex.getMessage());
        }
    }

    public void removeAccount(TblGroups group) {
        log.info("removeAccount: {}", group);
        dao.delete(group.getUsername());
    }

    public void onRowEdit(RowEditEvent event) {
        log.info("onRowEdit: {}", (TblGroups) event.getObject());

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

    public void addUserAccount() {
        log.info("addUserAccount");

        TblUsers user = new TblUsers();
        if (dao.validateUserName(username)) {
            if (dao.validateEmail(email)) {
                user.setUsername(username);
                user.setEmail(email);

                try {
                    user.setPassword(hashAndEncodePassword(password));
                } catch (NoSuchAlgorithmException | NoSuchProviderException
                        | UnsupportedEncodingException ex) {
                    log.error(ex.getMessage());
                }

                TblGroups group = new TblGroups();
                group.setGroupname(groupname);
                group.setUsername(user);

                List<TblGroups> groups = new ArrayList<>();
                groups.add(group);

                user.setTblGroupsList(groups);
 
                dao.createAccount(user);
                getLoanGroupAccount(); 

                if (servername.contains("localhost")) {
                    mail.sendNewAdminAccountMail(username, password, email);
                } else {
                    nrmMail.sendNewAdminAccountMail(username, password, email);
                }
//                nrmMail.sendNewAdminAccountMail(username, password, email);
                username = null;
                password = null;
                email = null;
                groupname = defaultGroupname;
            } else {
                addError(duplicateEmailErrorTitle, 
                        duplicateEmailErrorMsg1 + email + duplicateEmailErrorMsg2);
            }
        } else {
            addError("Duplicate username", 
                    "Username: " + username + " is already exist in database.");
        }
    }

//    public void updateUser(String user) {
//        accounts = new ArrayList<>();
//        getLoanGroupAccount();
//    }

    public void checkonvacation() {
        log.info("checkonvacation : {} -- {}", loggedinUser, loggedinUser.getOnvacation());
 
        dao.mergeAccount(loggedinUser);
        getLoanGroupAccount();
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
    
    private void getLoanGroupAccount() {
        accounts = dao.findGroupByNamedQuery(findNonInventoryGroupNamedQuery, excludeGroups);
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
