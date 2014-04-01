package com.fedevela.asic.servicios;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.core.hn.pojos.HnAutorizadores;
import com.fedevela.asic.excepciones.ServicioException;
import java.math.BigInteger;

/**
 *
 * @author egutierrez
 */
public interface AutorizacionesServicio {

    /**
     *
     * @param numAutoriz
     * @return
     * @deprecated
     */
    public HnAutorizadores getHnAutorizadores(BigInteger numAutoriz);

    public HnAutorizadores getAutorizacion(final BigInteger numAutoriz) throws ServicioException;
}
