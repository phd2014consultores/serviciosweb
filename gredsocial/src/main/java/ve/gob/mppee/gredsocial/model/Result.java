/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.gob.mppee.gredsocial.model;

/**
 *
 * @author Liam
 */
public class Result {
    
    public static final String RESULT_CODE_SUCCESFULLY = "000";
    public static final String RESULT_CODE_PREFIX_ERROR = "10";
    public static final String RESULT_STATUS_ERROR = "ERROR";
    public static final String RESULT_STATUS_SUCCESFULLY = "SATISFACTORIO";

    
    private String code;
    private String message;
    private String status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void setMessage(String messageFormat, String... params) {
        this.setMessage(String.format(messageFormat, params));
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }    
}
