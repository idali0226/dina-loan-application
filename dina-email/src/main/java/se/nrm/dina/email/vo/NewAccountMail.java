package se.nrm.dina.email.vo;
 
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idali
 */
@Slf4j
public class NewAccountMail {
 

  private final String startDivTagWithFont = "<div style=\"font-size: 1.2em; \">";
  private final String startDivTag = "<div>";
  private final String endDivTag = "</div>";
  private final String brTag = "<br />";
  private final String start3Tag = "<h3>";
  private final String endH3Tag = "</h3>";
  private final String startLingTag = "<a href=\"";
  
  private final String link = "\" target=\"_blank\">NRM Online Loan Form Administration</a>";
  
  private final String usernamelbl = "Username: ";
  private final String passwordlbl = "Passwork: ";
  
  private final String hi = "Hi ";
  private final String newAccountCreateMsg = "Your online loan account has been created with the following information:  ";
  private final String changePasswordMsg = " to login and change password";
  
//  private final boolean isLocal;

  public NewAccountMail() {
//    isLocal = System.getProperty("os.name").trim().equals("Mac OS X");
  }

  public String appendMailBody(String username, String password, String host) { 
    log.info("Account ");

    StringBuilder sb = new StringBuilder();
    sb.append(startDivTagWithFont);
    sb.append(startDivTag);
    sb.append(start3Tag);
    sb.append(hi);
    sb.append(username);
    sb.append(endH3Tag);
    sb.append(brTag);
    sb.append(brTag);

    sb.append(newAccountCreateMsg);
    sb.append(brTag);
    sb.append(brTag);
    sb.append(usernamelbl);
    sb.append(username);
    sb.append(brTag);
    sb.append(passwordlbl);
    sb.append(password);
    sb.append(brTag);
    sb.append(brTag);
    sb.append("Click ");
    sb.append(startLingTag);
    sb.append(host);
     
    sb.append(link);
    sb.append(changePasswordMsg);
    sb.append(endDivTag);
    sb.append(endDivTag); 
    
    return sb.toString();
  }

}
