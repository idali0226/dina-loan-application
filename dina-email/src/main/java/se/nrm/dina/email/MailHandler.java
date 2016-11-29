package se.nrm.dina.email;
  
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter; 
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;  
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import java.util.Properties;     
import javax.activation.DataHandler;
import javax.activation.DataSource; 
import javax.ejb.Stateless; 
import javax.mail.Message;
import javax.mail.MessagingException;  
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart; 
import javax.mail.util.ByteArrayDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;     
//import se.nrm.dina.email.model.loan.MailAttachment;
import se.nrm.dina.email.model.loan.OverDueLoan; 
import se.nrm.dina.email.model.loan.Person; 
import se.nrm.dina.email.model.loan.util.HelpClass;

/**
 * Send email
 */ 
@Stateless
public class MailHandler implements Serializable {
    
    Logger logger = LoggerFactory.getLogger(this.getClass());
    
//    private final static String SMTP_HOST_NAME = "mail.smtp.host";
//    private final static String HOST = "mailserver.nrm.se";
    
    private static final int SMTP_HOST_PORT = 587;
    private static final String SMTP_AUTH_USER = "dina@mail.dina-web.net";
    private static final String SMTP_AUTH_PWD = "D-I-N-A";

    private final static String SMTP_HOST_NAME = "mail.dina-web.net";
    private final static String HOST = "mail.dina-web.net";

    private static final String BODY_CONTENT = "The overdue loan list is attached.                   ";

    
    private static final Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD); 
    private static final Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private static final Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
    private static final Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
    
    private static final String TEXT_1 = "The following loans are overdue:";
     
     
    public MailHandler() {
        
    } 
    
    public void sendMail(Map<String, List<OverDueLoan>> map)  { 
//        MailAttachment ma = new MailAttachment();
        
        map.entrySet().stream()
                .filter(m -> m.getKey() != null)
                .forEach(m -> {
                    sendWithAttachment(m.getValue(), m.getKey());
                }); 
    }
    
    private void sendWithAttachment(List<OverDueLoan> overDueLoans, String address) {
         
        logger.info("sendWithAttachment : {}", address);

        // address to test
//        address = "ida.li@nrm.se";
        
        
        
        Properties props = new Properties();
        props.put(SMTP_HOST_NAME, HOST);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtps.host", SMTP_HOST_NAME);
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtp.port", SMTP_HOST_PORT);
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, null);
        session.setDebug(true);

        ByteArrayOutputStream outputStream = null;
         
        try {    
            //construct the text body part
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(BODY_CONTENT);
             
            //now write the PDF content to the output stream
            outputStream = new ByteArrayOutputStream();
            writePdf(outputStream, overDueLoans);
            byte[] bytes = outputStream.toByteArray();
            
            //construct the pdf body part
            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName("OverdueLoan.pdf");
            
            //construct the mime multi part
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(textBodyPart);
            mimeMultipart.addBodyPart(pdfBodyPart);
 
            Message message = new MimeMessage(session); 
            message.setSubject("Loan overdue notification");
 
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
            message.addRecipient(Message.RecipientType.CC, new InternetAddress("ida.li@nrm.se"));
            message.setContent(mimeMultipart); 
            message.setFrom(new InternetAddress("dina@mail.dina-web.net"));
            
            
            InternetAddress[] toaddress = new InternetAddress[]{new InternetAddress(address)};
 
            Transport transport = session.getTransport();
            transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD); 
            transport.sendMessage(message, toaddress); 
            transport.close(); 
            
            
//            Transport.send(message); 
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        } finally {
            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    logger.error(ex.getMessage());
                }
            } 
        }
    }
    
    /**
     * Writes the content of a PDF file (using iText API)
     * to the {@link OutputStream}.
     * @param outputStream {@link OutputStream}.
     * @throws Exception
     */
    private void writePdf(OutputStream outputStream, List<OverDueLoan> overDueLoans) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
         
        document.open();
         
        document.addTitle("Loan overdue notification");
        document.addSubject("Loan overdue notification");
        document.addKeywords("iText, email");
        document.addAuthor("DINA-Project");
        document.addCreator("DINA-Project");
        
        
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph(TEXT_1, catFont)); 
        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph(" Report generated by: DINA Admin, " + new Date(), subFont));
        addEmptyLine(preface, 2);

        preface.add(new Paragraph(" Loan number                   Loan date                Overdue date", smallBold));
        preface.add(new Paragraph("________________________________________________________________________________", normalFont));

        Map<String, List<OverDueLoan>> map = getBorrowerMap(overDueLoans);

        StringBuilder secondaryBorrowerSB;
        StringBuilder primaryBorrowerSB;
        for (Map.Entry<String, List<OverDueLoan>> entry : map.entrySet()) {

            List<OverDueLoan> list = entry.getValue();
            Person borrower = list.get(0).getBorrower();

            String borrowerFirstName = "";
            String borrowerLastName = "";
            String borrowerEmail = "";

            if(borrower != null) {  
                if(borrower.getAgentFirstName() != null) {
                    borrowerFirstName = borrower.getAgentFirstName();
                }  
                
                if(borrower.getAgentLastName() != null) {
                    borrowerLastName = borrower.getAgentLastName();
                }   
                
                if(borrower.getAgentEmail() != null) {
                    borrowerEmail = borrower.getAgentEmail();
                }  
            }   
              
            primaryBorrowerSB = new StringBuilder();
            
            primaryBorrowerSB.append(" Primary borrower: ");
            primaryBorrowerSB.append(borrowerFirstName);
            primaryBorrowerSB.append(" ");
            primaryBorrowerSB.append(borrowerLastName);
            primaryBorrowerSB.append("                  ");
            primaryBorrowerSB.append(borrowerEmail);
            preface.add(new Paragraph(primaryBorrowerSB.toString(), smallBold));  
            
            
            
            for(OverDueLoan overDueLoan : list) {
                Person sdBorrower = overDueLoan.getSecondaryBorrower();
                String sdBorrowerFirstName = "";
                String sdBorrowerLastName = "";
                String sdBorrowerEmail = "";
                
                if(sdBorrower != null) {  
                    if(sdBorrower.getAgentFirstName() != null) {
                        sdBorrowerFirstName = sdBorrower.getAgentFirstName();
                    }  

                    if(sdBorrower.getAgentLastName() != null) {
                        sdBorrowerLastName = sdBorrower.getAgentLastName();
                    }   

                    if(sdBorrower.getAgentEmail() != null) {
                        sdBorrowerEmail = sdBorrower.getAgentEmail();
                    }  
                    secondaryBorrowerSB = new StringBuilder();
                    secondaryBorrowerSB.append("    Secondary borrower: ");
                    secondaryBorrowerSB.append(sdBorrowerFirstName);
                    secondaryBorrowerSB.append(" ");
                    secondaryBorrowerSB.append(sdBorrowerLastName);
                    secondaryBorrowerSB.append("                  ");
                    secondaryBorrowerSB.append(sdBorrowerEmail);
                    preface.add(new Paragraph(secondaryBorrowerSB.toString(), smallBold));   
                }   
                 
                String loanNumber = overDueLoan.getLoanNumber();
                Date loanDate = overDueLoan.getLoanDate(); 
                Date overDueDate = overDueLoan.getDueDate();
                  
                StringBuilder sb = new StringBuilder();
                sb.append("       ");
                sb.append(loanNumber);
                sb.append("                   ");
                sb.append(HelpClass.dateToString(loanDate));
                sb.append("                   ");
                sb.append(HelpClass.dateToString(overDueDate));
                
                
                preface.add(new Paragraph(sb.toString(), normalFont));  
                preface.add(new Paragraph("________________________________________________________________________________", normalFont));
            }
            addEmptyLine(preface, 2); 
        } 
         
        document.add(preface); 
        document.newPage();  
        document.close();
    }
    
    
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
          paragraph.add(new Paragraph(" "));
        }
    }
    
    
    private Map<String, List<OverDueLoan>> getBorrowerMap(List<OverDueLoan> OverDueLoans) {
        Map<String, List<OverDueLoan>> map = new HashMap<>();
        
        for(OverDueLoan loan : OverDueLoans) { 
            Person person = loan.getBorrower();
            String key = "key";
            if(person != null) {
                key = person.getAgentEmail();
            } 
            if(map.containsKey(key)) {
                List list = map.get(key);
                list.add(loan);
            } else {
                List<OverDueLoan> list = new ArrayList<>();
                list.add(loan);
                map.put(key, list);
            } 
        }
        
        return map;
    }
    
    
 
    public static void main(String[] args) throws MessagingException {
        MailHandler handler = new MailHandler();  
    }  
}
