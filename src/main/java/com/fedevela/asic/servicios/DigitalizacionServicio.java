package com.fedevela.asic.servicios;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.core.asic.pojos.ChecklistDig;
import com.fedevela.core.asic.pojos.PaginaDig;
import com.fedevela.core.asic.pojos.VwDigitalizacion;
import com.fedevela.asic.excepciones.ServicioException;
import java.util.List;

/**
 *
 * @author fvilla
 */
public interface DigitalizacionServicio {

    public List<ChecklistDig> getChechlistByNunicodocCliente(Long nunicodoc, Long scltcod) throws ServicioException;

    public PaginaDig getPaginaDigNunicodocNunicodocTPagina(Long nunicodoc, Long nunicodoct, Long pagina) throws ServicioException;

    public ChecklistDig getChecklistDigById(Long nunicodoc, Long nunicodoct) throws ServicioException;

    public ChecklistDig getChecklistDigByT(Long nunicodoct) throws ServicioException;

    public ChecklistDig getChecklistDigByU(Long nunicodoc) throws ServicioException;

    public List<PaginaDig> getPaginaDigByUT(Long nunicodoc, Long nunicodoct) throws ServicioException;

    public Integer getNumPaginaDigByUT(Long nunicodoc, Long nunicodoct) throws ServicioException;

    /**
     *
     * @param etiqueta
     * @param tipo U o T
     * @return
     */
    public List<VwDigitalizacion> getVwDigitalizacion(final Long etiqueta, final Character tipo);
}
