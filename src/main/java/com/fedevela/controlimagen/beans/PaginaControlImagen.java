package com.fedevela.controlimagen.beans;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.asic.util.TypeCast;
import java.io.File;

@Deprecated
public class PaginaControlImagen {

    private Long pagina;
    private String nombreImagen;
    private String tipo;
    private byte[] contenidoArchivo;
    private ChecklistControlImagen checklistControlImagen;

    public PaginaControlImagen() {
    }

    public PaginaControlImagen(
            Long pagina,
            String nombreImagen,
            ChecklistControlImagen checklistControlImagen) {
        this.pagina = pagina;
        this.nombreImagen = nombreImagen;
        this.checklistControlImagen = checklistControlImagen;

        setTipo(obtenerExtension(nombreImagen));
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;

        setTipo(obtenerExtension(nombreImagen));
    }

    public Long getPagina() {
        return pagina;
    }

    public void setPagina(Long pagina) {
        this.pagina = pagina;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ChecklistControlImagen getChecklistControlImagen() {
        return checklistControlImagen;
    }

    public void setChecklistControlImagen(ChecklistControlImagen checklistControlImagen) {
        this.checklistControlImagen = checklistControlImagen;
    }

    public byte[] getContenidoArchivo() {
        return contenidoArchivo;
    }

    public void setContenidoArchivo(byte[] contenidoArchivo) {
        this.contenidoArchivo = contenidoArchivo;
    }

    private String obtenerExtension(String nombreArchivo) {
        String extension = null;

        if (nombreArchivo != null) {
            extension = nombreArchivo.substring(nombreArchivo.lastIndexOf('.') + 1);
        }

        return extension;
    }

    public boolean cargaArchivo(String archivo) {
        boolean statusCarga = false;
        if (archivo != null) {
            try {
                contenidoArchivo = TypeCast.toBytes(new File(archivo));
                statusCarga = true;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return statusCarga;
    }
}
