package se.nrm.dina.loan.admin.controllers;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.mongodb.jdbc.MongoJDBC;
import se.nrm.dina.mongodb.loan.vo.Notification;

/**
 *
 * @author idali
 */
@Named(value = "notification")
@SessionScoped
@Slf4j
public class PortalNotification implements Serializable {

    private List<Notification> notifications;
    private String title;
    private String descriptionEn;
    private String descriptionSv;
    private boolean isActive = true;
    private int orderNumber;
    
    private final String updateSummary = "Updated";
    private final String updateDetail = "Notification updated";

    @Inject
    private MongoJDBC mongo;

    @PostConstruct
    public void init() {
        if (notifications == null || notifications.isEmpty()) {
            findNotifications();
        }
    }

    public void onTabChange() {
        log.info("onTabChange");
    }

    public void activateNotification() {
        log.info("activateNotification: {}", isActive);
    }

    public void findNotifications() {
        log.info("findNotifications");
        
        notifications = mongo.findNotifications();
        log.info("notification size: {}", notifications.size());
    }

    public void addNotification() {
        log.info("addNotification");
        Notification notification = new Notification(title, descriptionEn,
                descriptionSv, isActive, orderNumber);
        mongo.saveNotification(notification);
        title = null;
        descriptionEn = null;
        descriptionSv = null;
        isActive = true;
        orderNumber = 0;
        findNotifications();
    }

    public void changeNotification(Notification notification) {
        log.info("changeNotification: {}", notification);
    }
 
    public void editNotification(Notification notification) {
        log.info("editNotification : {}", notification);
        
        mongo.updateNotification(notification);
        findNotifications();
        FacesContext.getCurrentInstance()
                .addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, 
                                updateSummary, updateDetail));
    }

    public void deleteNotification(Notification notification) {
        log.info("deleteNotification: {}", notification);
        
        mongo.deleteNotification(notification);
        findNotifications();
    }
 
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getDescriptionSv() {
        return descriptionSv;
    }

    public void setDescriptionSv(String descriptionSv) {
        this.descriptionSv = descriptionSv;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }
}
