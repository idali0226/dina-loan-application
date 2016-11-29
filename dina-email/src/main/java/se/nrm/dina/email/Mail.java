/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.email;
 
import java.io.File;
import java.io.UnsupportedEncodingException; 
import java.util.Map;     
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart; 
import javax.mail.Session; 
import javax.mail.Transport; 
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart; 
import javax.mail.internet.MimeMessage; 
import javax.mail.internet.MimeMultipart;  
import javax.mail.internet.MimeUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import se.nrm.dina.email.vo.LoanMail;
import se.nrm.dina.email.vo.NewAccountMail;
import se.nrm.dina.email.vo.PasswordRecoveryMail;

/**
 *
 * @author idali
 */
@Stateless
public class Mail {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private final String LOCAL_LOAN_PDF = "/Users/idali/Documents/onlineloans/loan_files/";
    private final String REMOTE_LOAN_PDF = "/home/admin/wildfly-8.0.0-2/loans/";
    
    private LoanMail loan;   
    private NewAccountMail account;
    private PasswordRecoveryMail recovery;
            
     
    private String pdfFileName;
      
    @Resource(name = "java:/mail/app")
    private Session session;

    public void sendNewAdminAccountMail(String username, String password, String emailAddress) {
        logger.info("sendNewAdminAccountMail : {} ", emailAddress);

        emailAddress = "ida.li@nrm.se";
        Message message = new MimeMessage(session);
        session.setDebug(true);
        account = new NewAccountMail();
        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress)); 
            message.setSubject(MimeUtility.encodeText("Loan admin new account", "utf-8", "B"));

            message.setContent(account.appendMailBody(username, password), "text/html; charset=ISO-8859-1"); 
            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            logger.warn(ex.getMessage());
        }
    }
    
    public void sendPasswordRecoverEmail(final String email, final String password) {
        logger.info("sendPasswordRecoverEmail : {}", email);

        Message message = new MimeMessage(session);
        session.setDebug(true);
        try {
            recovery = new PasswordRecoveryMail();
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject(MimeUtility.encodeText("Loan admin password recovery", "utf-8", "B"));
            message.setContent(recovery.buildPasswordRecoveryMsg(password), "text/html; charset=ISO-8859-1");
            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            logger.warn(ex.getMessage()); 
        }
    }


    public void send(Map<String, String> map) throws AddressException, MessagingException, UnsupportedEncodingException {

        logger.info("send : {} -- {}", map.get("primaryemail"), map.get("secondaryemail"));
        
        Message message = new MimeMessage(session);
        session.setDebug(true);

        loan = new LoanMail(map);
        buildPdfFilePath(map, "adminSummaryFile");

        String primaryEmail = map.get("primaryemail");
        String secondaryEmail = map.get("secondaryemail");
        
        String adminEmail = map.get("manager");
        String curatorEmail = map.get("curratormail");
         

        //  for testing now
        adminEmail = primaryEmail;
        curatorEmail = primaryEmail;
         
        String outofoffice = map.get("outofoffice");
        boolean isOut = outofoffice == null ? false : Boolean.valueOf(outofoffice); 
 
        if (Boolean.valueOf(map.get("hasPrimaryContact"))) {
            sendMailToPrimaryBorrower(primaryEmail, message); 
            message = new MimeMessage(session); 
            sendMailToBorrower(secondaryEmail, message);
        } else {
            sendMailToBorrower(primaryEmail, message);
        }
 
        message = new MimeMessage(session); 
        sendMailToAdmin(adminEmail, curatorEmail, message);
        
        if(isOut) {
            message = new MimeMessage(session);  
            sendOutOfOfficeNotification(primaryEmail, secondaryEmail, message);
        } 
    }
    
    private void buildPdfFilePath(Map<String, String> map, String key) { 
        StringBuilder sb = new StringBuilder();
        sb.append(Boolean.valueOf(map.get("isLocal")) ? LOCAL_LOAN_PDF : REMOTE_LOAN_PDF); 
        sb.append(map.get(key).replace("-", "/")); 
         
        pdfFileName = sb.toString();
        logger.info("pdffilename : {}", pdfFileName);
    }
    
    private void sendOutOfOfficeNotification(String primary, String secondary, Message message) throws MessagingException, UnsupportedEncodingException {
        
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(primary)); 
        if(secondary != null && !secondary.isEmpty()) {
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(secondary));
        }
         
        message.setSubject(MimeUtility.encodeText(loan.buildOutOfOfficeSubject(), "utf-8", "B"));  
        message.setContent(loan.buildOutOfOfficeMailBody(), "text/html; charset=ISO-8859-1");
        Transport.send(message);  
    }
    
    
    private void sendMailToBorrower(String emailAddress, Message message) throws MessagingException {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
        message.setSubject(loan.buildSubject()); 
        
        message.setContent(loan.buildBorrowerMailBody(), "text/html; charset=ISO-8859-1");
        Transport.send(message);  
    }
     
    private void sendMailToPrimaryBorrower(String emailAddress, Message message) throws MessagingException, UnsupportedEncodingException { 
         
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
        message.setSubject(loan.buildSubject());
 
        message.setContent(loan.buildPrimaryBorrowerMailBody(), "text/html; charset=ISO-8859-1");
        Transport.send(message); 
    }

    private void sendMailToAdmin(String adminEmail, String curatorEmail, Message message) throws MessagingException, UnsupportedEncodingException {
        logger.info("sendAdminMail : {} -- {}", adminEmail, curatorEmail);
        
        String testAddress = "ida.li@nrm.se";
        
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(testAddress));
        message.addRecipient(Message.RecipientType.CC, new InternetAddress(testAddress));
        message.setSubject(MimeUtility.encodeText(loan.buildAdminMailSubject(), "utf-8", "B") ); 
 
        String body = loan.buildAdminEmailBody(); 
        
        BodyPart messageBodyPart1 = new MimeBodyPart();
        messageBodyPart1.setContent(body, "text/html; charset=ISO-8859-1");
         
        File pdfFile = new File(pdfFileName);
         
        MimeBodyPart messageBodyPart2 = new MimeBodyPart();
        DataSource source = new FileDataSource(pdfFile);
        messageBodyPart2.setDataHandler(new DataHandler(source));
        
        
        messageBodyPart2.setFileName(loan.getPdfFileName());

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart1);
        multipart.addBodyPart(messageBodyPart2);

        message.setContent(multipart);
        Transport.send(message);
    }

 
    
    public void send(String email, String id, String agreement) {
        
        logger.info("send : {} -- {}", email, id);
        
        Message message = new MimeMessage(session);    
        try { 
            InternetAddress emailAddress = new InternetAddress(email);
            message.addRecipient(Message.RecipientType.TO, emailAddress);
            
            message.setSubject(loan.buildSubject()); 
            
        } catch (MessagingException ex) {
            logger.warn(ex.getMessage());
        }
    }
}
