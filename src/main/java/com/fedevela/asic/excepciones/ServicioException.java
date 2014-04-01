package com.fedevela.asic.excepciones;

/**
 * Created by fvelazquez on 31/03/14.
 */
import java.util.Map;

/**
 * Expcion general utilizado en caso de que un servicio falle.
 *
 */
public class ServicioException extends Exception {

    public enum TCodeException {

        NONE,
        CAPTURA,
        CONFIRMACION
    };
    private Map<String, Object> result;
    private TCodeException code = TCodeException.NONE;

    public ServicioException(Throwable cause) {
        super(cause);
    }

    public ServicioException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServicioException(String message) {
        super(message);
    }

    public ServicioException(String string, TCodeException code) {
        super(string);
        this.code = code;
    }

    public ServicioException(String message, Map<String, Object> result) {
        super(message);
        this.result = result;
    }

    public ServicioException() {
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public Object getResult(String key) {
        return result.get(key);
    }

    public TCodeException getCode() {
        return code;
    }

    public void setCode(TCodeException code) {
        this.code = code;
    }
}
