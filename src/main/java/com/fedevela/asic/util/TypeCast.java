package com.fedevela.asic.util;

/**
 * Created by fvelazquez on 9/04/14.
 */
public class TypeCast extends net.codicentro.core.TypeCast {

    public static String SF(Object v) {
        return CF("S", "0", 8, v);
    }

    public static String UF(Object v) {
        return CF("U", "0", 10, v);
    }

    public static String TF(Object v) {
        return CF("T", "0", 11, v);
    }
}
