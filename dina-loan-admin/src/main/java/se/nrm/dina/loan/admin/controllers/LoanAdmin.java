package se.nrm.dina.loan.admin.controllers;

import java.io.Serializable;  
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;  
import javax.inject.Inject;
import javax.inject.Named; 
import lombok.extern.slf4j.Slf4j;  
import se.nrm.dina.loan.admin.config.InitialProperties;

/**
 *
 * @author idali
 */
@Named(value = "loanAdmin")
@SessionScoped
@Slf4j
public class LoanAdmin implements Serializable { 
  
    private String contactEmail;
    
    @Inject
    private InitialProperties properties;
 
    public LoanAdmin() {
 
    }

    @PostConstruct
    public void init() {
        log.info("init"); 
        contactEmail = properties.getContactEmail();
    }
   
    public String getContactEmail() {
        return contactEmail;
    } 
}
