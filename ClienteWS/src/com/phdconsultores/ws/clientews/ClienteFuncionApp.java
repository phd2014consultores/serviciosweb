package com.phdconsultores.ws.clientews;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.soap.SOAPMessage;

import com.phdconsultores.ws.conexionws.ConexionWS;

/**
 * Clase que implementa un metodo para consumir el Web Service
 * FuncionApp desplegado en un proxy en el MULE-ESB, con el manejo
 * inteligente del timeout
 * 
 * @author Simon Cedeno
 * @version Versión inicial
 * 
 * 17/07/2016
 *
 */

public class ClienteFuncionApp {

	//VARIABLES GLOBALES
	private String prefijo = "ws";
	private String namespace = "http://ws.mercal.gob.ve/";
	private String path = "/ws-bigdata/";
	
	/*
	private static String prefijo = "ws";
	private static String namespace = "http://ws.mercal.gob.ve/";
	private static String path = "/ws-bigdata/";
	
	
	public static void main(String[] args) {
		int s = 0;
		
		try {
			s = funcionapp("public.get_id_tienda('OCASTILLO')");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Test WS: " + s);
		
	}*/
	
	/**
	 * Método que permite consumir el WS FuncionApp
	 * 
	 * @param	invFuncion								-String formado por la invocacion de la funcion en la bd	 *  
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	
	public int funcionapp(
	//public static int funcionapp(
			String invFuncion
			) throws Exception{
		
		int result = -4;
		
		String operacion = "funcionapp";
		int timeout = 300000;		
		
		ConexionWS cws = new ConexionWS();
		
		Properties prop = cws.cargaPropiedadesWS();
		
		Map<String, String> map = new  LinkedHashMap<>();
		
		//LLENAMOS EL MAP
		map.put("invFuncion", invFuncion);			
		//
		
		//OBTENEMOS EL SOBRE
		SOAPMessage sobre = cws.crearSobre(prefijo, namespace, operacion, map);
		
		//CONSTRUIMOS LA URL VALIDA PARA INVOCAR EL WS		
		String https_url =  "https://"  + prop.getProperty("mule.CN") + ":" + prop.getProperty("mule.port") + path + operacion;
		
		SOAPMessage sobre_respuesta = cws.requestSoapSeguro(prop.getProperty("keystore.client.file"), 
				prop.getProperty("keystore.client.pass"), https_url, timeout, sobre);
		
		String r = cws.respuestaProcesada(sobre_respuesta, "^[+|-]?\\d+$");
		
		if(r != null){
			result = Integer.parseInt(r);
		}		
		
		return result;
	}	

}
