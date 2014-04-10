package com.fedevela.dms.util;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.asic.util.TypeCast;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.imageio.ImageIO;
import javax.swing.*;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScrollablePicture extends JLabel implements Scrollable, MouseMotionListener, MouseListener {

    private Logger logger = LoggerFactory.getLogger(ScrollablePicture.class);
    private int maxUnitIncrement = 1;
    private boolean missingPicture = false;
    private Tesseract ocrEngine;
    private BufferedImage image;
    private BufferedImage wImage;// Working image
    private JScrollPane psp;
    private Window window;
    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    public ScrollablePicture(String rutaImagen, final JScrollPane psp, Boolean ocr) {
        //  super(img);
//        if (img == null) {
//            missingPicture = true;
//            setText("No picture found.");
//            setHorizontalAlignment(CENTER);
//            setOpaque(true);
//            setBackground(Color.white);
//        }
        BufferedImage budImag = null;
        try {
            URL url = new URL(rutaImagen);
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream ioStream = connection.getInputStream();
            budImag = ImageIO.read(ioStream);
            ioStream.close();
        } catch (IOException ioe) {
            logger.error("ERROR AL CREAR LA IMAGEN\n\n" + ioe.getMessage());
        }
        //Let the user scroll by dragging to outside the window.
        this.image = budImag;
        if (budImag != null) {
            budImag.flush();
        }
        this.psp = psp;
        int W = image.getWidth() > psp.getWidth() ? psp.getWidth() - 5 : image.getWidth();
        // Obtener porciento que se redujo el ancho.
        Double H = (image.getWidth() - W) * 100.0 / image.getWidth();
        // Calcular los pixeles que se reducen.
        H = image.getHeight() - (image.getHeight() * H / 100);
        wImage = new BufferedImage(W, H.intValue(), image.getType());
        Graphics2D g = wImage.createGraphics();
        g.drawImage(image, 0, 0, W, H.intValue(), null);
        g.dispose();
        setAutoscrolls(true); //enable synthetic drag events
        addMouseListener(this);
        addMouseMotionListener(this);
        if (ocr) {
            try {
                ocrEngine = Tesseract.getInstance();
                ocrEngine.setLanguage("spa");
            } catch (java.lang.UnsatisfiedLinkError ex) {
                logger.warn(ex.getMessage());
            }
        }
        psp.getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    applyOCR();
                } catch (TesseractException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        /**
         * ROTATE
         */
        psp.getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotate90ToRight();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.ALT_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        psp.getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotate90ToLeft();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.ALT_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        /**
         * ZOOM KEY
         */
        psp.getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoomIn();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.ALT_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);


        psp.getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoomOut();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.ALT_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        /**
         * ZOOM MOUSE
         */
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent mwe) {
                if (mwe.getWheelRotation() < 0) {
                    zoomIn();
                } else if (mwe.getWheelRotation() > 0) {
                    zoomOut();
                }
            }
        });

        /**
         * SCROLL HORIZONTAL
         */
        psp.getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int value = psp.getHorizontalScrollBar().getValue() + 10;
                value = value > psp.getHorizontalScrollBar().getMaximum() ? psp.getHorizontalScrollBar().getMaximum() : value;
                psp.getHorizontalScrollBar().setValue(value);
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, InputEvent.ALT_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        psp.getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int value = psp.getHorizontalScrollBar().getValue() - 10;
                value = value < psp.getHorizontalScrollBar().getMinimum() ? psp.getHorizontalScrollBar().getMinimum() : value;
                psp.getHorizontalScrollBar().setValue(value);
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, InputEvent.ALT_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        /**
         * SCROLL VERTICAL
         */
        psp.getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int value = psp.getVerticalScrollBar().getValue() + 10;
                value = value > psp.getVerticalScrollBar().getMaximum() ? psp.getVerticalScrollBar().getMaximum() : value;
                psp.getVerticalScrollBar().setValue(value);
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, InputEvent.ALT_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        psp.getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int value = psp.getVerticalScrollBar().getValue() - 10;
                value = value < psp.getVerticalScrollBar().getMinimum() ? psp.getVerticalScrollBar().getMinimum() : value;
                psp.getVerticalScrollBar().setValue(value);
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, InputEvent.ALT_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);


        setIcon();
    }

    //    public ScrollablePicture(Window window, String rutaImagen, JScrollPane psp, Boolean ocr) {
//        this(rutaImagen, psp, ocr);
//        this.window = window;
//    }
    public ScrollablePicture(Window window, String rutaImagen, JScrollPane psp, Boolean ocr) {
        this(rutaImagen, psp, ocr);
        this.window = window;
    }

    ////    public void setImage(BufferedImage image) {
////        //flushImag();
////        this.image = image;
////    }
    public void flushImag() {
        if (image != null) {
            image.flush();
            image = null;
        }
        if (wImage != null) {
            wImage.flush();
            wImage = null;
        }
    }

    private void applyOCR() throws TesseractException {
        if (selection != null && selection.width > 0 && selection.height > 0 && ocrEngine != null) {
            Rectangle crop = selection.intersection(new Rectangle(wImage.getData().getBounds()));
            String ocr = ocrEngine.doOCR(wImage.getSubimage(crop.x, crop.y, crop.width, crop.height));
            if (!TypeCast.isBlank(ocr.toString()) && window != null && window.getFocusOwner() instanceof javax.swing.text.JTextComponent) {
                javax.swing.text.JTextComponent txt = ((javax.swing.text.JTextComponent) window.getFocusOwner());
                if (!TypeCast.isBlank(txt.getName())) {
                    if (txt.getName().indexOf("ocr-limpia-espacio-blanco") != -1) {
                        ocr = ocr.replaceAll(" ", "");
                    }
                    if (txt.getName().indexOf("ocr-solo-numero") != -1) {
                        ocr = ocr.replaceAll("\\D*", "");
                    }
                }
                clipboard.setContents(new StringSelection(ocr), null);
                txt.paste();
            } else {
                clipboard.setContents(new StringSelection(ocr), null);
            }
            selection = null;
            repaint();
        }
    }

    private void setIcon() {
        setIcon(new ImageIcon(wImage));
        psp.setViewportView(this);
        // psp.setViewportView(new JLabel(new ImageIcon(wImage)));
    }

    @Override
    public Dimension getPreferredSize() {
        if (missingPicture) {
            return new Dimension(320, 480);
        } else {
            return super.getPreferredSize();
        }
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        //Get the current position.
        int currentPosition;
        if (orientation == SwingConstants.HORIZONTAL) {
            currentPosition = visibleRect.x;
        } else {
            currentPosition = visibleRect.y;
        }

        //Return the number of pixels between currentPosition
        //and the nearest tick mark in the indicated direction.
        if (direction < 0) {
            int newPosition = currentPosition
                    - (currentPosition / maxUnitIncrement)
                    * maxUnitIncrement;
            return (newPosition == 0) ? maxUnitIncrement : newPosition;
        } else {
            return ((currentPosition / maxUnitIncrement) + 1)
                    * maxUnitIncrement
                    - currentPosition;
        }
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect,
                                           int orientation,
                                           int direction) {
        if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width - maxUnitIncrement;
        } else {
            return visibleRect.height - maxUnitIncrement;
        }
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    public void setMaxUnitIncrement(int pixels) {
        maxUnitIncrement = pixels;
    }
    // MODE SELECTION
    private Rectangle selection;
    private Point anchor;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (selection != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLUE);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5F));
            g2d.fill(selection);
        }
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        selection.setBounds(Math.min(anchor.x, me.getX()),
                Math.min(anchor.y, me.getY()),
                Math.abs(me.getX() - anchor.x),
                Math.abs(me.getY() - anchor.y));
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent me) {
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        selection = null;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent me) {
        anchor = me.getPoint();
        selection = new Rectangle(anchor);
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        try {
            applyOCR();
        } catch (TesseractException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    public void rotate90ToLeft() {
        int width = wImage.getWidth();
        int height = wImage.getHeight();
        BufferedImage imgRotate = new BufferedImage(height, width, wImage.getType());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                imgRotate.setRGB(y, width - x - 1, wImage.getRGB(x, y));
            }
        }
        wImage = imgRotate;
        setIcon();
    }

    public void rotate90ToRight() {
        int width = wImage.getWidth();
        int height = wImage.getHeight();
        BufferedImage imgRotate = new BufferedImage(height, width, wImage.getType());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                imgRotate.setRGB(height - y - 1, x, wImage.getRGB(x, y));
            }
        }
        wImage = imgRotate;
        setIcon();
    }

    public void zoomIn() {
        int W = wImage.getWidth() + 100;
        W = W > image.getWidth() + 1000 ? image.getWidth() + 1000 : W;
        int H = wImage.getHeight() + 100;
        H = H > image.getHeight() + 1000 ? image.getHeight() + 1000 : W;
        wImage = new BufferedImage(W, H, image.getType());
        Graphics2D g = wImage.createGraphics();
        g.drawImage(image, 0, 0, W, H, null);
        g.dispose();
        setIcon();
    }

    public void zoomOut() {
        int W = wImage.getWidth() - 100;
        W = W < 1 ? 1 : W;
        int H = wImage.getHeight() - 100;
        H = H < 1 ? 1 : H;
        wImage = new BufferedImage(W, H, image.getType());
        Graphics2D g = wImage.createGraphics();
        g.drawImage(image, 0, 0, W, H, null);
        g.dispose();
        setIcon();
    }

    // MODE OCR
    public void flush() {
        if( image != null ){
            image.flush();
        }

        if( wImage != null ){
            wImage.flush();
        }

        image = null;
        wImage = null;
        System.gc();
    }
}
