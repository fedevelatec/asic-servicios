package com.fedevela.dms.services;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.sc.pojos.ScEntregaRetorno;
import com.fedevela.core.sc.pojos.ScObservacionesRetorno;

public interface SCService {

    /**
     *
     * @param entregaRetorno
     */
    public void saveDeliveryReception(ScEntregaRetorno entregaRetorno);

    public ScObservacionesRetorno saveObservacionesRetorno(ScObservacionesRetorno observacionesRetorno);
}
