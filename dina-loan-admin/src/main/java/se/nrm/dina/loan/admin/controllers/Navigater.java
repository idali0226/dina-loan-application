package se.nrm.dina.loan.admin.controllers;

import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idali
 */
@Named(value = "navigate")
@SessionScoped
@Slf4j
public class Navigater implements Serializable {

    private final String homePath = "/faces/secure/home.xhtml";
    private final String changePasswordPath = "/faces/secure/changePassword.xhtml";
    private final String loanPolicyPath = "/faces/secure/policies.xhtml";
    
    private final String userAccountPath = "/faces/secure/userAccount.xhtml";
    private final String collectionsPath = "/faces/secure/collections.xhtml";
    private final String loansPath = "/faces/secure/loans.xhtml";
    private final String notificationsPath = "/faces/secure/portalPopup.xhtml";
    
    private final String logoutPath = "/faces/login.xhtml";
    private final String passwordRecoverPath = "/faces/passwordrecover.xhtml";

    private ExternalContext externalContext;

    public Navigater() {

    }

    public void gotoHome() {
        log.info("gotoHome");  
        
        redirectPage(homePath); 
    }

    public void gotoChangePassword() {
        log.info("gotoChangePassword"); 
        redirectPage(changePasswordPath);
    }
    
    public void gotoManageUser() {
        log.info("gotoManageUser"); 
        redirectPage(userAccountPath);
    }
    
    public void gotoCollections() {
        log.info("gotoCollections"); 
        redirectPage(collectionsPath);
    }
    
    public void gotoLoans() {
        log.info("gotoLoans"); 
        redirectPage(loansPath);
    }
    
    public void gotoLoanPolicies() {
        log.info("gotoLoanPolicies"); 
        redirectPage(loanPolicyPath);
    }

    public void gotoNotification() {
        log.info("gotoNotification"); 
        redirectPage(notificationsPath);
    }
    
    public void gotoLoginPage() {
        log.info("gotoLoginPage"); 
        redirectPage(logoutPath);
    }
    
    public void gotoPasswordRecoverPage() {
        log.info("gotoPasswordRecoverPage : {}", passwordRecoverPath); 
        redirectPage(passwordRecoverPath);
    }
    
    private void redirectPage(String path) {
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + path);
        } catch (IOException ex) {
        }
    }
}
