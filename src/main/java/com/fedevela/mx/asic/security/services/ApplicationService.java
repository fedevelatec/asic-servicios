package com.fedevela.mx.asic.security.services;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.security.pojos.AplicacionWebmx;
import java.util.List;

public interface ApplicationService {

    public List<AplicacionWebmx> getAplicacions();

    public List<AplicacionWebmx> getAplicacions(
            final Long idaplicacion,
            final String nombre,
            final String descripcion,
            final Character status,
            final String login,
            final Integer page,
            final Integer size);

    public AplicacionWebmx saveApplication(AplicacionWebmx app);

    public AplicacionWebmx getApplication( Long idApp);

    public void deleteAplicacion(Long id) throws Exception;
}
