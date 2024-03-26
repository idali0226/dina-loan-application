package se.nrm.dina.loan.admin.controllers;

import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.manager.dao.AccountDao;
import se.nrm.dina.manager.entities.TblUsers;

/**
 *
 * @author idali
 */
@Named("login")
@SessionScoped
@Slf4j
public class Login implements Serializable {

    private final String homePath = "/secure/home?faces-redirect=true";
//    private final String logoutPath = "/secure/start?faces-redirect=true";
//    private final String logoutPath = "/login?faces-redirect=true";
    private final String emptyString = "";
    private final String loginUserSessionKey = "loginuser";

    private final String authenticationFailed = "Authentication failed!";
    private final String scientist = "scientist";
    private final String inventory = "inventory";

    private final String logoutFailed = "Failed to logout user!";
    private final String reLoginMsg = "Attempt to re-login while the user identity already exists";
    private final String incorrectPassword = "Incorrect Username or Password!";
    private final String emtpyString = "";
    
    private final String contextPath = "/admin";

    private final HttpSession session;

    private String username;
    private String password;

    @EJB
    private AccountDao dao;

    public Login() {
        session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
    }

    public String login() {
        log.info("login");

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.login(username, password);
            log.info("login user role : {}", request.isUserInRole(inventory));
            if (request.isUserInRole(inventory) || request.isUserInRole(scientist)) {
                log.info("here...");
                context.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                authenticationFailed, null));
                try {
                    request.logout();
                } catch (ServletException e) {
                    log.error(logoutFailed, e);
                }
                return emptyString;
            }

            TblUsers user = dao.findByUserName(username);
            session.setAttribute(loginUserSessionKey, user);
        } catch (ServletException e) {
            log.warn(e.getMessage());
            if (e.getMessage().equals(reLoginMsg)) {
                return emtpyString;
            }
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, incorrectPassword, null));
            return emtpyString;
        }
        return homePath;
    }

    public void logout() {
        log.info("logout");
         
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext(); 
        
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

        
        try {
            request.logout(); 
            session.invalidate(); 
            externalContext.redirect(contextPath);

        } catch (ServletException | IOException ex) {
            log.error(ex.getMessage());

        }  
//        return logoutPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
