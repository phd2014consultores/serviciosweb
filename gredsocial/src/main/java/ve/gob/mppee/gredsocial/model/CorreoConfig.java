/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.gob.mppee.gredsocial.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Liam
 */
public class CorreoConfig {
    private String SMTP;
    private String TLS;
    private String PORT;
    private String AUTH;
    private String USER;
    private String PASSWORD;

    public CorreoConfig() {
        try {
            Properties prop = new Properties();
            InputStream is = CorreoConfig.class.getResourceAsStream("../../../../../../correo.properties");
            prop.load(is);
            this.SMTP = prop.getProperty("mail.smtp.host");
            this.PORT = prop.getProperty("mail.smtp.port");
            this.TLS = prop.getProperty("mail.smtp.starttls.enable");
            this.AUTH = prop.getProperty("mail.smtp.auth");
            this.USER = prop.getProperty("mail.smtp.user");
            this.PASSWORD = prop.getProperty("mail.smtp.password");
        } catch(IOException ex) {
            Logger.getLogger(CorreoConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getSMTP() {
        return SMTP;
    }

    public void setSMTP(String SMTP) {
        this.SMTP = SMTP;
    }

    public String getTLS() {
        return TLS;
    }

    public void setTLS(String TLS) {
        this.TLS = TLS;
    }

    public String getPORT() {
        return PORT;
    }

    public void setPORT(String PORT) {
        this.PORT = PORT;
    }

    public String getAUTH() {
        return AUTH;
    }

    public void setAUTH(String AUTH) {
        this.AUTH = AUTH;
    }

    public String getUSER() {
        return USER;
    }

    public void setUSER(String USER) {
        this.USER = USER;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }
}
