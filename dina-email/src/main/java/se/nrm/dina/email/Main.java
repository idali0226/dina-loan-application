/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.email;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author idali
 */
public class Main {

  public static void main(String[] args) {
    // Recipient's email ID needs to be mentioned.
    final String username = "idali0226@gmail.com";
    final String password = "friday18";


        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.auth", "true");
//        prop.put("mail.smtp.socketFactory.port", "465");
//        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        

    Session session = Session.getInstance(prop,
            new javax.mail.Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });

    try {

      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress("idali0226@gmail.com"));
      message.setRecipients(
              Message.RecipientType.TO,
              InternetAddress.parse("ida_ho_99@yahoo.com")
      );
      message.setSubject("Testing Gmail TLS");
      message.setText("Dear Mail Crawler,"
              + "\n\n Please do not spam my email!");

      Transport.send(message);

      System.out.println("Done");

    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }
}
