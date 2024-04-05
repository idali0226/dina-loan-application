package se.nrm.dina.loan.admin.controllers;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idali
 */
@Named(value = "message")
@SessionScoped
@Slf4j
public class MessageBean implements Serializable  {
    
    private final String duplicatCollection = "Duplicat Collection";
    private final String duplicatUserTitle =  "Duplicate username";
    private final String userName = "Username: ";
    private final String existInDatabase = " is already exist in database.";
     
    private final String duplicateEmailErrorTitle = "Duplicate user";
    private final String duplicateEmailErrorMsg1 = "User with email: ";
    private final String duplicateEmailErrorMsg2 = " is exist in this group.";
    
    public MessageBean() {
        
    }
    
    public void addError(String errorTitle, String errorMsg) {
        FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, errorTitle, errorMsg));
    }

    public void addInfo(String msgTitle, String msg) {
        FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, msgTitle, msg));
    }

    public void addWarning(String warningTitle, String warningMsg) {
        FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_WARN, warningTitle, warningMsg));
    }
    
    public void addDuplicateUsernameError(String username) { 
        addError(duplicatUserTitle, buildDuplicateUserName(username));
    }
    
    public void addDuplicateEmailError(String email) { 
        addError(duplicateEmailErrorTitle, buildDuplicateEmail(email));
    }
    
    private String buildDuplicateEmail(String email) { 
        StringBuilder sb = new StringBuilder();
        sb.append(duplicateEmailErrorMsg1);
        sb.append(email);
        sb.append(duplicateEmailErrorMsg2);
        return sb.toString().trim(); 
    }
    
    private String buildDuplicateUserName(String username) { 
        StringBuilder sb = new StringBuilder();
        sb.append(userName);
        sb.append(username);
        sb.append(existInDatabase);
        return sb.toString().trim();
    }
        
        
    public void addDuplicatCollectionWarning(String collectionName, String collectionGroup) {
        StringBuilder sb = new StringBuilder();
        sb.append("Collection name [");
        sb.append(collectionName);
        sb.append("] is already exist in ");
        sb.append(collectionGroup);
        sb.append(".");

        addWarning(duplicatCollection, sb.toString());
    }
}
