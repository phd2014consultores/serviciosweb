/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.gob.mppee.gredsocial.negocio;

import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import ve.gob.mppee.gredsocial.correo.interfaz.Correo;
import ve.gob.mppee.gredsocial.model.CorreoConfig;

/**
 *
 * @author Liam
 */
public class CorreoJavaMailImpl implements Correo{

    @Override
    public void enviar(CorreoConfig correoConfig, String to, String subject, String message, File attachment) {
        
        String fileName = "";
        
        try {
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", correoConfig.getSMTP());
            props.setProperty("mail.smtp.starttls.enable", correoConfig.getTLS());
            props.setProperty("mail.smtp.port",correoConfig.getPORT());
            props.setProperty("mail.smtp.user", correoConfig.getUSER());
            props.setProperty("mail.smtp.auth", correoConfig.getAUTH());
            Session session = Session.getDefaultInstance(props);
            session.setDebug(true);
            BodyPart text = new MimeBodyPart(), attach = new MimeBodyPart();
            MimeMultipart multiParte = new MimeMultipart();
            
            
            if (message != null && !message.isEmpty()) {
                text.setText(message);
                multiParte.addBodyPart(text);
            }
            
            if (attachment != null) {
                    attach.setDataHandler(new DataHandler(new FileDataSource(attachment)));
                    attach.setFileName(attachment.getName());
                    fileName = attachment.getName();
                    multiParte.addBodyPart(attach); 
            }
            
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(correoConfig.getUSER()));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(multiParte);
            
            Transport t = session.getTransport("smtp");
            
            t.connect(correoConfig.getUSER(),correoConfig.getPASSWORD());
            t.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
            t.close();
            Logger.getLogger(CorreoJavaMailImpl.class.getName()).log(Level.INFO, 
                    "Se ha enviado el siguiente correo: " + "De: {0}" + "\n"
                            + "Para: {1}" + "\n" + "Mensaje: {2}"
                            + "\n" + "Adjunto: {3}", new Object[]{correoConfig.getUSER(), to, message, fileName});
        } catch (MessagingException ex) {
            Logger.getLogger(CorreoJavaMailImpl.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(CorreoJavaMailImpl.class.getName()).log(Level.SEVERE, 
                    "No se ha podido enviar el siguiente correo: " + "De: {0}" + "\n"
                            + "Para: {1}" + "\n" + "Mensaje: {2}"
                            + "\n" + "Adjunto: {3}", new Object[]{correoConfig.getUSER(), to, message, fileName});
        } 
    }
    
}
