/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.gob.mppee.gredsocial.ws;

import java.util.ArrayList;
import java.util.Date;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.ws.BindingType;
import ve.gob.mppee.gredsocial.model.Result;
import ve.gob.mppee.gredsocial.model.Tweet;
import ve.gob.mppee.gredsocial.negocio.TwitterTimerTask;
import ve.gob.mppee.gredsocial.planificador.impl.PlanificadorTimerImpl;

/**
 *
 * @author Liam
 */
@WebService(name="PublicarListaTweetsPlanificados", serviceName = "PublicarListaTweetsPlanificados",
        portName="PublicarListaTweetsPlanificadosPort", targetNamespace = "http://ws.gredsocial.mppee.gob.ve/")
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class PublicarListaTweetsPlanificados {
    
    private final String RESULT_CODE_ERROR_PARAMETER = "1111";
    private final String RESULT_CODE_ERROR_IMPL = "1112";

    /**
     * This is a sample web service operation
     * @param tweets Lista de tweets para ser publicados
     * @param oAuthConsumerKey
     * @param oAuthConsumerSecret
     * @param oAuthAccessToken
     * @param oAuthAccessTokenSecret
     * @param correoNotificacion
     * @return 0 si se planifica con éxito, n en caso contrario.
     */
    @WebMethod(operationName = "PublicarListaTweetsPlanificados")
    public Result PublicarListaTweetsPlanificados(
            @WebParam(name = "listaTweets") ArrayList<Tweet> tweets,
            @WebParam(name="oAuthConsumerKey") String oAuthConsumerKey,
            @WebParam(name="oAuthConsumerSecret") String oAuthConsumerSecret,
            @WebParam(name="oAuthAccessToken") String oAuthAccessToken,
            @WebParam(name="oAuthAccessTokenSecret") String oAuthAccessTokenSecret,
            @WebParam(name="correoNotificacion") String correoNotificacion) {
        
        Result result = new Result();
        
        if (tweets == null) {
            result.setCode(Result.RESULT_CODE_PREFIX_ERROR + RESULT_CODE_ERROR_PARAMETER);
            result.setStatus(Result.RESULT_STATUS_ERROR);
            result.setMessage(
                    result.getMessage() +
                            "Parametro 'listaTweets' NO puede ser null\n");
        }
        if (tweets != null && tweets.isEmpty()) {
            result.setCode(Result.RESULT_CODE_PREFIX_ERROR + RESULT_CODE_ERROR_PARAMETER);
            result.setStatus(Result.RESULT_STATUS_ERROR);
            result.setMessage(
                    result.getMessage() + 
                            "Parametro 'listaTweets' NO puede ser vacio\n");
        }
        if (oAuthConsumerKey == null) {
            result.setCode(Result.RESULT_CODE_PREFIX_ERROR + RESULT_CODE_ERROR_PARAMETER);
            result.setStatus(Result.RESULT_STATUS_ERROR);
            result.setMessage(
                    result.getMessage() +
                            "Parametro 'oAuthConsumerKey' NO puede ser null\n");
        }
        if (oAuthConsumerKey != null && oAuthConsumerKey.isEmpty()) {
            result.setCode(Result.RESULT_CODE_PREFIX_ERROR + RESULT_CODE_ERROR_PARAMETER);
            result.setStatus(Result.RESULT_STATUS_ERROR);
            result.setMessage(
                    result.getMessage() + 
                            "Parametro 'oAuthConsumerKey' NO puede ser vacio\n");
        }
        if (oAuthConsumerSecret == null) {
            result.setCode(Result.RESULT_CODE_PREFIX_ERROR + RESULT_CODE_ERROR_PARAMETER);
            result.setStatus(Result.RESULT_STATUS_ERROR);
            result.setMessage(
                    result.getMessage() +
                            "Parametro 'oAuthConsumerSecret' NO puede ser null\n");
        }
        if (oAuthConsumerSecret != null && oAuthConsumerSecret.isEmpty()) {
            result.setCode(Result.RESULT_CODE_PREFIX_ERROR + RESULT_CODE_ERROR_PARAMETER);
            result.setStatus(Result.RESULT_STATUS_ERROR);
            result.setMessage(
                    result.getMessage() + 
                            "Parametro 'oAuthConsumerSecret' NO puede ser vacio\n");
        }
        if (oAuthAccessToken == null) {
            result.setCode(Result.RESULT_CODE_PREFIX_ERROR + RESULT_CODE_ERROR_PARAMETER);
            result.setStatus(Result.RESULT_STATUS_ERROR);
            result.setMessage(
                    result.getMessage() +
                            "Parametro 'oAuthAccessToken' NO puede ser null\n");
        }
        if (oAuthAccessToken != null && oAuthAccessToken.isEmpty()) {
            result.setCode(Result.RESULT_CODE_PREFIX_ERROR + RESULT_CODE_ERROR_PARAMETER);
            result.setStatus(Result.RESULT_STATUS_ERROR);
            result.setMessage(
                    result.getMessage() + 
                            "Parametro 'oAuthAccessToken' NO puede ser vacio\n");
        }
        if (oAuthAccessTokenSecret == null) {
            result.setCode(Result.RESULT_CODE_PREFIX_ERROR + RESULT_CODE_ERROR_PARAMETER);
            result.setStatus(Result.RESULT_STATUS_ERROR);
            result.setMessage(
                    result.getMessage() +
                            "Parametro 'oAuthAccessTokenSecret' NO puede ser null\n");
        }
        if (oAuthAccessTokenSecret != null && oAuthAccessTokenSecret.isEmpty()) {
            result.setCode(Result.RESULT_CODE_PREFIX_ERROR + RESULT_CODE_ERROR_PARAMETER);
            result.setStatus(Result.RESULT_STATUS_ERROR);
            result.setMessage(
                    result.getMessage() + 
                            "Parametro 'oAuthAccessTokenSecret' NO puede ser vacio\n");
        }
        if (correoNotificacion == null) {
            result.setCode(Result.RESULT_CODE_PREFIX_ERROR + RESULT_CODE_ERROR_PARAMETER);
            result.setStatus(Result.RESULT_STATUS_ERROR);
            result.setMessage(
                    result.getMessage() +
                            "Parametro 'correoNotificacion' NO puede ser null\n");
        }
        if (correoNotificacion != null && correoNotificacion.isEmpty()) {
            result.setCode(Result.RESULT_CODE_PREFIX_ERROR + RESULT_CODE_ERROR_PARAMETER);
            result.setStatus(Result.RESULT_STATUS_ERROR);
            result.setMessage(
                    result.getMessage() + 
                            "Parametro 'correoNotificacion' NO puede ser vacio\n");
        }
        if (null != result.getStatus()) {
            result.setMessage("ERROR: " + result.getMessage());
            return result;
        }
        
        for (Tweet t: tweets) {
            for (Date fechaPublicacion: t.getFechasPublicacion()) {
                TwitterTimerTask tarea = new TwitterTimerTask(
                        t.getText(), fechaPublicacion,oAuthConsumerKey,oAuthConsumerSecret,oAuthAccessToken,oAuthAccessTokenSecret, correoNotificacion);
                PlanificadorTimerImpl planificador = new PlanificadorTimerImpl();
                planificador.crearTarea(tarea, fechaPublicacion);
            }
        }
        result.setCode(Result.RESULT_CODE_SUCCESFULLY);
        result.setStatus(Result.RESULT_STATUS_SUCCESFULLY);
        result.setMessage(
                "SE HA PLANIFICADO TODO CON EXITO, "
                        + "VERIFIQUE SU CORREO SEGÚN LAS FECHAS PLANIFICADAS "
                        + "PARA VALIDAR SI HA OCURRIDO ALGÚN ERROR "
                        + "DURANTE LAS PUBLICACIONES.");
        
        return result;
    }
}
