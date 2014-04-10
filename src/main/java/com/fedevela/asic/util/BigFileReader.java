package com.fedevela.asic.util;

/**
 * Created by fvelazquez on 9/04/14.
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BigFileReader {

    /**
     * Default constuctor
     */
    public BigFileReader() {
    }

    /**
     * Reads a file storing intermediate data into a list. Fast method.
     *
     * @param file the file to be read
     * @return a file data
     */
    public byte[] read2list(String file) throws Exception {
        InputStream in = null;
        byte[] buf = null; // output buffer
        int bufLen = 20000 * 1024;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            buf = new byte[bufLen];
            byte[] tmp;
            int len;
            List data = new ArrayList(24); // keeps peaces of data
            while ((len = in.read(buf, 0, bufLen)) != -1) {
                tmp = new byte[len];
                System.arraycopy(buf, 0, tmp, 0, len); // still need to do copy
                data.add(tmp);
            }
            /*
             This part os optional. This method could return a List data
             for further processing, etc.
             */
            len = 0;
            if (data.size() == 1) {
                return (byte[]) data.get(0);
            }
            for (int i = 0; i < data.size(); i++) {
                len += ((byte[]) data.get(i)).length;
            }
            buf = new byte[len]; // final output buffer
            len = 0;
            for (int i = 0; i < data.size(); i++) { // fill with data
                tmp = (byte[]) data.get(i);
                System.arraycopy(tmp, 0, buf, len, tmp.length);
                len += tmp.length;
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
        return buf;
    }

    /**
     * Reads a file storing intermediate data into an array.
     *
     * @param file the file to be read
     * @return a file data
     */
    public byte[] read2array(String file) throws Exception {
        InputStream in = null;
        byte[] out = new byte[0];
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            // the length of a buffer can vary
            int bufLen = 20000 * 1024;
            byte[] buf = new byte[bufLen];
            byte[] tmp = null;
            int len = 0;
            while ((len = in.read(buf, 0, bufLen)) != -1) {
                // extend array
                tmp = new byte[out.length + len];
                // copy data
                System.arraycopy(out, 0, tmp, 0, out.length);
                System.arraycopy(buf, 0, tmp, out.length, len);
                out = tmp;
                tmp = null;
            }
        } finally {
            // always close the stream
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
        return out;
    }

    /**
     * Creates a big file with given name
     *
     * @param file the file name
     */
    public void createData(String file) throws Exception {
        BufferedOutputStream os = new BufferedOutputStream(
                new FileOutputStream(file));
        byte[] b = new byte[]{0xC, 0xA, 0xF, 0xE, 0xB, 0xA, 0xB, 0xE};
        int c = 100000;
        for (int i = 0; i < c; i++) {
            os.write(b);
            os.flush();
        }
    }
}
