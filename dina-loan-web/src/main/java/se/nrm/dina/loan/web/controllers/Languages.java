package se.nrm.dina.loan.web.controllers;
 

import java.io.Serializable; 
import javax.enterprise.context.SessionScoped; 
import javax.inject.Named;  

/**
 *
 * @author idali
 */
@Named(value = "languages")
@SessionScoped
public class Languages implements Serializable {
     
    private final String sv = "sv";
    private String locale = "en";  
    
    private final String changeToEnglisth = "Change language to: English";
    private final String changeToSwedish = "     Byt språk till: Svenska";
    
    private boolean openGbif = false;

    public Languages() {
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
