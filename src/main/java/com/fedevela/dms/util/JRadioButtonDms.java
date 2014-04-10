package com.fedevela.dms.util;

/**
 * Created by fvelazquez on 9/04/14.
 */
import javax.accessibility.Accessible;
import javax.swing.JRadioButton;

public class JRadioButtonDms extends JRadioButton implements Accessible {

    private int idxButtonGroup = 0;
    private Short idDocumento = null;

    public JRadioButtonDms() {
        super();
    }

    public JRadioButtonDms(String text) {
        super(text);
    }

    /**
     * @return the idxButtonGroup
     */
    public int getIdxButtonGroup() {
        return idxButtonGroup;
    }

    /**
     * @param idxButtonGroup the idxButtonGroup to set
     */
    public void setIdxButtonGroup(int idxButtonGroup) {
        this.idxButtonGroup = idxButtonGroup;
    }

    /**
     * @return the idDocumento
     */
    public Short getIdDocumento() {
        return idDocumento;
    }

    /**
     * @param idDocumento the idDocumento to set
     */
    public void setIdDocumento(Short idDocumento) {
        this.idDocumento = idDocumento;
    }
}
