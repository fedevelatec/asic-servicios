package com.fedevela.controlimagen.servicios;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.asic.excepciones.ServicioException;
import com.fedevela.controlimagen.beans.ChecklistControlImagen;
import com.fedevela.controlimagen.beans.PaginaControlImagen;
import java.util.List;

@Deprecated
public interface ControlImagenServicio {

    public List<ChecklistControlImagen> getChechlistCIByClienteU(Long scltcod, Long nunicodoc) throws ServicioException;

    public List<ChecklistControlImagen> getChechlistCIByClienteUT(Long scltcod, Long nunicodoc, Long nunicodoct) throws ServicioException;

    public List<PaginaControlImagen> getPaginaCIByClienteIddocto(Long scltcod, String idCode) throws ServicioException;

    public List<PaginaControlImagen> getPaginaCIByClienteU(Long scltcod, Long nunicodoc) throws ServicioException;

    public List<PaginaControlImagen> getPaginaCIByClienteUT(Long scltcod, Long nunicodoc, Long nunicodoct) throws ServicioException;

    public List<PaginaControlImagen> getPaginaCIClienteByUTPagina(Long scltcod, Long nunicodoc, Long nunicodoct, Long pagina) throws ServicioException;

    public List<PaginaControlImagen> getPaginaCI(Object[] parametros) throws ServicioException;
}
