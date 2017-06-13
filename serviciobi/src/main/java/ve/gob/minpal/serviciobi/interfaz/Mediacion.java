/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.gob.minpal.serviciobi.interfaz;


/**
 * Interfaz para manejar la mediación que ejecuta un Kettle Job de Pentaho Data Integration
 * 
 * @author Dr. Pedro Nolasco Bonillo Ramos, Lic. Luiscarl Jordán
 * @version Version 2
 * 
 */
public interface Mediacion {
	
	/**
	 * Metodo que ejecuta un comando de mediacion de un ftp al sistema bi de minpal a traves de un Kettle Job de Pentaho Data Integration
	 * 
	 * @param	pentaho_directorio_ejecucion_data_integration	- Ruta del directorio del servidor donde esta instalado el Pentaho Data Integration (PDI)							-Directorio donde esta el pdi en el host
	 * @param	pentaho_repositorio_nombre						- Nombre del repositorio pentaho
	 * @param	pentaho_repositorio_usuario						- Usuario del repositorio pentaho
	 * @param	pentaho_repositorio_password					- Password del usuario del repositorio pentaho
	 * @param	pentaho_repositorio_ruta_job					- Directorio del repositorio donde se encuentra el job a ejecutar
	 * @param	pentaho_nombre_job								- Nombre del job a ejecutar
	 * @param	pentaho_directorio_logs							- Ruta del directorio para almacenar logs
	 * @param	pentaho_nivel_logs								- Nivel de los logs a ejecutar: Basic, Detailed, Debug, Rowlevel, Error, Nothing 

	 * @param	ftp_servidor_ip									- Direccion IP del servidor ftp remoto desde donde se tomara el archivo para importarlo en el BI de minpal
	 * @param	ftp_servidor_puerto								- Puerto del servidor ftp remoto desde donde se tomara el archivo para importarlo en el BI de minpal
     * @param	ftp_servidor_usuario							- Usuario para conectarse al servidor ftp remoto desde donde se tomara el archivo para importarlo en el BI de minpal
     * @param	ftp_servidor_password							- Password del usuario para conectarse al servidor ftp remoto desde donde se tomara el archivo para importarlo en el BI de minpal
     * @param	ftp_directorio_remoto							- Directorio que contiene los archivos dentro del servidor ftp remoto desde donde se tomara el archivo para importarlo en el BI de minpal
     * @param	ftp_directorio_remoto_procesados				- Directorio que contiene los archivos procesados dentro del servidor ftp remoto desde donde se tomara el archivo para importarlo en el BI de minpal
     * @param	ftp_expresion_regular_archivos					- Expresion regular para seleccionar los archivos que serán procesados, por ejemplo para todos los txt *.\*.txt
     * @param	ftp_directorio_local							- Directorio local que contiene los archivos procesados

     * @param	hadoop_servidor_ip								- Direccion ip del nodo de hadoop que recibira los datos
     * @param	hadoop_servidor_puerto							- Puerto del nodo de hadoop que recibira los datos
     * @param	hadoop_servidor_usuario							- Usuario del nodo de hadoop que recibira los datos
     * @param	hadoop_servidor_password						- Password del usuario del nodo de hadoop que recibira los datos
     * @param	hadoop_ruta_directorio							- Ruta del direcorio de hadoop donde se colocaran los datos
     * 
     * @param	solr_servidor_ip								- Direccion ip del nodo de solr que recibira los datos
     * @param	solr_servidor_puerto							- Puerto del nodo de solr que recibira los datos
     * @param	solr_core_destino								- Core de Solr que recibira los datos
	 * 
     * @return Un entero    									- Un valor entero (0 = exito y <> 0 error)
	 */
	
	public int mediacion (			
			String pentaho_directorio_ejecucion_data_integration,
			String pentaho_repositorio_nombre,
			String pentaho_repositorio_usuario,
			String pentaho_repositorio_password,
			String pentaho_repositorio_ruta_job,			
			String pentaho_nombre_job,
			String pentaho_directorio_logs,
			String pentaho_nivel_logs,
			
			String ftp_servidor_ip,
			String ftp_servidor_puerto,
			String ftp_servidor_usuario,
			String ftp_servidor_password,
			String ftp_directorio_remoto,
			String ftp_directorio_remoto_procesados,
			String ftp_expresion_regular_archivos,
			String ftp_directorio_local,

			String hadoop_servidor_ip,
			String hadoop_servidor_puerto,
			String hadoop_servidor_usuario,
			String hadoop_servidor_password,
			String hadoop_ruta_directorio,
			
			String solr_servidor_ip,
			String solr_servidor_puerto,
			String solr_core_destino			
			);
}


