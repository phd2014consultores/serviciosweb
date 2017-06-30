/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.gob.mppee.gredsocial.planificador.impl;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import ve.gob.mppee.gredsocial.planificador.interfaz.Planificador;

/**
 *
 * @author Liam
 */
public class PlanificadorTimerImpl extends Timer implements Planificador {

    @Override
    public void crearTarea(TimerTask tarea, Date date) {
        Timer timer = new Timer();
	timer.schedule(tarea, date);
    }
    
}
