package com.fedevela.asic.util;

/**
 * Created by fvelazquez on 9/04/14.
 */
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Clase de utilidad para limitar el numero de caracteres en un JTextField
 *
 */
public class LengthLimitedDocument extends PlainDocument {

    private int limit;

    public LengthLimitedDocument(int limit) {
        super();
        this.limit = limit;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        String pre = getText(0, offs);
        String post = "";
        int length = getLength();
        if (offs < length) {
            post = getText(offs, length - offs);
        }
        String candidate = pre + str + post;
        if (candidate.length() > limit) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            return;
        }
        super.insertString(offs, str == null ? null : str.toUpperCase(), a);
    }
}
