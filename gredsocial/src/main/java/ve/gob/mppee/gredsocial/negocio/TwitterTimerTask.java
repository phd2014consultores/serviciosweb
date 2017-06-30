/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.gob.mppee.gredsocial.negocio;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import ve.gob.mppee.gredsocial.model.Tweet;

import twitter4j.conf.ConfigurationBuilder;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import ve.gob.mppee.gredsocial.correo.interfaz.Correo;
import ve.gob.mppee.gredsocial.model.CorreoConfig;
import ve.gob.mppee.gredsocial.model.Result;

/**
 *
 * @author Liam
 */
public class TwitterTimerTask extends TimerTask {
    
    private final String RESULT_SUCCESFULLY_FORMATED_MESSAGE =
            "Se ha publicado el siguiente tweet: '%s' para la fecha '%s'";
    private final String RESULT_ERROR_FORMATED_MESSAGE =
            "Ha ocurrido un error publicando el tweet: '%s' para la fecha '%s'.\n%s";
    
    private Twitter twitter;
    private Tweet tweet;
    private String correoNotify;

    public TwitterTimerTask(String text, Date fechaPublicacion, String oAuthConsumerKey, String oAuthConsumerSecret, String oAuthAccessToken, String oAuthAccessTokenSecret, String correoNotify) {
        this.tweet = new Tweet();
        this.tweet.setText(text);
        ArrayList<Date> fechasPublicacion = new ArrayList<>();
        fechasPublicacion.add(fechaPublicacion);
        this.tweet.setFechasPublicacion(fechasPublicacion);
        
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey(oAuthConsumerKey)
            .setOAuthConsumerSecret(oAuthConsumerSecret)
            .setOAuthAccessToken(oAuthAccessToken)
            .setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
        twitter = new TwitterFactory(cb.build()).getInstance();
        
        this.correoNotify = correoNotify;
    }

    
    public Result publicarTweet () {
        Result result = new Result();
        try {
            twitter.updateStatus(this.tweet.getText());
            result.setCode(Result.RESULT_CODE_SUCCESFULLY);
            result.setStatus(Result.RESULT_STATUS_SUCCESFULLY);
            result.setMessage(
                    this.RESULT_SUCCESFULLY_FORMATED_MESSAGE,
                    this.tweet.getText(),
                    this.tweet.getFechasPublicacion().get(0).toString());
            Logger.getLogger(TwitterTimerTask.class.getName()).log(Level.INFO, result.getMessage());
        } catch (TwitterException ex) {
            Logger.getLogger(TwitterTimerTask.class.getName()).log(Level.SEVERE, null, ex);
            result.setCode(Result.RESULT_CODE_PREFIX_ERROR + ex.getErrorCode());
            result.setStatus(Result.RESULT_STATUS_ERROR);
            result.setMessage(
                    this.RESULT_ERROR_FORMATED_MESSAGE,
                    this.tweet.getText(),
                    this.tweet.getFechasPublicacion().get(0).toString(),
                    ex.getErrorMessage());
            Logger.getLogger(TwitterTimerTask.class.getName()).log(Level.SEVERE, result.getMessage());
        }
        
        return result;
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

    public String getCorreoNotify() {
        return correoNotify;
    }

    public void setCorreoNotify(String correoNotify) {
        this.correoNotify = correoNotify;
    }
    
    @Override
    public void run() {
        Result result = publicarTweet();
        if (result.getStatus().equals(Result.RESULT_STATUS_ERROR)) {
            Correo correo = new CorreoJavaMailImpl();
            correo.enviar(new CorreoConfig(),
                    this.correoNotify, "ERROR PUBLICACION TWEET",
                    "Código de error: " + result.getCode() + "\n"
                            + "Mensaje de error: " + result.getMessage(), null);
        }
    }
    
}
