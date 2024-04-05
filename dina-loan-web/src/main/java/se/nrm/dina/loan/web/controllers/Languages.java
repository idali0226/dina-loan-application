package se.nrm.dina.loan.web.controllers;
 

import java.io.Serializable; 
import javax.enterprise.context.SessionScoped; 
import javax.inject.Inject;
import javax.inject.Named;  
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.loan.web.beans.BreadCrumbBean;

/**
 *
 * @author idali
 */
@Named(value = "languages")
@SessionScoped
@Slf4j
public class Languages implements Serializable {
     
    private final String sv = "sv";
    private String locale = "en";  
    
    private final String changeToEnglisth = "Change language to: English";
    private final String changeToSwedish = "     Byt språk till: Svenska";
    
    private boolean openGbif = false;
    
    @Inject
    private BreadCrumbBean crumb;
    @Inject
    private BorrowerController borrower;
    @Inject
    private LoanForm form;
    @Inject
    private InformationLoanForm otherForm;
    @Inject
    private ScientificLoanForm scientificLoanForm;

    public Languages() {
    } 
    
    public void changelanguage(String locale) {
        log.info("changelanguage : {}", locale);
        this.locale = locale;
        
        boolean isSwedish = isIsSwedish();
        crumb.resetLocale(isSwedish);
        borrower.resetLocale(isSwedish);
        form.resetLocale(isSwedish);
        otherForm.resetLocale(isSwedish);
        scientificLoanForm.resetLocale(isSwedish);
    }
 
    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        openGbif = false;
        this.locale = locale;
    }
       
    public String getLanguage() {
        openGbif = false;
        
        return locale.equals(sv) ? changeToEnglisth : changeToSwedish;
    }
      
    public boolean isIsSwedish() {
        return locale.equals(sv);
    } 

    public boolean isOpenGbif() {
        return openGbif;
    }

    public void setOpenGbif(boolean openGbif) {
        this.openGbif = openGbif;
    } 
    
}
