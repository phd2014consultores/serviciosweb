package ve.gob.mercal.ws;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.phdconsultores.ws.exception.ExcepcionServicio;
import com.phdconsultores.ws.conexionbd.ConexionBD;


/**
 * Clase que implementa el servicio funcionapp en la aplicacion
 * 
 * @author Simon Cedeno
 * @version Versión inicial
 *
 *30/05/2016 
 *Se crea el ws con sus respectivos parametros iniciales
 *
 */

@WebService(targetNamespace = "http://ws.mercal.gob.ve/", endpointInterface = "ve.gob.mercal.ws.LoginApp",
portName = "LoginAppPort", serviceName = "LoginAppService")
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class LoginAppImpl implements LoginApp {
	
	public LoginAppImpl(){}
	
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
	
	public String loginapp(			
			String usuarioApp,
			String passwordUsuario
			) throws ExcepcionServicio {
		
		//VARIABLES A USAR
		String query = "select public.login_usuario('" + usuarioApp + "','" + passwordUsuario + "');";
		String consulta = "{\"id_usuario\": -3, \"acceso\": null}";  //VALOR POR DEFECTO, SI FALLA A ESTE NIVEL, EL ID DEL USUARIO ES -3		
		ConexionBD cbd = new ConexionBD();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Properties props = new Properties();
		
		try{
			
			props = cbd.cargaPropiedadesBD();
			conn = cbd.conexionBD(props.getProperty("db.url"), props.getProperty("db.usuario"), props.getProperty("db.clave"));			
        	pst = conn.prepareStatement(query);        		
			rs = pst.executeQuery();
			//SI LA FUNCION ES EJECUTADA EXITOSAMENTE DEVOLVERA UN RESULTADO
			if(rs.next()) {
		        consulta = rs.getString(1);
		        				
            }			
			
		} catch (SQLException sq) {
			throw new ExcepcionServicio(sq.getMessage());
		
		} catch (Exception e) {
			throw new ExcepcionServicio(e.getMessage());
		
		} finally {

            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }

            } catch (SQLException ex) {
            	throw new ExcepcionServicio(ex.getMessage());
            }
        } 
        
		return consulta;		
	}

}
