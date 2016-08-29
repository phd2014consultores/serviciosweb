package ve.gob.mercal.ws;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;

import com.phdconsultores.ws.exception.ExcepcionServicio;


/**
 * Clase que implementa el servicio de de anular ejecucion el cual detiene un proceso kitchen del Kettle Job de Pentaho Data Integration 
 * 
 * @author Simon Cedeno
 * @version Versión incial
 *
 */

/**
 * 
 * @editado por: simonts
 *  
 *
 *25/08/2016 
 *Se agregaron nuevos parametros de entrada para ejecutar el job
 *Se optimizo y adapto al nvo job que ejecuta todo
 *Se eliminaron parametros sobrantes
 *
 */



@WebService(targetNamespace = "http://ws.mercal.gob.ve/", endpointInterface = "ve.gob.mercal.ws.AnularEjecucion",
portName = "AnularEjecucionPort", serviceName = "AnularEjecucion")
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AnularEjecucionImpl implements AnularEjecucion {
	
	public AnularEjecucionImpl(){}
	
	/**
	 * Método que ejecuta un comando de sistema para detener un Kettle Job de Pentaho Data Integration
	 * 
	 * 
	 * @param	idPlanEjec							-Id del plan de ejecucion a detener en el host
	 * 
	 * @return
	 * @throws ExcepcionServicio
	 * 
	 * 
	 */
	
	public int anularejecucion(
			String idPlanEjec
			) throws ExcepcionServicio {
		
		int iExitValue = 99;
		String commandScript = null;
		
		commandScript = "ps aux | grep '[I]D_PLAN_EJEC=" + idPlanEjec + ".*' | grep -v 'grep' | awk '{print $2}' | xargs kill -9";
		
		CommandLine oCmdLine0 = CommandLine.parse(commandScript);
		DefaultExecutor oDefaultExecutor = new DefaultExecutor();
        oDefaultExecutor.setExitValue(0);
		
		try{
			
			//SE EJECUTA EL COMANDO PARA DETENER EL PROCESO JUNTO A SUS PROCESOS HIJOS
        	iExitValue = oDefaultExecutor.execute(oCmdLine0);
			
		}catch (ExecuteException ex) {
            
        	throw new ExcepcionServicio(ex.getMessage());
        	
        }catch(Exception e) {
        	
        	throw new ExcepcionServicio(e.getMessage());
        }
		
		return iExitValue;
	}

}
