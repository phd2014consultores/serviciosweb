package com.phdconsultores.ws.clientews;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.soap.SOAPMessage;

import com.phdconsultores.ws.conexionws.ConexionWS;

/**
 * Clase que implementa un metodo para consumir el Web Service
 * MediacionEtl desplegado en un proxy en el MULE-ESB, con el manejo
 * inteligente del timeout
 * 
 * @author Simon Cedeno
 * @version Versión inicial
 * 
 * 17/07/2016
 *
 */

public class ClienteMediacionEtl {
	
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
			s = mediacionetl(
					"/home/bigdata/mercal/etl",
					"INICIAR_MEDIACION_ETL",
					"/MERCAL/MEDIACION/JOB_MEDIACION",
					"desarrollo",
					"admin",
					"admin",
					"CARGAINICIAL_C_IDIOMA,CARGAINICIAL_C_NIVELES,CARGAINICIAL_PV_APERTURAS_X_DIA",
					"10.20.8.65",
					"OCASTILLO",
					"OCASTILLO",
					"BDLOGMOD",
					"10.24.26.61",
					"central",
					"mercal",
					"10.24.26.61",
					"bigdata",
					"bigdata",
					"db_pys",
					"NULL",
					"CARGA",
					"04-JUN-16 05.11.19,303827 PM",
					"09-JUN-16 04.04.50,987195 PM",
					"/home/bigdata/mercal/etl/log",
					"Debug");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Test WS: " + s);

	}*/
	
	/**
	 * Método que permite consumir el WS MediacionEtl
	 * 
	 * 
	 * @param	dirPDI								-Directorio donde esta el pdi en el host
	 * @param	nombreJob							-Nombre del job a ejecutar
	 * @param	dirEjecucion						-Directorio del repositorio donde se encuentra el job a ejecutar
	 * @param	repositorio							-Nombree del repositorio
	 * @param	usuarioRepositorio					-Nombre del usuario del repositorio
	 * @param	passUsuarioRepo						-Password del usuario del repositorio
	 * 
	 * @param	transformaciones					-Nombres de las transformaciones a ejecutar separadas por coma
	 * @param	hostBDOracle						-Ip o nombre del host de la bd en Oracle
	 * @param	usuarioBDOracle						-Nombre del usuario de la bd en Oracle
	 * @param	passUsuarioBDOracle					-Password del usuario de la bd en oracle
	 * @param	bdOracle							-Nombre de la base de datos en Oracle
	 * @param	hostBDCassandra						-Host de la BD Cassandra
	 * @param	colummFamily						-Nombre del column family en Cassandra
	 * @param	keySpace							-Nombre del keyspace en Cassandra
	 * 
	 * @param	hostBDApp							-Ip o nombre del host de la bd de la aplicacion
	 * @param	usuarioBDApp						-Nombre del usuario de la bd de la aplicacion
	 * @param	passUsuarioBDApp					-Password del usuario de la bd de la aplicacion
	 * @param	bdApp								-Nombre de la base de datos de la aplicacion
	 * @param	idPlanEjec							-Identificador del plan de ejecucion
	 * @param	jobModo								-Modo de ejecucion del Job: CARGA o CONTROL
	 * @param	timestampIni						-Timestamp donde inicia el filtrado de la data a cargar 
	 * @param	timestampFin						-Timestamp donde finaliza el filtrado de la data a cargar
	 * 
	 * @param	dirLogs								-Nombre del directorio para almacenar logs
	 * @param	nivelLogs							-Nivel de los logs a ejecutar: Basic, Detailed, Debug, Rowlevel, Error, Nothing 
	 * 
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	
	public int mediacionetl(
	//public static int mediacionetl(
			String dirPDI,
			String nombreJob,
			String dirEjecucion,
			String repositorio,
			String usuarioRepositorio,					
			String passUsuarioRepo,			
			String transformaciones,			
			String hostBDOracle,
			String usuarioBDOracle,
			String passUsuarioBDOracle,
			String bdOracle,
			String hostBDCassandra,
			String colummFamily,
			String keySpace,			
			String hostBDApp,
			String usuarioBDApp,
			String passUsuarioBDApp,
			String bdApp,
			String idPlanEjec,
			String jobModo,
			String timestampIni,
			String timestampFin,
			String dirLogs,
			String nivelLogs
			) throws Exception{
		
		int result = -1;
		
		String operacion = "mediacionetl";
		//DEBIDO A QUE ESTE ES UN SERVICIO CUYA EJECUCION NATURAL TARDA MUCHO TIEMPO
		//ENTONCES SE COLOCO UN TIMEOUT DE 3 SEGUNDOS, CON LA IDEA DE QUE ESTE CLIENTE
		//PUEDA RETORNAR UNA RESPUESTA "ELEGANTE" EN UN CORTO TIEMPO
		// A QUIEN LE INVOCA, CLARO ESTA RESPUESTA NO CORRESPONDE A LA REALIDAD
		//SINO QUE ES UNA FORMA DE RETORNAR "ALGO" EN UN CORTO TIEMPO A LA APLICACION
		//AQUI LO IMPORTANTE ES QUE EL PROCESO INVOCADO SE ESTA EJECUTANDO EN EL SERVIDOR
		
		int timeout = 3000;		
		
		ConexionWS cws = new ConexionWS();
		
		Properties prop = cws.cargaPropiedadesWS();
		
		Map<String, String> map = new  LinkedHashMap<>();
		
		//LLENAMOS EL MAP
		map.put("dirPDI", dirPDI);
		map.put("nombreJob", nombreJob);
		map.put("dirEjecucion", dirEjecucion);
		map.put("repositorio", repositorio);
		map.put("usuarioRepositorio", usuarioRepositorio);
		map.put("passUsuarioRepo", passUsuarioRepo);		
		map.put("transformaciones", transformaciones);		
		map.put("hostBDOracle", hostBDOracle);
		map.put("usuarioBDOracle", usuarioBDOracle);
		map.put("passUsuarioBDOracle", passUsuarioBDOracle);
		map.put("bdOracle", bdOracle);
		map.put("hostBDCassandra", hostBDCassandra);
		map.put("colummFamily", colummFamily);
		map.put("keySpace", keySpace);
		map.put("hostBDApp", hostBDApp);
		map.put("usuarioBDApp", usuarioBDApp);
		map.put("passUsuarioBDApp", passUsuarioBDApp);
		map.put("bdApp", bdApp);
		map.put("idPlanEjec", idPlanEjec);
		map.put("jobModo", jobModo);		
		map.put("timestampIni", timestampIni);
		map.put("timestampFin", timestampFin);		
		map.put("dirLogs", dirLogs);
		map.put("nivelLogs", nivelLogs);
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
