package com.phdconsultores.ws.clientews;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.soap.SOAPMessage;

import com.phdconsultores.ws.conexionws.ConexionWS;

/**
 * Clase que implementa un metodo para consumir el Web Service
 * QueryApp desplegado en un proxy en el MULE-ESB, con el manejo
 * inteligente del timeout
 * 
 * @author Simon Cedeno
 * @version Versión inicial
 * 
 * 17/07/2016
 *
 */

public class ClienteQueryApp {
	
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
			s = queryapp("select * from etls limit 1000;");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Test WS: " + s);
		
	}*/
	
	/**
	 * Método que permite consumir el WS QueryApp
	 * 
	 * 
	 * @param	query								-Query a ejecutar en la base de datos
	 * 
	 * @return	
	 * @throws Exception
	 * 
	 */
	
	public String queryapp(
	//public static String queryapp(
			String query
			) throws Exception{
		
		String result = "[]";
		
		String operacion = "queryapp";
		int timeout = 300000;		
		
		ConexionWS cws = new ConexionWS();
		
		Properties prop = cws.cargaPropiedadesWS();
		
		Map<String, String> map = new  LinkedHashMap<>();
		
		//LLENAMOS EL MAP
		map.put("query", query);			
		//
		
		//OBTENEMOS EL SOBRE
		SOAPMessage sobre = cws.crearSobre(prefijo, namespace, operacion, map);
		
		//CONSTRUIMOS LA URL VALIDA PARA INVOCAR EL WS		
		String https_url =  "https://"  + prop.getProperty("mule.CN") + ":" + prop.getProperty("mule.port") + path + operacion;
		
		SOAPMessage sobre_respuesta = cws.requestSoapSeguro(prop.getProperty("keystore.client.file"), 
				prop.getProperty("keystore.client.pass"), https_url, timeout, sobre);
		
		String r = cws.respuestaProcesada(sobre_respuesta, "^\\[\\{.+\\}\\]$");
		
		if(r != null){
			result = r;
		}
		
		
		return result;
	}

}
