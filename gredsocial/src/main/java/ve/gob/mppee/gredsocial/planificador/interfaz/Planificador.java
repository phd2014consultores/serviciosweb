/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.gob.mppee.gredsocial.planificador.interfaz;

import java.util.Date;
import java.util.TimerTask;

/**
 *
 * @author Liam
 */
public interface Planificador {
    public void crearTarea(TimerTask tarea, Date date);
}
