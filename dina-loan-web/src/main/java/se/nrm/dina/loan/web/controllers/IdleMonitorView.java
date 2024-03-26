package se.nrm.dina.loan.web.controllers;
 
import java.io.IOException;   
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage; 
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;  
import se.nrm.dina.loan.web.filehander.LoanFileHandler;
import se.nrm.dina.loan.web.util.CommonNames;
import se.nrm.dina.loan.web.util.NameMapping;


/**
 *
 * @author idali
 */
@Named
@RequestScoped
@Slf4j
public class IdleMonitorView {
       
    @Inject
    private LoanForm form;
    
    @Inject 
    private LoanFileHandler fileHandler;
    
    @Inject
    private Languages languages;

    public void onIdle() {
        log.info("onIdle");

        boolean isSwedish = languages.isIsSwedish();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        NameMapping.getMsgByKey(CommonNames.Idle, isSwedish),
                        NameMapping.getMsgByKey(CommonNames.IdleMsg, isSwedish)));

    }

    public void onActive() { 
        log.info("onActive");
         
        if(form.isUUIDExist()) {
            fileHandler.removeTempDirectory(form.getUUID().toString());
        }
        
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            
            HttpServletRequest request = (HttpServletRequest) 
                    FacesContext.getCurrentInstance().getExternalContext().getRequest();
            HttpSession session = request.getSession(false);
            session.invalidate();
            log.info("Redirect path : {}", 
                    FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
            ctx.getExternalContext().redirect("/");
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
            
    } 
}
