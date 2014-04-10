package com.fedevela.dms.util;

/**
 * Created by fvelazquez on 9/04/14.
 */
import java.util.Enumeration;
import javax.swing.DefaultListModel;

public class DefaultListModelDms extends DefaultListModel {

    public boolean containsE(Object elem) {
        Enumeration<?> e = elements();
        boolean rs = false;
        while (e.hasMoreElements() && !rs) {
            rs = e.nextElement().toString().equals(elem);
        }
        return rs;
    }
}
