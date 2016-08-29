package com.phdconsultores.ws;

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


@WebService(targetNamespace = "http://ws.phdconsultores.com/", endpointInterface = "com.phdconsultores.ws.Factorial",
portName = "FactorialPort", serviceName = "Factorial")
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class FactorialImpl implements Factorial {
	
	
	public FactorialImpl(){}
	/**
	 * Método que realiza el calculo del factorial y ejecuta el comando echo para guardar el resultado en un archivo
	 * 
	 * 
	 * @param	num									-numero a calcular el factorial
	 * @param   dir									-directorio donde se guardara el archivo
	 * 
	 * @return	Un entero long   						- Un valor entero (0 = éxito, <>0 Fallo)										-
	 * @throws ExcepcionServicio
	 * 
	 * 
	 */

	public long factorial(
			long num,
			String dir
			) throws ExcepcionServicio {
		// TODO Auto-generated method stub
		long iExitValue = -1;
		
		String commandScript = null;				
		FileWriter scriptfile = null;
        PrintWriter pwriter = null;
        Date fechaHoy = new Date();
        //ESPECIFICO EL NUEVO DIRECTORIO A CREAR
        String newdir = dir + "/" + "FACTORIAL_" + new SimpleDateFormat("yyyy-MM-dd").format(fechaHoy) + "_" + new SimpleDateFormat("HH:mm:ss.SSS").format(fechaHoy);
        //ESPECIFICO EL NOMBRE DEL SCRIPT A CREAR
        String filename = "command_" + new SimpleDateFormat("yyyy-MM-dd").format(fechaHoy) + "_" + new SimpleDateFormat("HH:mm:ss").format(fechaHoy) + ".sh";
        //CALCULO RESULTADO
        //int result = 1;
        long result = 1;
        String resultado = "";		
		if(num >= 0){
			for(long i = num; i > 1; i --) result *= i;
			resultado = Long.toString(result);
		}else{
			resultado = "El factorial solo esta definido para numeros naturales";
		}
		//CREO EL COMANDO DEL SCRIPT
		//commandScript = "echo -e \"$(date +%Y-%m-%d) Resultado:\n$(date +%Y-%m-%d) " + resultado + "\" > " + newdir + "/resultado.txt";
		commandScript = "echo \"$(date +%Y-%m-%d) *El factorial de " + Long.toString(num) + " es: " + resultado + "\" > " + newdir + "/resultado.txt";
		//COMANDO QUE CREA EL NUEVO DIRECTORIO DENTRO DE DIR
		CommandLine oCmdLine0 = CommandLine.parse("mkdir -p " + newdir);
		//SE AGREGA EL COMANDO QUE LE OTORGA PERMISO DE EJECUCION AL ARCHIVO
		CommandLine oCmdLine1 = CommandLine.parse("chmod +x " + newdir + "/" + filename);
		//SE AGREGA EL COMANDO QUE EJECUTA EL ARCHIVO
		CommandLine oCmdLine2 = CommandLine.parse(newdir + "/" + filename);
        DefaultExecutor oDefaultExecutor = new DefaultExecutor();
        oDefaultExecutor.setExitValue(0);
		
        try {
            //SE EJECUTA EL COMANDO PARA CREAR LA CARPETA
        	iExitValue = oDefaultExecutor.execute(oCmdLine0);
        	//UNA VEZ CREADA LALA RUTA dirLogs
    		try{    			
    			scriptfile = new FileWriter(newdir + "/" + filename);
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
        	if (iExitValue != 0) return -1;
        	//SE EJECUTA EL COMANDO QUE INICIA LA EJECUCION DEL ARCHIVO 
        	iExitValue = oDefaultExecutor.execute(oCmdLine2);
        	if (iExitValue != 0) return -1;
        	
        	iExitValue = result;
        
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
