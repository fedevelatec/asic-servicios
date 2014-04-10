package com.fedevela.dms.util;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.asic.util.TypeCast;
import javax.swing.JTextField;

public class JTextFieldDms extends JTextField {

    public JTextFieldDms() {
        super();
    }

    public JTextFieldDms(String text) {
        super(text);
    }

    /**
     * @return the type
     */
    public TType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(TType type) {
        this.type = type;
    }

    public enum TType {

        TEXT,
        NUMERIC,
        DATE
    };
    private Object value = null;
    private TType type = TType.TEXT;

    /**
     * @return the value
     */
    public Object getValue() {
        value = (TypeCast.isBlank(getText())) ? null : getText();
        setValue();
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
        setText(TypeCast.toString(value));
        setValue();
    }

    private void setValue() {
        switch (type) {
            case NUMERIC:
                value = TypeCast.toBigDecimal(value);
                break;
            default:
                value = TypeCast.toString(value);
                break;
        }
    }
}
