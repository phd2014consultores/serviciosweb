package com.phdconsultores.mule.interceptor.ws;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.interceptor.Interceptor;
import org.mule.processor.AbstractInterceptingMessageProcessor;
import org.wso2.carbon.databridge.agent.thrift.Agent;
import org.wso2.carbon.databridge.agent.thrift.AsyncDataPublisher;
import org.wso2.carbon.databridge.agent.thrift.conf.AgentConfiguration;
import org.wso2.carbon.databridge.agent.thrift.exception.AgentException;
import org.wso2.carbon.databridge.commons.Event;

import com.phdconsultores.mule.interceptor.param.ConexionParamBam;

import java.util.Properties;

public class BAMInterceptorFuncionApp extends AbstractInterceptingMessageProcessor implements Interceptor{
    private static Log log = LogFactory.getLog(BAMInterceptorFuncionApp.class);
    private AsyncDataPublisher asyncDataPublisher;
    
    private ConexionParamBam cpb;
    private Properties props;
    
    
    //VARIABLES GLOBALES A ADAPTAR SEGUN EL SERVICIO PARA EL CUAL ESTA HECHA ESTA CLASE
    private static final int ID_SERVICIO = 5;
    private static final String SERVICIO = "FuncionApp";
    private static final int QUANTITY = 1;

    public BAMInterceptorFuncionApp() throws Exception {
    	cpb = new ConexionParamBam();
    	props = cpb.cargaPropiedadesBD();
        if (props != null) {
        	System.out.println("Se inicializaron los parametros para conectarse al BAM............!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			setCarbonTrustoreProperties();
			initDataPublisher();
		}else{
			System.out.println("Error al inicilizar los parametros para conectarse al BAM............!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}        
    }

    private void initDataPublisher() {
        AgentConfiguration agentConfiguration = new AgentConfiguration();
        Agent agent = new Agent(agentConfiguration);
        //this.asyncDataPublisher = new AsyncDataPublisher("tcp://10.8.0.29:7611", "admin", "admin", agent);
        this.asyncDataPublisher = new AsyncDataPublisher("tcp://" 
        + props.getProperty("bam.cassandra.host")
        + ":"
        + props.getProperty("bam.cassandra.puerto"), props.getProperty("bam.cassandra.usuario"), props.getProperty("bam.cassandra.usuario.pass"), agent);
        
        /*String streamDefinition = "{  "
        		+ "'name':'invocacion_ws',  "
        		+ "'version':'1.0.0',  "
        		+ "'nickName': 'Invocacion WS',  "
        		+ "'description': 'Registra la invocacion de los servicios web a traves del mule.',  "
        		+ "'metaData':[          "
        		+ "{'name':'publisherIP','type':'STRING'}  ],  "
        		+ "'payloadData':[          "
        		+ "{'name':'id','type':'INT'},         "
        		+ "{'name':'servicio','type':'STRING'},         "
        		+ "{'name':'quantity','type':'INT'}]}";*/
        
        String streamDefinition = "{  "
        		+ "'name':'" + props.getProperty("bam.cassandra.keyspace.name") + "',  "
        		+ "'version':'1.0.0',  "
        		+ "'nickName': '" + props.getProperty("bam.cassandra.keyspace.nickname") + "',  "
        		+ "'description': 'Registra la invocacion de los servicios web a traves del mule.',  "
        		+ "'metaData':[          "
        		+ "{'name':'publisherIP','type':'STRING'}  ],  "
        		+ "'payloadData':[          "
        		+ "{'name':'id','type':'INT'},         "
        		+ "{'name':'servicio','type':'STRING'},         "
        		+ "{'name':'quantity','type':'INT'}]}";        
        
        this.asyncDataPublisher.addStreamDefinition(streamDefinition, props.getProperty("bam.cassandra.keyspace.name"), "1.0.0");
    }

    public MuleEvent process(MuleEvent muleEvent) throws MuleException {
        System.out.println("Mensaje recibido por el BAM Interceptor............!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        //Object[] payloadObjects = (Object[]) muleEvent.getMessage().getPayload();
        //captureDataFromPayload(payloadObjects);
        captureDataFromPayload(null);
        MuleEvent resultEvent = processNext(muleEvent);

        return resultEvent;
    }

    private void captureDataFromPayload(Object[] payloadObjects) {
    	
        //publishEventsToBAM(null, new Object[]{"10.8.0.29"}, new Object[]{Integer.valueOf(ID_SERVICIO), SERVICIO, Integer.valueOf(QUANTITY)});
    	publishEventsToBAM(null, new Object[]{props.getProperty("bam.cassandra.host")}, new Object[]{Integer.valueOf(ID_SERVICIO), SERVICIO, Integer.valueOf(QUANTITY)});

    }

    private void publishEventsToBAM(Object[] correlationData, Object[] metaData, Object[] payloadData) {
        Event event = new Event();
        event.setCorrelationData(correlationData);
        event.setMetaData(metaData);
        event.setPayloadData(payloadData);
        try {
            this.asyncDataPublisher.publish(props.getProperty("bam.cassandra.keyspace.name"), "1.0.0", event);
        } catch (AgentException e) {
            log.error("Error al enviar evento al BAM", e);
        }
    }

    private void setCarbonTrustoreProperties() {
        String muleHome = System.getProperty("mule.home");        
        System.setProperty("javax.net.ssl.trustStore", props.getProperty("bam.trustStore").replaceAll("\\$\\{mule.home\\}", muleHome) );
        System.setProperty("javax.net.ssl.trustStorePassword", props.getProperty("bam.trustStorePassword"));
    }

}