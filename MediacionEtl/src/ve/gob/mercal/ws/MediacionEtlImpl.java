package ve.gob.mercal.ws;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;

import com.phdconsultores.ws.exception.ExcepcionServicio;


/**
 * Clase que implementa el servicio de mediacionetl que ejecuta un Kettle Job de Pentaho Data Integration, 
 * donde se puede indicar las transformaciones específicas a ejecutar
 * 
 * @author Simon Cedeno
 * @version Versión incial
 *
 *20/15/2016 
 *Se agregaron nuevos parametros de entrada para ejecutar el job
 *Se optimizo y adapto al nvo job que ejecuta todo
 *Se eliminaron parametros sobrantes
 *
 */

@WebService(targetNamespace = "http://ws.mercal.gob.ve/", endpointInterface = "ve.gob.mercal.ws.MediacionEtl",
portName = "MediacionEtlPort", serviceName = "MediacionEtlService")
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class MediacionEtlImpl implements MediacionEtl {
	
	public MediacionEtlImpl(){}
	
	/**
	 * Método que ejecuta un comando de sistema para lanzar un Kettle Job de Pentaho Data Integration
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
	 * @throws ExcepcionServicio
	 * 
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
			) throws ExcepcionServicio {
		
		int iExitValue = 99;
		String commandScript = null;				
		FileWriter scriptfile = null;
        PrintWriter pwriter = null;
        Date fechaHoy = new Date();
        dirLogs = dirLogs + "/" + usuarioBDOracle + "_MED_ETL_" + new SimpleDateFormat("yyyy-MM-dd").format(fechaHoy) + "_" + new SimpleDateFormat("HH:mm:ss.SSS").format(fechaHoy);
        String filename =  usuarioBDOracle + "_MED_ETL_" + new SimpleDateFormat("yyyy-MM-dd").format(fechaHoy) + "_" + new SimpleDateFormat("HH:mm:ss").format(fechaHoy);
		
      //SET TRANSFORMACIONES
        transformaciones = transformaciones.replaceAll("[^a-zA-Z0-9,_-]","");
        //
        transformaciones = transformaciones.replaceAll(",{2,}", ",");  //comas consecutivas
		transformaciones = transformaciones.replaceAll("^,", ""); //coma al inicio
		transformaciones = transformaciones.replaceAll(",$", ""); //coma al final
        //
		transformaciones = transformaciones.replace(",", "|");
        //
		
		//Construimos el comando con los parametros obtenidos        
        commandScript = dirPDI + "/./kitchen.sh" + " -rep:" + repositorio + " -job:\"" + nombreJob +
        		"\" -param:TRANSFORMACIONES=\"" + transformaciones +        		
        		"\" -param:HOST_ORACLE=\"" + hostBDOracle +
        		"\" -param:BD_ORACLE=\"" + bdOracle +
        		"\" -param:USER_ORACLE=\"" + usuarioBDOracle +
        		"\" -param:PASS_ORACLE=\"" + passUsuarioBDOracle +
        		"\" -param:HOST=\"" + hostBDCassandra +
        		"\" -param:COLUMNFAMILY=\"" + colummFamily +
        		"\" -param:KEYSPACE=\"" + keySpace +
        		"\" -param:HOST_BD_PYS=\"" + hostBDApp +
        		"\" -param:USER_BD_PYS=\"" + usuarioBDApp +
        		"\" -param:PASS_BD_PYS=\"" + passUsuarioBDApp +
        		"\" -param:BD_PYS=\"" + bdApp +
        		"\" -param:ID_PLAN_EJEC=\"" + idPlanEjec +
        		"\" -param:JOB_MODO=\"" + jobModo +
        		"\" -param:TIMESTAMP_INI=\"" + timestampIni +
        		"\" -param:TIMESTAMP_FIN=\"" + timestampFin +        		
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
	      	//UNA VEZ CREADA LA RUTA dirLogs YA SE PUEDE CREAR EL ARCHIVO .sh CON EL COMANDO COMPLETO
	  		try{    			
	  			scriptfile = new FileWriter(dirLogs + "/" + filename + "_command.sh");
	  			pwriter = new PrintWriter(scriptfile);
	  			pwriter.println(commandScript);
	  		} catch (Exception io) {
	  			throw new ExcepcionServicio(io.getMessage());
	  		} finally {
	  			try {
	  				if (null != scriptfile) scriptfile.close();
	  			} catch (Exception io2){
	  				throw new ExcepcionServicio(io2.getMessage());
	  			}			
	  		}
  		
      	//SE EJECUTA EL COMANDO PARA DERLE PERMISO
      	iExitValue = oDefaultExecutor.execute(oCmdLine1);
      	//SE EJECUTA EL COMANDO QUE INICIA LA EJECUCION DEL ARCHIVO 
      	iExitValue = oDefaultExecutor.execute(oCmdLine2);        
                        
        } catch (ExecuteException ex) {
            
        	throw new ExcepcionServicio(ex.getMessage());
        	
        } catch (IOException ie) {
            
        	throw new ExcepcionServicio(ie.getMessage());
        	
        } catch(Exception e) {
        	
        	throw new ExcepcionServicio(e.getMessage());
        }
		
		return iExitValue;
	}
	
	

}
