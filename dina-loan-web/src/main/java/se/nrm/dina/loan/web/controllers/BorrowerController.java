package se.nrm.dina.loan.web.controllers;

import java.io.Serializable;   
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;  
import se.nrm.dina.loan.web.util.CountryNames;
import se.nrm.dina.mongodb.loan.vo.User;

/**
 *
 * @author idali
 */
@Named(value = "borrower")
@SessionScoped
@Slf4j
public class BorrowerController implements Serializable {

    
    private final String swedenEn = "Sweden";
    private final String swedenSv = "Sverige"; 
    private final String student = "student";
    private final String emptySpace = " ";

    private User primaryUser;
    private User user;
    private boolean hasPrimaryUser = false;
    private boolean isAgree = false;
    
    @Inject
    private Languages language;
     
    public BorrowerController() {

        primaryUser = new User();
        user = new User();
        primaryUser.getAddress().setCountry(swedenEn);
        user.getAddress().setCountry(swedenEn);
        hasPrimaryUser = false;
    }
    
    public void resetData() {
        
        hasPrimaryUser = false;
        primaryUser = new User();
        user = new User();
        isAgree = false;

        if (language.isIsSwedish()) {
            primaryUser.getAddress().setCountry(swedenSv);
            user.getAddress().setCountry(swedenSv);
        } else {
            primaryUser.getAddress().setCountry(swedenEn);
            user.getAddress().setCountry(swedenEn);
        } 
    }
    
    public List<String> getCountryList() {
        return CountryNames.getCountryList(language.isIsSwedish());
    }
    
    public void borrowerTitleChanged() {
        log.info("borrowerTitleChanged : {}", hasPrimaryUser);
        
        if (user.getTitle() != null && user.getTitle().contains(student)) {
                addPrimaryContact();
        }

//        hasPrimaryContact = false;
//        if (RequestType.Physical.isPhysical(requestType)
//                && LoanPurpose.ScientificPurpose.isScientificPurpose(purpose)) {
//            if (user.getTitle() != null && user.getTitle().contains(student)) {
//                addPrimaryContact();
//            }
//        }
    }
    
    public boolean isIsStudent() {
        log.info("isIsStudent user : {}", user);
        if (user != null && user.getTitle() != null) {
            return user.getTitle().contains(student);
        }
        return false;
    }
    
    public void addPrimaryContact() {
        log.info("addPrimaryContact : {}", hasPrimaryUser);
        hasPrimaryUser = true;
    }

    public void removePrimaryContact() {
        log.info("removePrimaryContact");
        hasPrimaryUser = false;
    }

    public void onIsAgreeStatusChange() {
        log.info("onIsAgreeStatusChange : {}", isAgree);
    }
    
    public void saveText() {
        log.info("saveText");
    }

    public User getPrimaryUser() {
        return primaryUser;
    }

    public void setPrimaryUser(User primaryUser) {
        this.primaryUser = primaryUser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isHasPrimaryUser() {
        return hasPrimaryUser;
    }

    public void setHasPrimaryUser(boolean hasPrimaryUser) {
        this.hasPrimaryUser = hasPrimaryUser;
    }
    
    public String getUserFullName() {
        return getFullName(user.getFirstname(), user.getLastname());
    }
    
    public String getPrimaryUserFullName() {
        return getFullName(primaryUser.getFirstname(), primaryUser.getLastname());
    }
    
    
    public boolean isIsAgree() {
        return isAgree;
    }

    public void setIsAgree(boolean isAgree) {
        this.isAgree = isAgree;
    }
    
    private String getFullName(String firstName, String lastName) {
        StringBuilder sb = new StringBuilder();
        sb.append(firstName);
        sb.append(emptySpace);
        sb.append(lastName);
        return sb.toString();
    }
 
}
