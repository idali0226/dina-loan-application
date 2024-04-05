package se.nrm.dina.email;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.email.vo.LoanMail;
import se.nrm.dina.email.vo.NewAccountMail; 

/**
 *
 * @author idali
 */
@Stateless
@Slf4j
public class NrmMail {

    private final String smtpHost = "mail.smtp.host"; 
    private final String mailHost = "mail.nrm.se";
     

    private LoanMail loan;
 
    private final String fromMail = "no-reply@nrm.se";

//    private final String testAdminMail = "bioinformatics@nrm.se";
//    private final String testCuratorEmail = "ida.li@nrm.se";
//    private final String testBorrowMail = "ida.li@nrm.se";
    
    private final String newAccountSubject = "Loan admin new account";
//    private final String passwordRecordSubject = "Loan admin password recovery";
    private final String utf8 = "utf-8";
    private final String encodB = "B";
    
    private final String dash = "-";
    private final String slash = "/";
    
    private final String textHtml = "text/html; charset=ISO-8859-1";
    
    private final String adminSummaryFile = "adminSummaryFile";
    private String pdfPath;
    
    private NewAccountMail account; 
 
    private String pdfAdminFileName;
    
    private Properties props;
    private Session session;
    private Message message;
    

//    private String testEmail;
    public NrmMail() {

    }

    /**
     * Email send to new admin user when new admin account created
     *
     * @param username
     * @param password
     * @param emailAddress
     * @param host
     */
    public void sendNewAdminAccountMail(String username, String password, 
            String emailAddress, String host) {
        log.info("sendNewAdminAccountMail : {} ", emailAddress);

        props = new Properties();
        props.put(smtpHost, mailHost);
        session = Session.getInstance(props, null);

        message = new MimeMessage(session);
        session.setDebug(true);

        account = new NewAccountMail(); 
        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
            message.setSubject(MimeUtility.encodeText(newAccountSubject, utf8, encodB));
            message.setFrom(new InternetAddress(fromMail));

            message.setContent(account.appendMailBody(username, password, host), textHtml);
            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            log.error(ex.getMessage());
        }
    }
     
     
    
    /**
     * Email send to curator and loan requester when a new loan request created
     *
     * @param map  
     * @throws AddressException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void send(Map<String, String> map) throws AddressException,
            MessagingException, UnsupportedEncodingException {
        log.info("send primary -- secondary: {} -- {}", map.get("primaryemail"), map.get("secondaryemail"));
        log.info("send 1 manager -- curratormail : {} -- {}", map.get("manager"), map.get("curratormail"));

        props = new Properties();
        props.put(smtpHost, mailHost);
        session = Session.getInstance(props, null);
        session.setDebug(true);

        loan = new LoanMail(map);
        pdfPath = map.get("pdfPath");
        
        pdfAdminFileName = buildPdfFilePath(map, adminSummaryFile, pdfPath);

        String primaryEmail = map.get("primaryemail");
        String secondaryEmail = map.get("secondaryemail");

        String adminEmail = map.get("manager");
        String curatorEmail = map.get("curratormail");

        log.info("email ... : {} -- {}", adminEmail, curatorEmail);
        log.info("email 2... : {} -- {}", primaryEmail, secondaryEmail);

//        testEmail = primaryEmail;
        // for testing now
//        adminEmail = testAdminMail;
//        curatorEmail = testCuratorEmail;
//
//        primaryEmail = testBorrowMail;
//        secondaryEmail = testBorrowMail;

        String outofoffice = map.get("outofoffice");
        boolean isOut = outofoffice == null ? false : Boolean.parseBoolean(outofoffice);

        message = new MimeMessage(session);
 
        
        if (Boolean.parseBoolean(map.get("hasPrimaryContact"))) {
            sendMailToPrimaryBorrower(primaryEmail, message);
            message = new MimeMessage(session);
            sendMailToBorrower(secondaryEmail, message);
        } else {
            sendMailToBorrower(primaryEmail, message);
        } 
        message = new MimeMessage(session);
        sendMailToAdmin(adminEmail, curatorEmail, message);
 
        if (isOut) {  
            message = new MimeMessage(session);
            sendOutOfOfficeNotification(primaryEmail, secondaryEmail, message);
        }
    }

    private String buildPdfFilePath(Map<String, String> map, String key, String pdfPath) {

        StringBuilder sb = new StringBuilder();  
        sb.append(pdfPath); 
  
        sb.append(map.get(key).replace(dash, slash));

        return sb.toString(); 
    }

    private void sendOutOfOfficeNotification(String primary, String secondary, Message message)
            throws MessagingException, UnsupportedEncodingException {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(primary));

        if (secondary != null && !secondary.isEmpty()) {
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(secondary));
        }

        message.setSubject(MimeUtility.encodeText(
                loan.buildOutOfOfficeSubject(), utf8, encodB));
        message.setFrom(new InternetAddress(fromMail));

        message.setContent(loan.buildOutOfOfficeMailBody(), textHtml);
        Transport.send(message);
    }

    private void sendMailToBorrower(String emailAddress, Message message) throws MessagingException {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
        message.setSubject(loan.buildSubject());
        message.setFrom(new InternetAddress(fromMail));

        message.setContent(loan.buildBorrowerMailBody(), "text/html; charset=ISO-8859-1");
        Transport.send(message);
    }

    private void sendMailToPrimaryBorrower(String emailAddress, Message message) throws MessagingException, UnsupportedEncodingException {

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
        message.setSubject(loan.buildSubject());
        message.setFrom(new InternetAddress(fromMail));

        message.setContent(loan.buildPrimaryBorrowerMailBody(), textHtml);
        Transport.send(message);
    }
    
    
    private void sendMailToAdmin(String adminEmail, String curatorEmail, Message message)
            throws MessagingException, UnsupportedEncodingException {
        log.info("sendAdminMail : {} -- {}", adminEmail, curatorEmail);

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(adminEmail));
        message.addRecipient(Message.RecipientType.CC, new InternetAddress(curatorEmail));
        message.setFrom(new InternetAddress(fromMail));
        message.setSubject(MimeUtility.encodeText(
                loan.buildAdminMailSubject(), utf8, encodB));

        String body = loan.buildAdminEmailBody();

        BodyPart messageBodyPart1 = new MimeBodyPart();
        messageBodyPart1.setContent(body, textHtml);

        File pdfFile = new File(pdfAdminFileName);

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
    

    
    
    //    public void sendPasswordRecoverEmail(final String email, final String password) {
//        log.info("sendPasswordRecoverEmail : {}", email);
//
//        props = new Properties();
//        props.put(smtpHost, mailHost);
//
//        session = Session.getInstance(props, null);
//        message = new MimeMessage(session);
//        try {
//            recovery = new PasswordRecoveryMail();
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
//            message.setSubject(MimeUtility.encodeText(passwordRecordSubject, utf8, encodB));
//            message.setFrom(new InternetAddress(fromMail));
//            message.setContent(recovery.buildPasswordRecoveryMsg(password), textHtml);
//
//            Transport.send(message);
//        } catch (MessagingException | UnsupportedEncodingException ex) {
//            log.warn(ex.getMessage());
//        }
//    }

    
    
    
    
    
//    private void sendMailToAdmin1(String adminEmail, String curatorEmail, Message message)
//            throws MessagingException, UnsupportedEncodingException {
//        log.info("sendAdminMail : {} -- {}", adminEmail, curatorEmail);
//          
//        message.addRecipient(Message.RecipientType.TO, new InternetAddress(adminEmail));
//        message.addRecipient(Message.RecipientType.CC, new InternetAddress(curatorEmail));
//      
//        message.setSubject(MimeUtility.encodeText(loan.buildAdminMailSubject(), "utf-8", "B") );
//
//        String body = loan.buildAdminEmailBody(); 
//        
//        BodyPart messageBodyPart1 = new MimeBodyPart();
//        messageBodyPart1.setContent(body, "text/html; charset=ISO-8859-1");
//         
//        File pdfFile = new File(pdfFileName);
//         
//        MimeBodyPart messageBodyPart2 = new MimeBodyPart();
//        DataSource source = new FileDataSource(pdfFile);
//        messageBodyPart2.setDataHandler(new DataHandler(source));
//        
//        
//        messageBodyPart2.setFileName(loan.getPdfFileName());
//
//        Multipart multipart = new MimeMultipart();
//        multipart.addBodyPart(messageBodyPart1);
//        multipart.addBodyPart(messageBodyPart2);
//
//        message.setContent(multipart);
//        Transport.send(message);  
//    } 
//    
    
    
    
    
    
    
    
    
    
    
}
