package ve.gob.mercal.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import com.phdconsultores.ws.exception.ExcepcionServicio;


/**
 * Interfaz que expone el servicio regtienda en la aplicacion
 * 
 * @author Simon Cedeno
 * @version Versión inicial
 *
 *30/05/2016 
 *Se crea el ws con sus respectivos parametros iniciales
 *
 */

@WebService(name = "RegTienda", targetNamespace = "http://ws.mercal.gob.ve/")
public interface RegTienda {
	
	/**
	 * Método que invoca una funcion almacenada para registrar una nueva tienda en la aplicacion
	 * 
	 * 
	 * @param	tienda								-Nombre de la tienda
	 * @param	hostBDOracle						-Host de la bd de oracle
	 * @param	usuarioBDOracle						-Usuario de la bd de oracle
	 * @param	passUsuarioBDOracle					-Pass usuario bd de oracle
	 * @param	bdOracle							-Nombre de la bd de oracle
	 * @param	idUsuarioApp						-Id del usuario de la aplicacion quien crea la tienda y a la vez sera el manager de dicha tienda
	 *  
	 * 
	 * @return
	 * @throws ExcepcionServicio
	 * 
	 * 
	 */
	
	
	@WebMethod(operationName = "regtienda", action = "urn:RegTienda")
	public int regtienda(
			
			@WebParam(name = "tienda" ) @XmlElement(name = "tienda",required=true, nillable=false) String tienda,
			@WebParam(name = "hostBDOracle" ) @XmlElement(name = "hostBDOracle",required=true, nillable=false) String hostBDOracle,
			@WebParam(name = "usuarioBDOracle" ) @XmlElement(name = "usuarioBDOracle",required=true, nillable=false) String usuarioBDOracle,
			@WebParam(name = "passUsuarioBDOracle" ) @XmlElement(name = "passUsuarioBDOracle",required=true, nillable=false) String passUsuarioBDOracle,
			@WebParam(name = "bdOracle" ) @XmlElement(name = "bdOracle",required=true, nillable=false) String bdOracle,
			@WebParam(name = "idUsuarioApp" ) @XmlElement(name = "idUsuarioApp",required=true, nillable=false) String idUsuarioApp		
			
	)throws ExcepcionServicio;
	
}
