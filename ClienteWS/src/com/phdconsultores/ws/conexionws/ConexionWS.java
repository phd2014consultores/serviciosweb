package com.phdconsultores.ws.conexionws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

/**
 * Clase que implementa metodos para consumir Web Services
 * desplegados en un proxy del MULE-ESB, con el manejo
 * inteligente del timeout para servicios que tardan mucho
 * tiempo para retornar respuesta
 * 
 * @author Simon Cedeno
 * @version Versión inicial
 * 
 * 17/07/2016
 *
 */

public class ConexionWS {
	
	private String rutaPropiedades;			//Ruta al archivo donde estan configuradas las propiedades	
		
	public ConexionWS(){		
		rutaPropiedades = "../lib/wsc.properties";
		//rutaPropiedades = "/home/simonts/Apps/apache-tomcat-7.0.47_mercal/lib/wsc.properties";
		
	}
	
	/**
	 * Método que crea el socket de conexion segura y realiza el request al servicio
	 *  
	 * @param keystore Ruta al keystore
	 * @param keystorepassword Password del keystore
	 * @param https_url Url para acceder al WS en el mule, no debe terminar en ?wsdl
	 * @param timeout Numero de milisegundos qu puede esperar por respuesta
	 * @param sobre SOAPMessage para realizar el request
	 * 
	 * @return SOAPMessage Sobre con la respuesta del ws
	 * @throws Exception
	 */
	
	public SOAPMessage requestSoapSeguro(String keystore, String keystorepassword, String https_url, int timeout, SOAPMessage sobre) throws Exception{
		
		SOAPMessage response = null;
		
		//REDEFINIENDO UN TRUSTMANAGER PARA QUE NO USE EL QUE TIENE JAVA POR DEFECTO
		TrustManager[] trustManagers = new TrustManager[] {
				new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() {
						return new X509Certificate[0];
					}
					public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
						//NO HACE NADA
					}
					public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
						//NO HACE NADA
					}
                }
         };			        
			
        try {
        	//DEFINIENDO EL SOCKET DE CONEXION Y EL KEYSTORE  
	        SSLContext sc = SSLContext.getInstance("SSLv3");
	        
	        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	        KeyStore kstore = KeyStore.getInstance("JKS");
	        kstore.load(new FileInputStream(new File(keystore)), keystorepassword.toCharArray());
	        
	        kmf.init(kstore, keystorepassword.toCharArray());            
            sc.init(kmf.getKeyManagers(), trustManagers, null);
            
            //ACTUALIZAMOS LA CONEXION HTTPS CON EL CONTEXTO SSL ANTERIOR
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            
            //CREAMOS LA CONEXION SOAP 
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            
            //SE INICIALIZA LA CONEXION
            URL url = new URL(https_url);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            
            //ESTABLECEMOS EL TIMEOUT
            con.setConnectTimeout(timeout);
            
            //SE REALIZA LA CONEXION
            con.connect();
            
            //SE OBTIENE EL SOBRE CON LA RESPUESTA
            response = soapConnection.call(sobre, url);
            
            //SE CIERRA LA CONEXION
            soapConnection.close();
        } catch (KeyStoreException e) {
        	throw new Exception(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			throw new Exception(e.getMessage());
		} catch (CertificateException e) {
			throw new Exception(e.getMessage());
		} catch (FileNotFoundException e) {
			throw new Exception(e.getMessage());
		} catch (IOException e) {
			//MANEJO DEL TIMEOUT PARA LOS WS RAPIDOS Y LOS WS LENTOS
			if(timeout > 3000){
				throw new Exception(e.getMessage());
			}else{
				return null;
			}
		} catch (UnrecoverableKeyException e) {
			throw new Exception(e.getMessage());
		} catch (KeyManagementException e) {
			throw new Exception(e.getMessage());
		} catch (UnsupportedOperationException e) {
			throw new Exception(e.getMessage());
		} catch (SOAPException e) {
			throw new Exception(e.getMessage());
		} catch (Exception e){
			throw new Exception(e.getMessage());
		}		
		
		return response;
	}
	
	/**
	 * Método que retorna la respuesta del response como un String manejable para el cliente 
	 *  
	 * @param soapResponse Sobre obtenido en el request
	 * @param patron Patron de expresion regular que debe cumplir el String obtenido para que sea valido
	 *  
	 * @return String ya adaptado para que el cliente lo pueda manipular
	 * @throws Exception
	 */
	
	public String respuestaProcesada(SOAPMessage soapResponse, String patron) throws Exception{
		
		String r = "null";
		SOAPBody soapBody = null;
		Node soapBodyElem = null;
		if(soapResponse != null){
			try{
				//OBTENGO EL BODY
				soapBody = soapResponse.getSOAPBody();			
				if(soapBody != null){
					//bUSCAMOS EL RETURN
					soapBodyElem = (Node) soapBody.getFirstChild();
							//.getFirstChild();
					while(soapBodyElem.getFirstChild() != null){					
						if((soapBodyElem.getLocalName()).equals("return")){
							//SE OBTIENE EL RESULTADO COMO UN STRING
							r = soapBodyElem.getTextContent();
							break;
						}
						soapBodyElem = (Node) soapBodyElem.getFirstChild();
					}				
				}					
			} catch (Exception e){
				throw new Exception(e.getMessage());
			}
			//VERIFICA EL PATRON A DEVOLVER
			if(Pattern.matches(patron, r)){
				return r;
			}		
			//return null;
		}
		return null;
	}
	
	
	
	
	/**
	 * Método que crea el sobre para hacer el request al servicio
	 *  
	 * @param prefijo del servicio, por lo general usaremos ws
	 * @param namespace del servicio
	 * @param operacion
	 * @param Map<String, String> map <atributo, valor>. El map debe ser LinkedHashMap<>()
	 * 
	 * @return SOAPMessage sobre con los atributos y valores respectivos, para realizar el request
	 * @throws Exception 
	 * @throws Exception
	 */
	
	public SOAPMessage crearSobre(String prefijo, String namespace, String operacion, Map<String, String> map ) throws Exception{
		
		SOAPMessage soapMessage = null;
		
		try{
		MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		
		soapMessage = messageFactory.createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		
		//LE PASAMOS EL PREFIJO DEL SOBRE Y EL NAMESPACE
		envelope.addNamespaceDeclaration(prefijo, namespace);
		
		//OBTENEMOS EL BODY PARA AGREGAR LOS ATRIBUTOS CON SUS RESPECTIVOS VALORES
		SOAPBody soapBody = envelope.getBody();		
		SOAPElement soapBodyElem = soapBody.addChildElement(operacion, prefijo);
		SOAPElement soapBodyElem1;
		
		//RECORREMOS EL MAP PARA OBTENER LOS ATRIBUTOS CON SUS RESPECTIVOS VALORES PARA AGREGARLOS AL ENVELOPE		
		for(Map.Entry<String, String> e: map.entrySet()){			
			soapBodyElem1 = soapBodyElem.addChildElement(e.getKey());
			soapBodyElem1.addTextNode(e.getValue());			
		}
		
		//AGREGAMOS EL HEADER
		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("SOAPAction", namespace);
		
		soapMessage.saveChanges();
		}catch (Exception e){
			throw new Exception(e.getMessage());
		}
		
		return soapMessage;
	}
	
	
	
	/**
	 * Método que devuelve los parámetros de invocacion para poder invocar el servicio
	 *  
	 * 
	 * @return Properties Objeto con los parametros obtenidos del archivo wsc.properties
	 * @throws Exception
	 */
	
	public Properties cargaPropiedadesWS() throws Exception {
		 Properties props = new Properties();
	     FileInputStream in = null;
	      
	     try {
	       	in = new FileInputStream(rutaPropiedades);
			props.load(in);
				
	     } catch (IOException e1) {
				throw new Exception(e1.getMessage());
				
		} finally {
	            
	        try {
	            if (in != null) {
	                in.close();
	            }
	        } catch (IOException ex) {
	        	throw new Exception(ex.getMessage());
	        }
		}
	    
	    return props;
	}
	
	
	

}
