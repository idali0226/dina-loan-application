package se.nrm.dina.email.vo;
 

/**
 *
 * @author idali
 */
public class PasswordRecoveryMail {
     
    private final String startDivTagWithFont = "<div style=\"font-size: 1.2em; \">";
    private final String startDivTag = "<div>"; 
    private final String endDivTag = "</div>";
    private final String brTag = "<br />"; 
//    private final String START_LINK_TAG = "<a href=\""; 
    
    private final String hiKey = "Hi,";
    private final String text1 = "Your online loan account password has been chaged.  You can login with the new password:  ";
    private final String passwordKey = "Password: ";
    private final String clickKey = "Click ";
    private final String text2 = " to login and change password";
    
    public PasswordRecoveryMail() { 
    }

    public String buildPasswordRecoveryMsg(final String password, String host) {
        StringBuilder sb = new StringBuilder();
        sb.append(startDivTagWithFont);
        sb.append(startDivTag);
        sb.append(hiKey);
        sb.append(brTag);
        sb.append(brTag);
        sb.append(text1);
        sb.append(brTag);
        sb.append(brTag); 
        sb.append(passwordKey);
        sb.append(password);
        sb.append(brTag);
        sb.append(brTag);
        sb.append(clickKey);
//        sb.append(START_LINK_TAG); 
        sb.append(host);  
        sb.append(text2);
        sb.append(endDivTag);
        sb.append(endDivTag);

        return sb.toString();
    }

}
