package com.phdconsultores.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.phdconsultores.ws.exception.ExcepcionServicio;


@WebService(name = "Factorial", targetNamespace = "http://ws.phdconsultores.com/")
public interface Factorial {
	
	/**
	 * Método que realiza el calculo del factorial y ejecuta el comando echo para guardar el resultado en un archivo
	 * 
	 * 
	 * @param	num									-numero a calcular el factorial
	 * @param   dir									-directorio donde se guardara el archivo
	 * 
	 * @return	Un entero long   						- Un valor entero (0 = éxito, <>0 Fallo)
	 * @throws ExcepcionServicio
	 * 
	 * 
	 */
	
	@WebMethod(operationName = "factorial", action = "urn:Factorial")
	public long factorial(
			@WebParam(name = "num" ) long num,
			@WebParam(name = "dir" ) String dir
						
			) throws ExcepcionServicio;
	

}
