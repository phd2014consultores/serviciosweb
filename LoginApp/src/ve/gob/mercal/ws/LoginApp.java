package ve.gob.mercal.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import com.phdconsultores.ws.exception.ExcepcionServicio;


/**
 * Interfaz que expone el servicio loginapp en la aplicacion
 * 
 * @author Simon Cedeno
 * @version Versión inicial
 *
 *30/05/2016 
 *Se crea el ws con sus respectivos parametros iniciales
 *
 */

@WebService(name = "LoginApp", targetNamespace = "http://ws.mercal.gob.ve/")
public interface LoginApp {
	
	/**
	 * Método que invoca una funcion almacenada en la bd de la aplicacion
	 * 
	 * @param	usuarioApp								-Nombre del usuario  en la aplicacion
	 * @param	passwordUsuario							-Password del usuario  
	 * 
	 * @return
	 * @throws ExcepcionServicio
	 * 
	 */
	
	@WebMethod(operationName = "loginapp", action = "urn:LoginApp")
	public String loginapp(			
			@WebParam(name = "usuarioApp" ) @XmlElement(name = "usuarioApp",required=true, nillable=false) String usuarioApp,
			@WebParam(name = "passwordUsuario" ) @XmlElement(name = "passwordUsuario",required=true, nillable=false) String passwordUsuario
			
			) throws ExcepcionServicio;

}
