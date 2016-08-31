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

import java.io.File;

public class BAMInterceptorLoginApp extends AbstractInterceptingMessageProcessor implements Interceptor

{
    private static Log log = LogFactory.getLog(BAMInterceptorLoginApp.class);
    private AsyncDataPublisher asyncDataPublisher;
    
    
    //VARIABLES GLOBALES A ADAPTAR SEGUN EL SERVICIO PARA EL CUAL ESTA HECHA ESTA CLASE
    private static final int ID_SERVICIO = 6;
    private static final String SERVICIO = "LoginApp";
    private static final int QUANTITY = 1;

    public BAMInterceptorLoginApp() {
        setCarbonTrustoreProperties();
        initDataPublisher();
    }

    private void initDataPublisher() {
        AgentConfiguration agentConfiguration = new AgentConfiguration();
        Agent agent = new Agent(agentConfiguration);
        this.asyncDataPublisher = new AsyncDataPublisher("tcp://10.8.0.29:7611", "admin", "admin", agent);
        String streamDefinition = "{  "
        		+ "'name':'invocacion_ws',  "
        		+ "'version':'1.0.0',  "
        		+ "'nickName': 'Invocacion WS',  "
        		+ "'description': 'Registra la invocacion de los servicios web a traves del mule.',  "
        		+ "'metaData':[          "
        		+ "{'name':'publisherIP','type':'STRING'}  ],  "
        		+ "'payloadData':[          "
        		+ "{'name':'id','type':'INT'},         "
        		+ "{'name':'servicio','type':'STRING'},         "
        		+ "{'name':'quantity','type':'INT'}]}";
        this.asyncDataPublisher.addStreamDefinition(streamDefinition, "invocacion_ws", "1.0.0");
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

        /*Book book = (Book) payloadObjects[0];
        String author = book.getAuthor();
        String title = book.getTitle();
        long id = book.getId();
        double price = book.getPrice();
        int quantity = ((Integer) payloadObjects[1]).intValue();
        String address = payloadObjects[2].toString();
        String email = payloadObjects[3].toString();*/
    	
        publishEventsToBAM(null, new Object[]{"10.8.0.29"}, new Object[]{Integer.valueOf(ID_SERVICIO), SERVICIO, Integer.valueOf(QUANTITY)});

    }

    private void publishEventsToBAM(Object[] correlationData, Object[] metaData, Object[] payloadData) {
        Event event = new Event();
        event.setCorrelationData(correlationData);
        event.setMetaData(metaData);
        event.setPayloadData(payloadData);
        try {
            this.asyncDataPublisher.publish("invocacion_ws", "1.0.0", event);
        } catch (AgentException e) {
            log.error("Failed to send events to BAM", e);
        }
    }

    private void setCarbonTrustoreProperties() {
        String muleHome = System.getProperty("mule.home");
        //System.setProperty("javax.net.ssl.trustStore", muleHome + File.separator + "resources" + File.separator + "client-truststore.jks");
        System.setProperty("javax.net.ssl.trustStore", muleHome 
        		+ File.separator + "conf" 
        		+ File.separator + "security"
        		+ File.separator + "bam"
        		+ File.separator + "client-truststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
    }

}