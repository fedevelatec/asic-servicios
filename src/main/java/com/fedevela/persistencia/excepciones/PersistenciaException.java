package com.fedevela.persistencia.excepciones;

/**
 * Created by fvelazquez on 9/04/14.
 */
public class PersistenciaException extends Exception {

    public PersistenciaException(Throwable cause) {
        super(cause);
    }

    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenciaException(String message) {
        super(message);
    }

    public PersistenciaException() {
    }

}
