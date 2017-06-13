/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.gob.minpal.serviciobi.impl;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import ve.gob.minpal.serviciobi.interfaz.CargaInicial;


/**
 * Clase que implementa el servicio de carga incial que ejecuta un Kettle Job de Pentaho Data Integration 
 * 
 * @author Avelardo Gavide
 * @version Versión incial
 *
 */

/**
 * 
 * @editado por: simonts
 *  
 *
 *20/05/2016 
 *Se agregaron nuevos parametros de entrada para ejecutar el job
 *Se optimizo y adapto al nvo job que ejecuta todo
 *Se eliminaron parametros sobrantes
 *
 */


public class CargaInicialImpl implements CargaInicial {
	
	/**
	 * Método que ejecuta un comando de sistema para lanzar un Kettle Job de Pentaho Data Integration 
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
	 * @throws ExcepcionServicio
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
			)  {
		
		int iExitValue = 99;
		String commandScript = null;				
		FileWriter scriptfile = null;
        PrintWriter pwriter = null;
        Date fechaHoy = new Date();
        dirLogs = dirLogs + "/" + usuarioBDOracle + "_CINI_" + new SimpleDateFormat("yyyy-MM-dd").format(fechaHoy) + "_" + new SimpleDateFormat("HH:mm:ss.SSS").format(fechaHoy);
        String filename =  usuarioBDOracle + "_CINI_" + new SimpleDateFormat("yyyy-MM-dd").format(fechaHoy) + "_" + new SimpleDateFormat("HH:mm:ss").format(fechaHoy);
		
        commandScript = dirPDI + "/./kitchen.sh" + " -rep:" + repositorio + " -job:\"" + nombreJob +
        		"\" -param:HOST_ORACLE=\"" + hostBDOracle +
        		"\" -param:BD_ORACLE=\"" + bdOracle +
        		"\" -param:USER_ORACLE=\"" + usuarioBDOracle +
        		"\" -param:PASS_ORACLE=\"" + passUsuarioBDOracle +
        		"\" -param:HOST=\"" + hostBDCassandra +
        		"\" -param:KEYSPACE=\"" + keySpace +
        		"\" -param:COLUMNFAMILY=\"" + colummFamily +
        		"\" -param:HOST_BD_PYS=\"" + hostBDApp +
        		"\" -param:USER_BD_PYS=\"" + usuarioBDApp +
        		"\" -param:PASS_BD_PYS=\"" + passUsuarioBDApp +
        		"\" -param:BD_PYS=\"" + bdApp +
        		"\" -param:ID_PLAN_EJEC=\"" + idPlanEjec +
        		"\" -param:JOB_MODO=\"" + jobModo +
        		"\" -param:DIRLOG=\"" + dirLogs +       		        		
        		"\" -dir:" + dirEjecucion +
				" -user:" + usuarioRepositorio + " -pass:" + passUsuarioRepo + " -level:" + nivelLogs + " -log:" + dirLogs + "/" + filename + ".log";
				
		//SE CREA EL NUEVO DIRECTORIO DENTRO DE DIRLOGS
		CommandLine oCmdLine0 = CommandLine.parse("mkdir -p " + dirLogs);
		//SE AGREGA EL COMANDO QUE LE OTORGA PERMISO DE EJECUCION AL ARCHIVO
		CommandLine oCmdLine1 = CommandLine.parse("chmod +x " + dirLogs + "/" + filename + "_command.sh" );
		//SE AGREGA EL COMANDO QUE EJECUTA EL ARCHIVO
		CommandLine oCmdLine2 = CommandLine.parse(dirLogs + "/./" + filename + "_command.sh");
        DefaultExecutor oDefaultExecutor = new DefaultExecutor();
        oDefaultExecutor.setExitValue(0);
        
        try {
            //SE EJECUTA EL COMANDO PARA CREAR LA CARPETA
        	iExitValue = oDefaultExecutor.execute(oCmdLine0);
        	//UNA VEZ CREADA LALA RUTA dirLogs 
    		//YA SE PUEDE CREAR EL ARCHIVO .sh CON EL COMANDO COMPLETO
    		try{    			
    			scriptfile = new FileWriter(dirLogs + "/" + filename + "_command.sh");
    			pwriter = new PrintWriter(scriptfile);
    			pwriter.println(commandScript);
    		} catch (Exception io) {
                    io.printStackTrace();
    		} finally {
    			try {
    				if (null != scriptfile) scriptfile.close();
    			} catch (Exception io2){
    				io2.printStackTrace();
    			}			
    		}
    		
        	//SE EJECUTA EL COMANDO PARA DERLE PERMISO
        	iExitValue = oDefaultExecutor.execute(oCmdLine1);
        	//SE EJECUTA EL COMANDO QUE INICIA LA EJECUCION DEL ARCHIVO 
        	iExitValue = oDefaultExecutor.execute(oCmdLine2);        	
        
        } catch(Exception e) {
            e.printStackTrace();
        }
            return iExitValue;
	}
}

