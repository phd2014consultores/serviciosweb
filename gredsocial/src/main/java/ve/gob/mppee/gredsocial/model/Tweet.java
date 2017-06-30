/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.gob.mppee.gredsocial.model;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Liam
 */
public class Tweet {
    
    private String Text;
    private ArrayList<Date> fechasPublicacion;

    public Tweet() {}

    public String getText() {
        return Text;
    }

    public void setText(String Text) {
        this.Text = Text;
    }

    public ArrayList<Date> getFechasPublicacion() {
        return fechasPublicacion;
    }

    public void setFechasPublicacion(ArrayList<Date> fechasPublicacion) {
        this.fechasPublicacion = fechasPublicacion;
    }
}
