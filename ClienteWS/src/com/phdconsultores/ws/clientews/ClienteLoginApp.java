package com.phdconsultores.ws.clientews;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.soap.SOAPMessage;

import com.phdconsultores.ws.conexionws.ConexionWS;

/**
 * Clase que implementa un metodo para consumir el Web Service
 * LoginApp desplegado en un proxy en el MULE-ESB, con el manejo
 * inteligente del timeout
 * 
 * @author Simon Cedeno
 * @version Versión inicial
 * 
 * 17/07/2016
 *
 */

public class ClienteLoginApp {
	
	//VARIABLES GLOBALES
	private String prefijo = "ws";
	private String namespace = "http://ws.mercal.gob.ve/";
	private String path = "/ws-bigdata/";
	
	/*
	private static String prefijo = "ws";
	private static String namespace = "http://ws.mercal.gob.ve/";
	private static String path = "/ws-bigdata/";
	
	
	public static void main(String[] args) {
		String s = "";
		
		try {
			s = loginapp("admin","12345678");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Test WS: " + s);
		
	}*/
	
	/**
	 * Método que permite consumir el WS LoginApp
	 * 
	 * @param	usuarioApp								-Nombre del usuario  en la aplicacion
	 * @param	passwordUsuario							-Password del usuario  
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	
	public String loginapp(
	//public static String loginapp(
			String usuarioApp,
			String passwordUsuario
			) throws Exception{
		
		//String result = "{\"id_usuario\": -4, \"acceso\": null}";
		String result = "{\"id_usuario\": -4, \"usuario\": \"null\", \"password\": \"null\", \"tipo_usuario\": \"null\", \"acceso\": null}";
		
		String operacion = "loginapp";
		int timeout = 300000;		
		
		ConexionWS cws = new ConexionWS();
		
		Properties prop = cws.cargaPropiedadesWS();
		
		Map<String, String> map = new  LinkedHashMap<>();
		
		//LLENAMOS EL MAP
		map.put("usuarioApp", usuarioApp);
		map.put("passwordUsuario", passwordUsuario);
		//
		
		//OBTENEMOS EL SOBRE
		SOAPMessage sobre = cws.crearSobre(prefijo, namespace, operacion, map);
		
		//CONSTRUIMOS LA URL VALIDA PARA INVOCAR EL WS		
		/*"https://ws.mercal-dev.gob.ve:8443/ws-bigdata/loginapp";*/
		String https_url =  "https://"  + prop.getProperty("mule.CN") + ":" + prop.getProperty("mule.port") + path + operacion;
		
		SOAPMessage sobre_respuesta = cws.requestSoapSeguro(prop.getProperty("keystore.client.file"), 
				prop.getProperty("keystore.client.pass"), https_url, timeout, sobre);
		
		String r = cws.respuestaProcesada(sobre_respuesta, "^\\{.+\\}$");
		
		if(r != null){
			result = r;
		}
		
		return result;
	}
	
	

}
