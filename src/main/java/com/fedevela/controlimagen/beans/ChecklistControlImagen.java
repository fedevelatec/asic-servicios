package com.fedevela.controlimagen.beans;

/**
 * Created by fvelazquez on 9/04/14.
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class ChecklistControlImagen implements Serializable {

    private long nunicodoc;
    private long nunicodoct;
    private long scltcod;
    private String rutaDestino;
    private int imagenesDig;
    private int imagenesTrans;
    private int imagenesActivas;
    private String lote;
    private short status;
    private String usuario;
    private List<PaginaControlImagen> paginas = new ArrayList<PaginaControlImagen>();
    private String desc;

    public ChecklistControlImagen() {
    }

    public ChecklistControlImagen(long nunicodoc, long nunicodoct, long scltcod, String rutaDestino, int imagenesDig, int imagenesTrans, int imagenesActivas, String lote, short status, String usuario, String desc) {
        this.nunicodoc = nunicodoc;
        this.nunicodoct = nunicodoct;
        this.scltcod = scltcod;
        this.rutaDestino = rutaDestino;
        this.imagenesDig = imagenesDig;
        this.imagenesTrans = imagenesTrans;
        this.imagenesActivas = imagenesActivas;
        this.lote = lote;
        this.status = status;
        this.usuario = usuario;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getImagenesActivas() {
        return imagenesActivas;
    }

    public void setImagenesActivas(int imagenesActivas) {
        this.imagenesActivas = imagenesActivas;
    }

    public int getImagenesDig() {
        return imagenesDig;
    }

    public void setImagenesDig(int imagenesDig) {
        this.imagenesDig = imagenesDig;
    }

    public int getImagenesTrans() {
        return imagenesTrans;
    }

    public void setImagenesTrans(int imagenesTrans) {
        this.imagenesTrans = imagenesTrans;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public long getNunicodoc() {
        return nunicodoc;
    }

    public void setNunicodoc(long nunicodoc) {
        this.nunicodoc = nunicodoc;
    }

    public long getNunicodoct() {
        return nunicodoct;
    }

    public void setNunicodoct(long nunicodoct) {
        this.nunicodoct = nunicodoct;
    }

    public List<PaginaControlImagen> getPaginas() {
        return paginas;
    }

    public void setPaginas(List<PaginaControlImagen> paginas) {
        this.paginas = paginas;
    }

    public String getRutaDestino() {
        return rutaDestino;
    }

    public void setRutaDestino(String rutaDestino) {
        this.rutaDestino = rutaDestino;
    }

    public long getScltcod() {
        return scltcod;
    }

    public void setScltcod(long scltcod) {
        this.scltcod = scltcod;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
