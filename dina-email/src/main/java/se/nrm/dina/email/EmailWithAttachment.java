package se.nrm.dina.email;

import com.itextpdf.text.BaseColor; 
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream; 
import java.util.Date;
import java.util.Properties; 
import javax.activation.DataHandler;
import javax.activation.DataSource; 
import javax.mail.Message; 
import javax.mail.Session;
import javax.mail.Transport; 
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

/**
 *
 * @author idali
 */
public class EmailWithAttachment {
    
    
    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
//    private static final int SMTP_HOST_PORT = 465;
    private static final int SMTP_HOST_PORT = 587;
    private static final String SMTP_AUTH_USER = "dinanrm@gmail.com";
    private static final String SMTP_AUTH_PWD = "dina2012";
    
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
   
        
    /**
     * Sends an email with a PDF attachment.
     */
    public void email() {  
                 
        String sender = "dinanrm@gmail.com"; //replace this with a valid sender email address
        String recipient = "ida.li@nrm.se"; //replace this with a valid recipient email address
        String content = "dummy content"; //this will be the text of the email
        String subject = "dummy subject"; //this will be the subject of the email
         
        Properties props = new Properties();

        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", SMTP_HOST_NAME);
        props.put("mail.smtps.auth", "true");
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);
        
         
        
         
         
        ByteArrayOutputStream outputStream = null;
         
        try {          
            //construct the text body part
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(content);
             
            //now write the PDF content to the output stream
            outputStream = new ByteArrayOutputStream();
            writePdf(outputStream);
            byte[] bytes = outputStream.toByteArray();
             
            //construct the pdf body part
            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName("test.pdf");
                         
            //construct the mime multi part
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(textBodyPart);
            mimeMultipart.addBodyPart(pdfBodyPart);
             
            //create the sender/recipient addresses
            InternetAddress iaSender = new InternetAddress(sender);
            InternetAddress iaRecipient = new InternetAddress(recipient);
             
            
            MimeMessage message = new MimeMessage(session);
            message.setSubject("Loan overdue notification");
            
            
            //construct the mime message 
            message.setSender(iaSender); 
            message.setRecipient(Message.RecipientType.TO, iaRecipient);
            message.setContent(mimeMultipart);
             
            //send off the email
   
            
            
            Transport transport = session.getTransport();
            
            transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);

            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
             
            System.out.println("sent from " + sender +
                    ", to " + recipient +
                    "; server = " + SMTP_HOST_NAME + ", port = " + SMTP_HOST_PORT);        
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            //clean off
            if(null != outputStream) {
                try { outputStream.close(); outputStream = null; }
                catch(Exception ex) { }
            }
        }
    }
     
    /**
     * Writes the content of a PDF file (using iText API)
     * to the {@link OutputStream}.
     * @param outputStream {@link OutputStream}.
     * @throws Exception
     */
    public void writePdf(OutputStream outputStream) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
         
        document.open();
         
        document.addTitle("Test PDF");
        document.addSubject("Testing email PDF");
        document.addKeywords("iText, email");
        document.addAuthor("Jee Vang");
        document.addCreator("Jee Vang");
        
        
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Title of the document", catFont));

        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph("Report generated by: " + System.getProperty("user.name") + ", " + new Date(),  smallBold));
        addEmptyLine(preface, 3);
        preface.add(new Paragraph("This document describes something which is very important ",
            smallBold));

        addEmptyLine(preface, 8);

        preface.add(new Paragraph("This document is a preliminary version and not subject to your license agreement or any other agreement with vogella.com ;-).",
            redFont));

        document.add(preface);
        // Start a new page
        document.newPage();
        
        
        
         
        
          
        document.close();
    }
    
    
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
          paragraph.add(new Paragraph(" "));
        }
    }
     
    /**
     * Main method.
     * @param args No args required.
     */
    public static void main(String[] args) {
        EmailWithAttachment demo = new EmailWithAttachment();
        demo.email();
    }
}
