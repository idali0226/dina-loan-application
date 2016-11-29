package se.nrm.dina.loan.web.controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable; 
import javax.enterprise.context.SessionScoped; 
import javax.inject.Named; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author idali
 */
@Named(value = "languages")
@SessionScoped
public class Languages implements Serializable {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private String locale = "en";  
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
        
        return locale.equals("sv") ? "Change language to: English" : "     Byt språk till: Svenska";
    }
      
    public boolean isIsSwedish() {
        return locale.equals("sv");
    } 

    public boolean isOpenGbif() {
        return openGbif;
    }

    public void setOpenGbif(boolean openGbif) {
        this.openGbif = openGbif;
    } 
    
}
