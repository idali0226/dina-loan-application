package se.nrm.dina.loan.admin.controllers;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random; 
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.RowEditEvent;
import se.nrm.dina.email.NrmMail;
import se.nrm.dina.loan.admin.config.InitialProperties;
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
    private final String loginuserSessionKey = "loginuser";

    private final String noAccountFind = "Username and email not found!";
    private final String passwordRecoverSucess = "The password has been changed and the new password has been sent to your email";

    private final String defaultGroupname = "user";

    private final String sha256 = "SHA-256";
    private final String utf8 = "UTF-8";

    private String username;
    private String password;
    private String email;
    private boolean isOnVacation;
    private String groupname;

    private List<TblGroups> filteredAccounts;
    private List<TblGroups> accounts;

    private TblUsers loggedinUser;

    private String oldPassword = null;
    private String newPassword = null;
    private boolean isChangePasswordSuccessed;

    @EJB
    private AccountDao dao;

    @Inject
    private InitialProperties properties;

    @Inject
    private NrmMail nrmMail;

    @Inject
    private Login login;

    @Inject
    private MessageBean msg;

    @Inject
    private Navigater navigate;

    private HttpSession session;

    private String host;

    public UserAccountController() {

        isChangePasswordSuccessed = false;
    }

    @PostConstruct
    public void init() {
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        loggedinUser = (TblUsers) session.getAttribute(loginuserSessionKey);
        host = properties.getHost();

        if (accounts == null || accounts.isEmpty()) {
            getLoanGroupAccount();
        }
    }

    // user page
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

    public void removeAccount(TblGroups group) {
        log.info("removeAccount: {}", group);
        dao.delete(group.getUsername());
    }

    public void addUserAccount() {
        log.info("addUserAccount");

        TblUsers user = new TblUsers();

        int validateResult = dao.validateUser(username, email);
        if (validateResult == 0) {
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

            nrmMail.sendNewAdminAccountMail(username, password, email, host);

            username = null;
            password = null;
            email = null;
            groupname = defaultGroupname;
        } else {
            if (validateResult == 1) {
                msg.addDuplicateUsernameError(username);
            } else if (validateResult == 2) {
                msg.addDuplicateEmailError(email);
            }
        }
    }

    public List<TblGroups> getFilteredAccounts() {
        return filteredAccounts;
    }

    public void setFilteredAccounts(List<TblGroups> filteredAccounts) {
        this.filteredAccounts = filteredAccounts;
    }

    // End of user page
    // change password page
    public void changePassword() {
        log.info("changePassword ");
        isChangePasswordSuccessed = false;
        navigate.gotoChangePassword();
    }

    public void updatePassword() {
        log.info("updatePassword : {} -- {}",
                oldPassword, newPassword + " -- " + password);

        isChangePasswordSuccessed = false;
        String encodedPasswordDigest;
        try {
            encodedPasswordDigest = hashAndEncodePassword(oldPassword); 

            log.info("loggedinUser password : {}", loggedinUser);
            if (loggedinUser == null) {
                session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                loggedinUser = (TblUsers) session.getAttribute(loginuserSessionKey);
                log.info("loggedinUser password : {}", loggedinUser);
            }
            if (encodedPasswordDigest.equals(loggedinUser.getPassword())) {
                loggedinUser.setPassword(hashAndEncodePassword(newPassword));
                dao.mergeAccount(loggedinUser);
                isChangePasswordSuccessed = true;
                msg.addInfo(passwordChangedMsg, passwordChangedMsg);
                login.logout();
            } else {
                msg.addError(incorrectOldPassword, incorrectOldPassword);
                isChangePasswordSuccessed = false;
            }
        } catch (NoSuchAlgorithmException | NoSuchProviderException
                | UnsupportedEncodingException ex) {
            isChangePasswordSuccessed = false;
            log.error(ex.getMessage());
        }
    }

    // end change password page
    // password recover
    public void recoverPasswrod() {
        log.info("recoverPasswrod : {} -- {}", username, email);
        TblUsers user = dao.validateUserByUsernameAndEmail(username, email);
        if (user == null) {
            log.info("user not find");
            msg.addInfo(noAccountFind, noAccountFind);
        } else {
            try {
                log.info("user find : {}", user);
                
                String randomPassword = generateRandomPassword();
                log.info("randomPassword : {}", randomPassword);
                
                user.setPassword(hashAndEncodePassword(randomPassword));
                dao.mergeAccount(user); 
                
                nrmMail.sendPasswordRecoverEmail(email, randomPassword, properties.getHost());
                username = null;
                email = null;
                msg.addInfo(null, passwordRecoverSucess); 
            } catch (NoSuchAlgorithmException | NoSuchProviderException | UnsupportedEncodingException ex) { 
                log.error(ex.getMessage());
            }
        }
//        navigate.gotoLoginPage();
    }

    private String generateRandomPassword() { 
        return new Random()
                .ints(10, 33, 122)
                .collect(StringBuilder::new,
                        StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    // end password recover
    // home page 
    public void checkonvacation() {
        log.info("checkonvacation : {} -- {}", loggedinUser, loggedinUser.getOnvacation());

        dao.mergeAccount(loggedinUser);
        getLoanGroupAccount();
    }

    private String hashAndEncodePassword(String password)
            throws NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException {

        try {
            MessageDigest md = MessageDigest.getInstance(sha256);
            md.update(password.getBytes(utf8));
            byte[] passwordDigest = md.digest();

            return (new BASE64Encoder()).encode(passwordDigest);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Exception encoding password", e);
        }
    }

    private void getLoanGroupAccount() {
        accounts = dao.findLoanGroupByNamedQuery();
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
}
