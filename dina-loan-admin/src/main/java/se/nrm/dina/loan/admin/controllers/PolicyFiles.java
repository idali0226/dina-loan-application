package se.nrm.dina.loan.admin.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException; 
import java.io.Serializable; 
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import se.nrm.dina.loan.admin.config.InitialProperties; 
import se.nrm.dina.loan.admin.policypdf.FileHandler;

/**
 *
 * @author idali
 */
@Named("policies")
@SessionScoped
@Slf4j
public class PolicyFiles implements Serializable  {
    
    private final String contentType = "application/pdf"; 
    
    private boolean isNewPolicy;
    
    @Inject
    private  InitialProperties properties;
    @Inject
    private FileHandler fileHandler;
    
    public StreamedContent getScientificPolicyPdfFile() { 
        log.info("getScientificPolicyPdfFile");
        
        try { 
            File pdf = new File(properties.getScientificPolicyPath());
              
            return new DefaultStreamedContent(new FileInputStream(pdf), contentType, pdf.getName());
        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage());
        }
        return null;
    }
    
    public StreamedContent getEducationPolicyPdfFile() {
        log.info("getEducationPolicyPdfFile");
         
        try { 
            File pdf = new File(properties.getEducationalPolicyPath());
             
            return new DefaultStreamedContent(new FileInputStream(pdf), contentType, pdf.getName());
        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage());
        }
        return null;
    } 
    
    public void uploadScientificPolicyFile(FileUploadEvent event) {
        log.info("uploadScientificPolicyFile : {} -- {}", 
                event.getFile(), event.getFile().getFileName());
        
        fileHandler.saveFile(event.getFile(), properties.getScientificPolicyPath()); 
        isNewPolicy = true;
    }
 
    public void uploadEdFile(FileUploadEvent event) { 
        log.info("uploadEdFile : {} -- {}", 
                event.getFile(), event.getFile().getFileName());
        
        fileHandler.saveFile(event.getFile(), properties.getEducationalPolicyPath()); 
        isNewPolicy = true;
    }
    
            
    public void updatePolicy() {
        log.info("updatePolicy"); 
        isNewPolicy = false;
    }
     

    public boolean isIsNewPolicy() {
        return isNewPolicy;
    }

    public void setIsNewPolicy(boolean isNewPolicy) {
        this.isNewPolicy = isNewPolicy;
    }
    
    
}
