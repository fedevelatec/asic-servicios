package com.fedevela.mx.asic.security.services;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.security.pojos.TarjetaWebmx;

public interface CardService {

    public TarjetaWebmx getCard(final String username);
}
