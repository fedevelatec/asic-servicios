package com.fedevela.asic.util;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.asic.formularios.FrmAutorizaciones;
import com.fedevela.asic.formularios.FrmRecaptura;
import com.fedevela.asic.formularios.FrmRecapturaCampoLargo;
import com.fedevela.asic.formularios.FrmRecapturaEx;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Validaciones {

    /**
     * @param c
     * @param l
     */
    public static boolean validaRecaptura(JDialog owner, JTextField tf, String l) {
        tf.setVisible(false);
        FrmRecaptura dialog = new FrmRecaptura(owner, true, l, tf.getText());
        dialog.setVisible(true);
        tf.setVisible(true);
        if (!dialog.isValidated()) {
            tf.setText(null);
            tf.requestFocusInWindow();
            return false;
        } else {
            return true;
        }
    }

    public static boolean validaRecaptura(JDialog owner, JTextField tf, String l, JPanel panel) {
        tf.setVisible(false);
        FrmRecaptura dialog = new FrmRecaptura(owner, true, l, tf.getText(), panel);
        dialog.setVisible(true);
        tf.setVisible(true);
        if (!dialog.isValidated()) {
            // tf.setText("");
            tf.requestFocusInWindow();
        }
        return dialog.isValidated();
    }

    public static boolean validaRecaptura(JDialog owner, JTextField tf, String l, int tipo) {
        tf.setVisible(false);
        FrmRecaptura dialog = new FrmRecaptura(owner, true, l, tf.getText(), tipo);
        dialog.setVisible(true);
        tf.setVisible(true);
        if (!dialog.isValidated()) {
            // tf.setText("");
            tf.requestFocusInWindow();
        }
        return dialog.isValidated();
    }

    public static boolean validaRecapturaCampoLargo(JDialog owner, JTextField tf, String l) {
        tf.setVisible(false);
        FrmRecapturaCampoLargo dialog = new FrmRecapturaCampoLargo(owner, true, l, tf.getText());
        dialog.setVisible(true);
        tf.setVisible(true);
        if (!dialog.isValidated()) {
            // tf.setText("");
            tf.requestFocusInWindow();
        }
        return dialog.isValidated();
    }

    /**
     * @param c
     * @param l
     */
    public static boolean validaRecaptura(JDialog owner, JFormattedTextField tf, String l) {
        tf.setVisible(false);
        FrmRecapturaEx dialog = new FrmRecapturaEx(owner, true, l, tf.getText());
        dialog.setVisible(true);
        tf.setVisible(true);
        if (!dialog.isValidated()) {
            // tf.setText("");
            tf.requestFocusInWindow();
        }
        return dialog.isValidated();
    }

    public static boolean validaRecaptura(JDialog owner, JFormattedTextField tf, String l, JPanel container) {
        tf.setVisible(false);
        FrmRecapturaEx dialog = new FrmRecapturaEx(owner, true, l, tf.getText(), container);
        dialog.setVisible(true);
        tf.setVisible(true);
        if (!dialog.isValidated()) {
            // tf.setText("");
            tf.requestFocusInWindow();
        }
        return dialog.isValidated();
    }

    public static boolean validaRecaptura(JDialog owner, JFormattedTextField tf, String l, String mask) {
        try {
            tf.setVisible(false);
            FrmRecapturaEx dialog = new FrmRecapturaEx(owner, true, l, tf.getText(), mask);
            dialog.setVisible(true);
            tf.setVisible(true);
            if (!dialog.isValidated()) {
                // tf.setText("");
                tf.requestFocusInWindow();
            }
            return dialog.isValidated();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validaRecaptura(JDialog owner, JFormattedTextField tf, String l, javax.swing.text.MaskFormatter mask) {
        try {
            tf.setVisible(false);
            FrmRecapturaEx dialog = new FrmRecapturaEx(owner, true, l, tf.getText(), mask);
            dialog.setVisible(true);
            tf.setVisible(true);
            if (!dialog.isValidated()) {
                // tf.setText("");
                tf.requestFocusInWindow();
            }
            return dialog.isValidated();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validaRecaptura(JDialog owner, JFormattedTextField tf, String l, String mask, boolean isNumber) {
        try {
            tf.setVisible(false);
            FrmRecapturaEx dialog = null;
            if (isNumber) {
                dialog = new FrmRecapturaEx(owner, true, l, tf.getValue().toString(), mask, isNumber);
            } else {
                dialog = new FrmRecapturaEx(owner, true, l, tf.getText(), mask, isNumber);
            }

            dialog.setVisible(true);
            tf.setVisible(true);
            if (!dialog.isValidated()) {
                // tf.setText("");
                tf.requestFocusInWindow();
            }
            return dialog.isValidated();
        } catch (Exception e) {
            return false;
        }
    }

    public static String autorizaciones(JDialog owner) {
        FrmAutorizaciones frmAutorizaciones = new FrmAutorizaciones(owner, true);
        frmAutorizaciones.setVisible(true);
        return frmAutorizaciones.getPwdClaveSupervisor();
    }

    public static String autorizaciones(JDialog owner, String label) {
        FrmAutorizaciones frmAutorizaciones = new FrmAutorizaciones(owner, true, label);
        frmAutorizaciones.setVisible(true);
        return frmAutorizaciones.getPwdClaveSupervisor();
    }

    public boolean validarFecha(String fecha) {

        if (fecha == null) {
            return false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (fecha.trim().length() != dateFormat.toPattern().length()) {
            return false;
        }

        dateFormat.setLenient(false);
        try {
            dateFormat.parse(fecha.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
}
