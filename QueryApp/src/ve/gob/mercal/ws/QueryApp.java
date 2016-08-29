package ve.gob.mercal.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import com.phdconsultores.ws.exception.ExcepcionServicio;


/**
 * Interfaz que expone el servicio queryapp en la aplicacion
 * 
 * @author Simon Cedeno
 * @version Versión inicial
 *
 *05/06/2016 
 *Se crea el ws con sus respectivos parametros iniciales
 *
 */

@WebService(name = "QueryApp", targetNamespace = "http://ws.mercal.gob.ve/")
public interface QueryApp {
	
	/**
	 * Método que recibe como parametro un query que ejecuta en la base de datos bd_pys y devuelve el resultado en formato json
	 * 
	 * 
	 * @param	query								-Query a ejecutar en la base de datos
	 * 
	 * @return	
	 * @throws ExcepcionServicio
	 * 
	 */
	
	@WebMethod(operationName = "queryapp", action = "urn:queryapp")
	public String queryapp(
			@WebParam(name = "query" ) @XmlElement(name = "query",required=true, nillable=false) String query			
	)throws ExcepcionServicio;

}
