package se.nrm.dina.loan.web.beans;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named; 
import se.nrm.dina.loan.web.config.InitialProperties; 

/**
 *
 * @author idali
 */
@Named("contactBean")
@SessionScoped
public class ContactBean implements Serializable {
      
    @Inject
    private InitialProperties properties;
    
    public ContactBean() {
        
    }
    
    public String getSupportEmail() {
        return properties.getSupportMail();
    }
    
    public String getSupportPhone() {
        return properties.getSupportPhone();
    }
}
