package com.fedevela.dms.services.impl;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.sc.pojos.ScEntregaRetorno;
import com.fedevela.core.sc.pojos.ScObservacionesRetorno;
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.dms.services.SCService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SCServiceImpl implements SCService {

    @Resource
    private DmsDao dao;

    @Override
    public void saveDeliveryReception(ScEntregaRetorno entregaRetorno) {
        dao.persist(entregaRetorno);
    }

    @Override
    public ScObservacionesRetorno saveObservacionesRetorno(ScObservacionesRetorno observacionesRetorno) {
        return dao.persist(observacionesRetorno);
    }
}
