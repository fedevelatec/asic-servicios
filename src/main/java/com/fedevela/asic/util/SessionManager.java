package com.fedevela.asic.util;

/**
 * Created by fvelazquez on 9/04/14.
 */
import java.util.HashMap;

public abstract class SessionManager {
    private static HashMap<String, Object> session;

    public static HashMap<String, Object> getSession() {
        if (session == null) {
            session = new HashMap<String, Object>();
        }
        return session;
    }
}
