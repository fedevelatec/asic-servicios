package com.fedevela.dms.util;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.asic.util.TypeCast;
import javax.swing.JFormattedTextField;

public class JFormattedTextFieldDms extends JFormattedTextField {

    public enum TType {

        TEXT, DATE, DECIMAL, INTEGER
    };
    private TType tType = TType.TEXT;

    /**
     * @return the tType
     */
    public TType gettType() {
        return tType;
    }

    /**
     * @param tType the tType to set
     */
    public void settType(TType tType) {
        this.tType = tType;
    }

    public Object getVal() {
        if (getValue() instanceof java.util.Date) {
            return TypeCast.toString((java.util.Date) getValue(), "dd/MM/yyyy");
        } else {
            return TypeCast.toString(getValue());
        }
    }
}
