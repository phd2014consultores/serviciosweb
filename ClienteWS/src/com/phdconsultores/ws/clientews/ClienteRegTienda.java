package com.phdconsultores.ws.clientews;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.soap.SOAPMessage;

import com.phdconsultores.ws.conexionws.ConexionWS;

/**
 * Clase que implementa un metodo para consumir el Web Service
 * RegTienda desplegado en un proxy en el MULE-ESB, con el manejo
 * inteligente del timeout
 * 
 * @author Simon Cedeno
 * @version Versión inicial
 * 
 * 17/07/2016
 *
 */

public class ClienteRegTienda {

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
			s = regtienda(
					"CHAIMAS",
					"10.20.8.65",
					"CHAIMAS",
					"CHAIMAS",
					"BDLOGMOD",
					"1");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Test WS: " + s);

	}*/
	
	/**
	 * Método que permite consumir el WS RegTienda
	 *  
	 * @param	tienda								-Nombre de la tienda
	 * @param	hostBDOracle						-Host de la bd de oracle
	 * @param	usuarioBDOracle						-Usuario de la bd de oracle
	 * @param	passUsuarioBDOracle					-Pass usuario bd de oracle
	 * @param	bdOracle							-Nombre de la bd de oracle
	 * @param	idUsuarioApp						-Id del usuario de la aplicacion quien crea la tienda y a la vez sera el manager de dicha tienda
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	
	public int regtienda(
	//public static int regtienda(
			String tienda,
			String hostBDOracle,
			String usuarioBDOracle,
			String passUsuarioBDOracle,
			String bdOracle,
			String idUsuarioApp
			) throws Exception{
		
		int result = -4;
		
		String operacion = "regtienda";
		int timeout = 300000;		
		
		ConexionWS cws = new ConexionWS();
		
		Properties prop = cws.cargaPropiedadesWS();
		
		Map<String, String> map = new  LinkedHashMap<>();
		
		//LLENAMOS EL MAP
		map.put("tienda", tienda);
		map.put("hostBDOracle", hostBDOracle);
		map.put("usuarioBDOracle", usuarioBDOracle);
		map.put("passUsuarioBDOracle", passUsuarioBDOracle);
		map.put("bdOracle", bdOracle);
		map.put("idUsuarioApp", idUsuarioApp);
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
