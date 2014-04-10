package com.fedevela.dms.util;

/**
 * Created by fvelazquez on 9/04/14.
 */
public class ComboBoxModelDms {

    private Object value = null;
    private String display = null;

    @Override
    public String toString() {
        return display;
    }

    public ComboBoxModelDms() {
    }

    public ComboBoxModelDms(String display) {
        this.value = display;
        this.display = display;
    }

    public ComboBoxModelDms(Object value, String display) {
        this.value = value;
        this.display = display;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * @return the display
     */
    public String getDisplay() {
        return display;
    }

    /**
     * @param display the display to set
     */
    public void setDisplay(String display) {
        this.display = display;
    }
}
