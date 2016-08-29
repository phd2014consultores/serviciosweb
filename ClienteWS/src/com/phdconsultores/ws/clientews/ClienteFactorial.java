package com.phdconsultores.ws.clientews;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.soap.SOAPMessage;

import com.phdconsultores.ws.conexionws.ConexionWS;

/**
 * Clase que implementa un metodo para consumir el Web Service
 * Factorial (Servicio de test de funcionamiento) desplegado en 
 * un proxy en el MULE-ESB, con el manejo inteligente del timeout
 * 
 * @author Simon Cedeno
 * @version Versión inicial
 * 
 * 17/07/2016
 *
 */

public class ClienteFactorial {
	
	//VARIABLES GLOBALES
	private String prefijo = "ws";
	private String namespace = "http://ws.phdconsultores.com/";
	private String path = "/secure/";
	/*
	private static String prefijo = "ws";
	private static String namespace = "http://ws.phdconsultores.com/";
	private static String path = "/ws-bigdata/";	
	
	
	
	public static void main(String[] args) {
		long s = 0;
		
		try {
			s = factorial(5,"/home/simonts/Escritorio") + 1;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Test WS: " + s);
		
	}*/
	
	/**
	 * Método que permite consumir el WS Factorial (Servicio de test)
	 * 
	 * @param	num									-numero a calcular el factorial
	 * @param   dir									-directorio donde se guardara el archivo
	 * 
	 * @return	Un entero long   					- Un valor entero (0 = éxito, <>0 Fallo)										-
	 * @throws Exception
	 * 
	 */
	
	public long factorial(
	//public static long factorial(
			long num,
			String dir
			) throws Exception{
		
		long result = -4;
		
		String operacion = "factorial";
		int timeout = 300000;		
		
		ConexionWS cws = new ConexionWS();
		
		Properties prop = cws.cargaPropiedadesWS();
		
		Map<String, String> map = new  LinkedHashMap<>();
		
		//LLENAMOS EL MAP
		map.put("num", Long.toString(num));
		map.put("dir", dir);
		//
		
		//OBTENEMOS EL SOBRE
		SOAPMessage sobre = cws.crearSobre(prefijo, namespace, operacion, map);
		
		//CONSTRUIMOS LA URL VALIDA PARA INVOCAR EL WS		
		/*"https://ws.mercal-dev.gob.ve:8444/secure/factorial";*/
		String https_url =  "https://"  + prop.getProperty("mule.CN") + ":" + prop.getProperty("mule.port") + path + operacion;
		
		SOAPMessage sobre_respuesta = cws.requestSoapSeguro(prop.getProperty("keystore.client.file"), 
				prop.getProperty("keystore.client.pass"), https_url, timeout, sobre);
		
		String r = cws.respuestaProcesada(sobre_respuesta, "^[+|-]?\\d+$");
		
		if(r != null){
			result = Long.parseLong(r);
		}
		
		
		return result;
	}

}
