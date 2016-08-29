package ve.gob.mercal.ws;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.google.gson.Gson;
import com.phdconsultores.ws.exception.ExcepcionServicio;
import com.phdconsultores.ws.conexionbd.ConexionBD;

/**
 * Clase que implementa el servicio QueryApp en la aplicacion
 * 
 * @author Simon Cedeno
 * @version Versión inicial
 *
 *30/05/2016 
 *Se crea el ws con sus respectivos parametros iniciales
 *
 */

@WebService(targetNamespace = "http://ws.mercal.gob.ve/", endpointInterface = "ve.gob.mercal.ws.QueryApp",
portName = "QueryAppPort", serviceName = "QueryAppService")
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class QueryAppImpl implements QueryApp {
	
	public QueryAppImpl(){};
	
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
	
	public String queryapp(
			
			String query
			
			) throws ExcepcionServicio {
		
		//VARIABLES A USAR		
		String consulta = new String("[]");		
		ConexionBD cbd = new ConexionBD();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Properties props = new Properties();
		Map<String, String> map;
		ArrayList<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
		Gson gson = new Gson();
		
		try{
			
			props = cbd.cargaPropiedadesBD();
			conn = cbd.conexionBD(props.getProperty("db.url"), props.getProperty("db.usuario"), props.getProperty("db.clave"));			
        	pst = conn.prepareStatement(query);        		
			rs = pst.executeQuery();
			
			int count = rs.getMetaData().getColumnCount();
			
			while (rs.next()) {
				//map = new HashMap<>();
				map = new  LinkedHashMap<>();
				for (int i = 1; i <= count; i++){					
					map.put(rs.getMetaData().getColumnName(i), rs.getString(i));
				}		        
		        mapList.add(map);
            }			
			
			consulta = gson.toJson(mapList);			
			
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
