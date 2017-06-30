/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.gob.mppee.gredsocial.correo.interfaz;

import java.io.File;
import ve.gob.mppee.gredsocial.model.CorreoConfig;

/**
 *
 * @author Liam
 */
public interface Correo {
    public void enviar(
            CorreoConfig correoConfig,
            String to,
            String subject,
            String message,
            File attachments);
}
