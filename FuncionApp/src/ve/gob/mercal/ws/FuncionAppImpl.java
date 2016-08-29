package ve.gob.mercal.ws;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

@WebService(targetNamespace = "http://ws.mercal.gob.ve/", endpointInterface = "ve.gob.mercal.ws.FuncionApp",
portName = "FuncionAppPort", serviceName = "FuncionAppService")
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class FuncionAppImpl implements FuncionApp {
	
	public FuncionAppImpl(){}
	
	/**
	 * Método que invoca una funcion almacenada en la bd de la aplicacion
	 * 
	 * @param	invFuncion								-String formado por la invocacion de la funcion en la bd	 *  
	 * 
	 * @return
	 * @throws ExcepcionServicio
	 * 
	 */
	
	public int funcionapp(
			String invFuncion			
			) throws ExcepcionServicio {
		
		//VARIABLES A USAR
		int iExitValue = -3; //VALOR POR DEFECTO
		String consulta = "select " + invFuncion + ";";
		Pattern patron; 
	 	Matcher match;
		ConexionBD cbd = new ConexionBD();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Properties props = new Properties();
		
		try {			
			props = cbd.cargaPropiedadesBD();
			conn = cbd.conexionBD(props.getProperty("db.url"), props.getProperty("db.usuario"), props.getProperty("db.clave"));			
        	pst = conn.prepareStatement(consulta);        		
			rs = pst.executeQuery();
			//SI LA FUNCION ES EJECUTADA EXITOSAMENTE DEVOLVERA UN RESULTADO
			if(rs.next()) {
		        consulta = rs.getString(1);
		        if(Pattern.matches("^\\s*([+|-]?\\d+)\\s*$", consulta)){
		        	patron = Pattern.compile("^\\s*([+|-]?\\d+)\\s*$");
					match = patron.matcher(consulta);
					match.find();
		        	iExitValue = Integer.parseInt(match.group(1));
		        }				
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
		
		return iExitValue;		
	}
	
	//Se puede agregar otras funciones
	
}
