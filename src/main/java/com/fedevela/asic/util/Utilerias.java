package com.fedevela.asic.util;

/**
 * Created by fvelazquez on 9/04/14.
 */
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utilerias {

    public static String conocerIp() {
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Utilerias.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ip;
    }

    public static String nombrePC() {

        String maquina = null;

        try {
            InetAddress address = InetAddress.getLocalHost();
            maquina = address.getHostName();

            if (maquina == null || maquina.length() < 1) {
                maquina = "No encontrada";
            }
        } catch (Exception e) {
            maquina = "No encontrada";
        }

        return maquina;
    }
}
