package se.nrm.dina.loan.admin.config;

import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

/**
 *
 * @author idali
 */
@ApplicationScoped
@Slf4j
public class InitialProperties implements Serializable {
    
    private final static String CONFIG_INITIALLISING_ERROR = "Property not initialized";
    
    private String scientificPolicy;
    private String educationalPolicy; 
    
    private String contactMail;
    
    private String loanFilePath;  
    
    private String adminPdfFilePath;  
    
 
    public InitialProperties() {
    }
    
    @Inject
    public InitialProperties(@ConfigurationValue("swarm.loan.policies.scientific") String scientificPolicy,
            @ConfigurationValue("swarm.loan.policies.educational") String educationalPolicy,   
            @ConfigurationValue("swarm.loan.file.loan") String loanFilePath, 
            @ConfigurationValue("swarm.loan.adminPdfPath") String adminPdfFilePath, 
            @ConfigurationValue("swarm.contact.email") String contactMail) {
        this.scientificPolicy = scientificPolicy;
        this.educationalPolicy = educationalPolicy;  
        this.loanFilePath = loanFilePath; 
        this.adminPdfFilePath = adminPdfFilePath;
        
        this.contactMail = contactMail;
        log.info("InitialProperties : {}", contactMail);
    }
 
    public String getScientificPolicyPath() {
        if (scientificPolicy == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return scientificPolicy;
    }
    
     
    public String getLoanFilePath() {
        if (loanFilePath == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return loanFilePath;
    }
 
    public String getEducationalPolicyPath() {
        if (educationalPolicy == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return educationalPolicy;
    } 
    
    public String getAdminPdfPath() {
        if (adminPdfFilePath == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return adminPdfFilePath;
    } 
    
    public String getContactEmail() {
        if (contactMail == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return contactMail;
    }  
}
