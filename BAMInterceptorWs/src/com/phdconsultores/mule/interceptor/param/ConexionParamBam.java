package com.phdconsultores.mule.interceptor.param;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class ConexionParamBam {
	
	private String rutaPropiedades;

	public ConexionParamBam() throws URISyntaxException {		
		this.rutaPropiedades = (new File(this.getClass().getProtectionDomain().getCodeSource()
				.getLocation().toURI().getPath())).getParent().toString() + "/mule-app-bam.properties"; 
		
	}
	
	/**
	 * Método que devuelve los parámetros de conexión al BAM desde el archivo mule-app-bam.properties
	 *  
	 * 
	 * @return Parámetros de conexión en objeto Properties
	 * @throws Exception
	 */
	public Properties cargaPropiedadesBD() throws Exception {
		 Properties props = null;
	     FileInputStream in = null;
	      
	     try {
	       	in = new FileInputStream(rutaPropiedades);
	       	props = new Properties();
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
