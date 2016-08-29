package ve.gob.mercal.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import com.phdconsultores.ws.exception.ExcepcionServicio;


/**
 * Interfaz que expone el servicio funcionapp en la aplicacion
 * 
 * @author Simon Cedeno
 * @version Versión inicial
 *
 *30/05/2016 
 *Se crea el ws con sus respectivos parametros iniciales
 *
 */

@WebService(name = "FuncionApp", targetNamespace = "http://ws.mercal.gob.ve/")
public interface FuncionApp {
	
	/**
	 * Método que invoca una funcion almacenada en la bd de la aplicacion
	 * 
	 * @param	invFuncion								-String formado por la invocacion de la funcion en la bd	 *  
	 * 
	 * @return
	 * @throws ExcepcionServicio
	 * 
	 */
	
	@WebMethod(operationName = "funcionapp", action = "urn:FuncionApp")
	public int funcionapp(
			
			@WebParam(name = "invFuncion" ) @XmlElement(name = "invFuncion",required=true, nillable=false) String invFuncion		
			
	)throws ExcepcionServicio;
	
	//Se puede agregar otras funciones

}
