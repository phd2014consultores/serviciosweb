package com.phdconsultores.ws.clientews;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.soap.SOAPMessage;

import com.phdconsultores.ws.conexionws.ConexionWS;

/**
 * Clase que implementa los metodo para consumir los Web Services
 * desplegados en un proxy en el MULE-ESB, con el manejo
 * inteligente del timeout
 * 
 * @author Simon Cedeno
 * @version Versión inicial
 * 
 * 17/07/2016
 *
 */

public class ClienteWS {
	
	private String prefijo = "ws";
	private String namespace = "http://ws.mercal.gob.ve/";
	private String path = "/ws-bigdata/";
	
	
	/**
	 * Método que permite consumir el WS AnularEjecucion
	 * 
	 * @param	idPlanEjec								-Id del plan de ejecucion a detener en el host	 *  
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	
	public int anularejecucion(	
			String idPlanEjec
			) throws Exception{
		
		int result = -1;
		
		String operacion = "anularejecucion";
		int timeout = 300000;		
		
		ConexionWS cws = new ConexionWS();
		
		Properties prop = cws.cargaPropiedadesWS();
		
		Map<String, String> map = new  LinkedHashMap<>();
		
		//LLENAMOS EL MAP
		map.put("idPlanEjec", idPlanEjec);			
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
	
	
	/**
	 * Método que permite consumir el WS CargaInicial
	 *  
	 * @param	dirPDI								-Directorio donde esta el pdi en el host
	 * @param	nombreJob							-Nombre del job a ejecutar
	 * @param	dirEjecucion						-Directorio del repositorio donde se encuentra el job a ejecutar
	 * @param	repositorio							-Nombree del repositorio
	 * @param	usuarioRepositorio					-Nombre del usuario del repositorio
	 * @param	passUsuarioRepo						-Password del usuario del repositorio
	 * 
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
	 * 
	 * @param	dirLogs								-Nombre del directorio para almacenar logs
	 * @param	nivelLogs							-Nivel de los logs a ejecutar: Basic, Detailed, Debug, Rowlevel, Error, Nothing 
	 *
	 * @return Un entero    						-Un valor entero (0 = éxito, <>0 Fallo)
	 * @throws Exception
	 */
	
	public int cargainicial(	
			String dirPDI,
			String nombreJob,
			String dirEjecucion,
			String repositorio,
			String usuarioRepositorio,					
			String passUsuarioRepo,
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
			String dirLogs,
			String nivelLogs
			) throws Exception{
		
		int result = -1;
		
		String operacion = "cargainicial";
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
	
	/**
	 * Método que permite consumir el WS CargaInicialEtl
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
	 * 
	 * @param	dirLogs								-Nombre del directorio para almacenar logs
	 * @param	nivelLogs							-Nivel de los logs a ejecutar: Basic, Detailed, Debug, Rowlevel, Error, Nothing 
	 *
	 * @return Un entero    						-Un valor entero (0 = éxito, <>0 Fallo)
	 * @throws Exception
	 */
	
	public int cargainicialetl(	
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
			String dirLogs,
			String nivelLogs
			) throws Exception{
		
		int result = -1;
		
		String operacion = "cargainicialetl";
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
			String usuarioApp,
			String passwordUsuario
			) throws Exception{
		
		String result = "{\"id_usuario\": -4, \"acceso\": null}";
		
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
	
	/**
	 * Método que permite consumir el WS Mediacion 
	 * 
	 * @param	dirPDI								-Directorio donde esta el pdi en el host
	 * @param	nombreJob							-Nombre del job a ejecutar
	 * @param	dirEjecucion						-Directorio del repositorio donde se encuentra el job a ejecutar
	 * @param	repositorio							-Nombree del repositorio
	 * @param	usuarioRepositorio					-Nombre del usuario del repositorio	 * 
	 * @param	passUsuarioRepo						-Password del usuario del repositorio
	 * 
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
	 * 
	 * @param	dirLogs								-Nombre del directorio para almacenar logs
	 * @param	nivelLogs							-Nivel de los logs a ejecutar: Basic, Detailed, Debug, Rowlevel, Error, Nothing 
	 *
	 * @return Un entero    						-Un valor entero (0 = éxito, <>0 Fallo)
	 * @throws Exception
	 */
	
	public int mediacion(	
			String dirPDI,
			String nombreJob,
			String dirEjecucion,
			String repositorio,
			String usuarioRepositorio,					
			String passUsuarioRepo,
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
			String dirLogs,
			String nivelLogs
			) throws Exception{
		
		int result = -1;
		
		String operacion = "mediacion";
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
