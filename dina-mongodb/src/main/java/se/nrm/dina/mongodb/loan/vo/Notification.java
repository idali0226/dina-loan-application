package se.nrm.dina.mongodb.loan.vo;

import org.jongo.marshall.jackson.oid.ObjectId;

/**
 *
 * @author idali
 */
public class Notification {

  @ObjectId
  protected String _id;
  private String title;
  private String descriptionEn;
  private String descriptionSv;
  private boolean isActive;
  private int orderNumber;

  public Notification() {

  }

  public Notification(String title, String descriptionEn, String descriptionSv,
          boolean isActive, int orderNumber) {
    this.title = title;
    this.descriptionEn = descriptionEn;
    this.descriptionSv = descriptionSv;
    this.isActive = isActive;
    this.orderNumber = orderNumber;
  }

  public String getId() {
    return _id;
  }

  public void setId(String _id) {
    this._id = _id;
  }
   
  public String getTitle() {
    return title;
  }

  public String getDescriptionEn() {
    return descriptionEn;
  }

  public String getDescriptionSv() {
    return descriptionSv;
  }

  public boolean isIsActive() {
    return isActive;
  }

  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setDescriptionEn(String descriptionEn) {
    this.descriptionEn = descriptionEn;
  }

  public void setDescriptionSv(String descriptionSv) {
    this.descriptionSv = descriptionSv;
  }

  public int getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(int orderNumber) {
    this.orderNumber = orderNumber;
  }
   
  @Override
  public String toString() {
    return "Notification: " + title + ", " + descriptionEn + ", " + 
            descriptionSv + ", " + isActive;
  }
}
