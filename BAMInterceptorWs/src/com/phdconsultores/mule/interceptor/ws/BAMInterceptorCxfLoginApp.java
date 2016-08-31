package com.phdconsultores.mule.interceptor.ws;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.interceptor.Interceptor;

/*import org.mule.example.bookstore.Book;

import org.apache.log4j.spi.LoggingEvent;
import org.wso2.carbon.databridge.agent.thrift.DataPublisher;*/

import org.mule.processor.AbstractInterceptingMessageProcessor;
import org.wso2.carbon.databridge.agent.thrift.Agent;
import org.wso2.carbon.databridge.agent.thrift.AsyncDataPublisher;
import org.wso2.carbon.databridge.agent.thrift.conf.AgentConfiguration;
import org.wso2.carbon.databridge.agent.thrift.exception.AgentException;
import org.wso2.carbon.databridge.commons.Event;

import java.io.File;

public class BAMInterceptorCxfLoginApp extends AbstractInterceptingMessageProcessor implements Interceptor

{
    /*private static final String STREAM_NAME = "ws_mule_details";
    private static final String VERSION = "1.0.0";*/
    private static Log log = LogFactory.getLog(BAMInterceptorCxfLoginApp.class);
    private AsyncDataPublisher asyncDataPublisher;


    public BAMInterceptorCxfLoginApp() {
        setCarbonTrustoreProperties();
        initDataPublisher();
    }

    private void initDataPublisher() {
        AgentConfiguration agentConfiguration = new AgentConfiguration();
        Agent agent = new Agent(agentConfiguration);
        this.asyncDataPublisher = new AsyncDataPublisher("tcp://10.8.0.29:7611", "admin", "admin", agent);
        String streamDefinition = "{  "
        		+ "'name':'ws_mule_details',  "
        		+ "'version':'1.0.0',  "
        		+ "'nickName': 'Ws Mule details',  "
        		+ "'description': 'Stream for sending captured events when ordering books.',  "
        		+ "'metaData':[          "
        		+ "{'name':'publisherIP','type':'STRING'}  ],  "
        		+ "'payloadData':[          "
        		+ "{'name':'id','type':'LONG'},         "
        		+ "{'name':'title','type':'STRING'},         "
        		+ "{'name':'author','type':'STRING'},          "
        		+ "{'name':'price','type':'DOUBLE'},          "
        		+ "{'name':'quantity','type':'INT'},          "
        		+ "{'name':'address','type':'STRING'},          "
        		+ "{'name':'email','type':'STRING'}  ]}";
        this.asyncDataPublisher.addStreamDefinition(streamDefinition, "ws_mule_details", "1.0.0");
    }

    public MuleEvent process(MuleEvent muleEvent) throws MuleException {
        System.out.println("MESSAGE RECEIVED TO BAM INTERCEPTOR............!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Object[] payloadObjects = (Object[]) muleEvent.getMessage().getPayload();
        captureDataFromPayload(payloadObjects);
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
    	
    	String author = "Autor: prueba";
        String title = "Titulo: prueba";
    	
        long id = 1L;
        double price = 4.0;
        int quantity = 4;
        String address = "Caracas";
        String email = "phd2014@phd2014.com";
        publishEventsToBAM(null, new Object[]{"10.8.0.29"}, new Object[]{Long.valueOf(id), title, author, Double.valueOf(price), Integer.valueOf(quantity), address, email});

    }

    private void publishEventsToBAM(Object[] correlationData, Object[] metaData, Object[] payloadData) {
        Event event = new Event();
        event.setCorrelationData(correlationData);
        event.setMetaData(metaData);
        event.setPayloadData(payloadData);
        try {
            this.asyncDataPublisher.publish("ws_mule_details", "1.0.0", event);
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
    
    protected void setCarbonTrustoreProperties_wso2() {
        //System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\phd2014\\Documents\\wso2bam-2.4.1\\repository\\resources\\security\\client-truststore.jks");
	   	System.setProperty("javax.net.ssl.trustStore", "/home/bam/mule-standalone-3.5.0/conf/security/bam/client-truststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
    }

}