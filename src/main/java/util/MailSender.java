/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import model.ChapterModel;

/**
 *
 * @author Usuario
 */
public class MailSender extends Thread{
    
    public MailSender(HttpServletRequest request, PropertiesReader pReader, List<String> addresses, ChapterModel cm){
    this.run(addresses, pReader, cm, request);
    }
    
    
    
    public void run(List<String> addresses, PropertiesReader pReader, ChapterModel cm, HttpServletRequest request){
        
     //Get the session object
      Properties properties = System.getProperties();
      properties.put("mail.smtp.host", "smtp.gmail.com");
      properties.setProperty("mail.smtp.starttls.enable", "true");
      properties.setProperty("mail.smtp.port", "587");
      properties.setProperty("mail.smtp.user", "mangaku.subscribe@gmail.com");
      properties.setProperty("mail.smtp.auth", "true");
      Session session = Session.getDefaultInstance(properties);
      String host = "localhost:"+request.getServerPort();
      String bodyText = "<h1>New Manga chapter!</h1>"
              + "<p>The following series: <h2><b>"+cm.getMangaName()+"</b></h2>, has been updated, "
              + "and a brand new chapter is on! It's chapter No. "+cm.getChapterNumber()+". "
              + "<a href=\"http://"+host+"/Mangaku/views/chapter.html?id="+cm.getChapterId()+"&page=1\">Read now!</a></p>";
      

     //compose the message
      try{             
         MimeMessage message = new MimeMessage(session);
         message.setFrom(new InternetAddress(pReader.getValue("mailUser")));
         for (String e: addresses){
         message.addRecipient(Message.RecipientType.TO,new InternetAddress(e));
         }
         message.setSubject("New "+ cm.getMangaName().toUpperCase() + " chapter is on!");
         message.setText(bodyText, "UTF-8", "html");

         // Send message
        Transport t = session.getTransport("smtp");
        t.connect(pReader.getValue("mailUser"), pReader.getValue("mailPassword"));
        t.sendMessage(message, message.getAllRecipients());
        t.close();
         System.out.println("message sent successfully....");

      }catch (MessagingException mex) {mex.printStackTrace();}
    }
}
