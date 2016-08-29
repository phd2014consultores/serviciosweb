package com.phdconsultores.ws.conexionbd;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/**
 * Clase para el manejo de conexiones a la bd del servicios web
 * 
 * @author Simon Cedeno
 * @version Versión inicial
 */


public class ConexionBD {
	
	
	//private static final String rutaPropiedades = System.getProperties().getProperty("user.dir") + 
//System.getProperty("file.separator") + "db.properties";
	
	private String rutaPropiedades;	
	
	public ConexionBD(){		
		rutaPropiedades = "../lib/db.properties";
	}
	
	/**
	 * Método que devuelve una conexión a base de datos
	 * 
	 * @param url
	 * @param usuario
	 * @param clave
	 * @return Objeto de Conexión
	 */
	public Connection conexionBD() throws Exception {
		
		Properties props = cargaPropiedadesBD();
		
		Connection conn = null;
		
		try {
			String driver = "org.postgresql.Driver"; 
        	Class.forName(driver);
			conn = DriverManager.getConnection(props.getProperty("db.url"), props.getProperty("db.usuario"), props.getProperty("db.clave"));
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} catch (ClassNotFoundException ecn) {
			throw new Exception(ecn.getMessage());
		}
		
		return conn;
	}	
	
	/**
	 * Método que devuelve una conexión a base de datos
	 * 
	 * @param url
	 * @param usuario
	 * @param clave
	 * @return Objeto de Conexión
	 * @throws Exception
	 */
	public Connection conexionBD(String url, String usuario, String clave) throws Exception {
		Connection conn = null;
		
		try {
			String driver = "org.postgresql.Driver"; 
        	Class.forName(driver);
			conn = DriverManager.getConnection(url, usuario, clave);
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} catch (ClassNotFoundException ecn) {
			throw new Exception(ecn.getMessage());
		}
		
		return conn;
	}
	
	/**
	 * Método que devuelve los parámetros de conexión a la base de datos desde el archivo db.properties
	 *  
	 * 
	 * @return Parámetros de conexión en archivo Properties
	 * @throws Exception
	 */
	public Properties cargaPropiedadesBD() throws Exception {
		 Properties props = new Properties();
	     FileInputStream in = null;
	      
	     try {
	       	in = new FileInputStream(rutaPropiedades);
			props.load(in);
				
	     } catch (IOException e1) {
				throw new Exception(e1.getMessage());
				
		} finally {
	            
	        try {
	            if (in != null) {
	                in.close();
	            }
	        } catch (IOException ex) {
	        	throw new Exception(ex.getMessage());
	        }
		}
	    
	    return props;
	}
	
	/**
	 * Método que devuelve los parámetros de conexión a la base de datos desde un archivo properties
	 * 
	 * @param rutaPropiedades
	 * @return Parámetros de conexión en archivo Properties
	 * @throws Exception
	 */
	public Properties cargaPropiedadesBD(String rutaPropiedades) throws Exception {
		 Properties props = new Properties();
	     FileInputStream in = null;
	     
	     try {
	       	in = new FileInputStream(rutaPropiedades);
			props.load(in);
				
	     } catch (IOException e1) {
				throw new Exception(e1.getMessage());
				
		} finally {
	            
	        try {
	            if (in != null) {
	                in.close();
	            }
	        } catch (IOException ex) {
	        	throw new Exception(ex.getMessage());
	        }
		}
	    
	    return props;
	}
	

}
