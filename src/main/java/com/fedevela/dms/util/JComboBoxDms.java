package com.fedevela.dms.util;

/**
 * Created by fvelazquez on 9/04/14.
 */
import java.awt.ItemSelectable;
import java.awt.event.ActionListener;
import java.util.List;
import javax.accessibility.Accessible;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataListener;

public class JComboBoxDms extends JComboBox implements ItemSelectable, ListDataListener, ActionListener, Accessible {
    private List<ComboBoxModelDms> model =null;
    public void setModel(List<ComboBoxModelDms> model) {
        super.setModel(new DefaultComboBoxModel(model.toArray()));
        this.model=model;
    }

    public Object getValue() {
        if (getSelectedItem() == null) {
            return null;
        } else {
            return ((ComboBoxModelDms) getSelectedItem()).getValue();
        }
    }

    public void setSelectedItemByValue(Object value) {
        Object selectedObjects = null;
        int i = 0;
        while ((i < model.size()) && (selectedObjects == null)) {
            if (model.get(i).getValue().equals(value)) {
                selectedObjects = model.get(i);
            }
            i++;
        }
        if (selectedObjects != null) {
            setSelectedItem(selectedObjects);
        }
    }
}
