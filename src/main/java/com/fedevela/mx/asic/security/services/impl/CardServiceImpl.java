package com.fedevela.mx.asic.security.services.impl;

/**
 * Created by fvelazquez on 10/04/14.
 */
import com.fedevela.core.security.pojos.TarjetaWebmx;
import com.fedevela.asic.daos.DmsDao;
//import com.adeamx.persistencia.dao.SecurityDao;
import java.util.List;
import javax.annotation.Resource;
import com.fedevela.mx.asic.security.services.CardService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl implements CardService {

    private Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);
//    @Resource
//    private SecurityDao dao;

    @Resource
    private DmsDao dao;

    @Override
    public TarjetaWebmx getCard(String username) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TarjetaWebmx.class);
        criteria.add(Restrictions.eq("login", username));
        criteria.add(Restrictions.eq("status", 'A'));
        List<TarjetaWebmx> tws = dao.find(criteria);
        if ((tws == null) || (tws.isEmpty())) {
            return null;
        } else {
            return tws.get(0);
        }
    }
}
