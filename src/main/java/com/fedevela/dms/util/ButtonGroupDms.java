package com.fedevela.dms.util;

/**
 * Created by fvelazquez on 9/04/14.
 */
import java.io.Serializable;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

public class ButtonGroupDms extends ButtonGroup implements Serializable {

    private Object key = null;

    /**
     * @return the key
     */
    public Object getKey() {
        key = null;
        Enumeration elements = getElements();
        while ((elements.hasMoreElements()) && (key == null)) {
            AbstractButton button = (AbstractButton) elements.nextElement();
            if (button.isSelected()) {
                key = button.getText();
            }
        }
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(Object key) {
        this.key = null;
        Enumeration elements = getElements();
        while ((elements.hasMoreElements()) && (this.key == null)) {
            AbstractButton button = (AbstractButton) elements.nextElement();
            if (button.getText().equals(key)) {
                button.setSelected(true);
                this.key = key;
            }
        }
    }
}
