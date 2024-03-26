package se.nrm.dina.loan.admin.controllers;

//import java.io.IOException;
import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext; 
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idali
 */
@Named
@RequestScoped
@Slf4j
public class IdleMonitorView {
      
    private final String idle = "No activity";
    private final String idleMsg = "Page is inactive for two hours. Session is expired. Redirect to start page.";
    
    public void onIdle() {
        log.info("onIdle");
 
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,idle, idleMsg)); 
    }

    public void onActive() { 
        log.info("onActive");
         
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
