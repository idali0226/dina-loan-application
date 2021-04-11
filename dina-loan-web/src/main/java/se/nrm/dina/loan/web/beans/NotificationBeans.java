package se.nrm.dina.loan.web.beans;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import se.nrm.dina.loan.web.controllers.Languages;
import se.nrm.dina.mongodb.jdbc.MongoJDBC;
import se.nrm.dina.mongodb.loan.vo.Notification;

/**
 *
 * @author idali
 */
@Named("notification")
@SessionScoped
public class NotificationBeans implements Serializable {
  
  @Inject
  private MongoJDBC mongo;
   

  public NotificationBeans() {

  }

  public List<Notification> getNotifications() {
    return mongo.findActiveNotifications();
  }

}
