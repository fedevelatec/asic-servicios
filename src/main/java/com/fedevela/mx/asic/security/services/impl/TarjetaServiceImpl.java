package com.fedevela.mx.asic.security.services.impl;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.security.pojos.TarjetaWebmx;
import com.fedevela.asic.daos.DmsDao;
import java.util.List;
import javax.annotation.Resource;
import com.fedevela.mx.asic.security.services.TarjetaService;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TarjetaServiceImpl implements TarjetaService {

    private final Logger logger = LoggerFactory.getLogger(TarjetaService.class);
    @Resource
    private DmsDao dao;

    @Override
    public List<TarjetaWebmx> getTarjetas(String login) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TarjetaWebmx.class);
        criteria.add(Restrictions.eq("login", login));
        return dao.find(criteria);
    }
}
