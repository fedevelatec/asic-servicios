package com.fedevela.mx.asic.security.services;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.security.pojos.TarjetaWebmx;
import java.util.List;


public interface TarjetaService {

    public List<TarjetaWebmx> getTarjetas(final String login);
}
