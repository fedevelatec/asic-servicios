package com.fedevela.asic.servicios;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.asic.excepciones.ServicioException;
import com.fedevela.core.asic.pojos.Personas;

/**
 *
 * @author rvillaueva
 */
@Deprecated
public interface PersonaServicio {

    /**
     * @deprecated Usar Ahora CatalogoServicio.getPersona.
     * @param codigo
     * @return
     * @throws ServicioException
     */
    public Personas getPersona(long codigo) throws ServicioException;
}