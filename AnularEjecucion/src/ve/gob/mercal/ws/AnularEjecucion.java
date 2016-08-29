package ve.gob.mercal.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import com.phdconsultores.ws.exception.ExcepcionServicio;


/**
 * Interfaz que expone el servicio de anular ejecucion el cual detiene  un proceso kitchen del Kettle Job de Pentaho Data Integration
 * 
 * @author Simon Cedeno
 * @version Versión incial
 * 
 */

/**
 * 
 * @editado por: simonts 
 *
 *25/08/2016 
 *Se agregaron nuevos parametros de entrada para ejecutar el job
 *Se optimizo y adapto al nvo job que ejecuta todo
 *Se eliminaron parametros sobrantes
 *
 */

@WebService(name = "AnularEjecucion", targetNamespace = "http://ws.mercal.gob.ve/")
public interface AnularEjecucion {

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
	
	@WebMethod(operationName = "anularejecucion", action = "urn:AnularEjecucion")
	public int anularejecucion(
			@WebParam(name = "idPlanEjec" ) @XmlElement(name = "idPlanEjec",required=true, nillable=false) String idPlanEjec
			)throws ExcepcionServicio;
	
	
}
