package com.fedevela.dms.util;

/**
 * Created by fvelazquez on 9/04/14.
 */
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.JDialog;

import javax.swing.JLabel;
import javax.swing.WindowConstants;
import org.apache.commons.lang.ObjectUtils;

public class REnter extends JDialog {

    private JPanel panel = null;
    private JTextFieldDms newValue = null;
    private JTextFieldDms oldValue = null;
    private boolean valid = false;

    private void makeJTextFieldDms() {
        newValue = new JTextFieldDms();
        newValue.setColumns(15);
        newValue.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent evt) {
                evtKey(evt);
            }
        });
        newValue.requestFocus();
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    private void evtKey(KeyEvent evt) {
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            valid = false;
            oldValue.requestFocus();
            setVisible(false);
            return;
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (ObjectUtils.equals(newValue.getValue(), oldValue.getValue())) {
                valid = true;
                setVisible(false);
                return;
            }
        }
        if (ObjectUtils.equals(newValue.getValue(), oldValue.getValue())) {
            ((Component) evt.getSource()).setBackground(new Color(153, 255, 153));
        } else {
            ((Component) evt.getSource()).setBackground(new Color(255, 204, 204));
        }
    }

    public REnter(JDialog owner, JTextFieldDms oldValue, String d) {
        super(owner, "Retipeo de valores", true);

        this.oldValue = oldValue;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        /*   JTextComponent.KeyBinding[] newBindings = {
        new JTextComponent.KeyBinding(
        KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK),
        DefaultEditorKit.beepAction),
        new JTextComponent.KeyBinding(
        KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK),
        DefaultEditorKit.beepAction),
        new JTextComponent.KeyBinding(
        KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK),
        DefaultEditorKit.beepAction)
        };*/
        setLayout(new BorderLayout());
        panel = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.setLayout(new GridBagLayout());
        panel.add(new JLabel("Recaptura y presiona ENTER para validar o ESC para regresar"), gbc);
        panel.add(new JLabel(d));
        makeJTextFieldDms();
        panel.add(newValue);
        add(panel);
        setSize(500, 100);
        Dimension dPantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dFormulario = getSize();
        setLocation(new Point((dPantalla.width - dFormulario.width) / 2,
                ((dPantalla.height - dFormulario.height) / 2) - 50));
        // setSize(500, 100);
        setResizable(false);
        oldValue.setVisible(false);
        setVisible(true);
        oldValue.setVisible(true);
    }
}
