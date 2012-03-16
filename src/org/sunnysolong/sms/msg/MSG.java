package org.sunnysolong.sms.msg;

import java.util.Properties;
import java.util.Date; 
import javax.mail.*;
import javax.mail.internet.*;

public class MSG {

    public MSG() {
    }

    public void msgsend() {
        String username = "13418651172";
        String password = "04251006Bm";
        String smtphost = "139.com";
        String compression = "Compression Option goes here - find out more";
        String from = "13418651172@139.com";
        String to = "570624044@qq.com";
        String body = "Hello,SunnySoLong!";
        Transport tr = null;

        try {
         Properties props = System.getProperties();
         props.put("mail.smtp.auth", "true");

         // Get a Session object
         Session mailSession = Session.getDefaultInstance(props, null);

         // construct the message
         Message msg = new MimeMessage(mailSession);

         //Set message attributes
         msg.setFrom(new InternetAddress(from));
         InternetAddress[] address = {new InternetAddress(to)};
         msg.setRecipients(Message.RecipientType.TO, address);
         msg.setSubject(compression);
         msg.setText(body);
         msg.setSentDate(new Date());

         tr = mailSession.getTransport("smtp");
         tr.connect(smtphost, username, password);
         msg.saveChanges();
         tr.sendMessage(msg, msg.getAllRecipients());
         tr.close();
         } catch (Exception e) {
             e.printStackTrace();
         }
    }

      public static void main(String[] argv) {
    	  MSG msg = new MSG();
    	  msg.msgsend();
      }
} 