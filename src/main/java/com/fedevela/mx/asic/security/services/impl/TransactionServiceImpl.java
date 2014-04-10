package com.fedevela.mx.asic.security.services.impl;

/**
 * Created by fvelazquez on 10/04/14.
 */
import com.fedevela.core.security.pojos.AccesoWebmx;
import com.fedevela.core.security.pojos.TransaccionWebmx;
import com.fedevela.core.security.pojos.VwTransactionWebmx;
import com.fedevela.asic.daos.DmsDao;
//import com.adeamx.persistencia.dao.SecurityDao;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.Resource;
import com.fedevela.mx.asic.security.services.TransactionService;
import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService, Serializable {

    private Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

//    @Resource
//    private SecurityDao dao;

    @Resource
    private DmsDao dao;

    @Override
    public void register(AccesoWebmx aw) {
        dao.persist(aw);
    }

    @Override
    public void register(TransaccionWebmx tw) {
        dao.persist(tw);
    }

    @Override
    public List<VwTransactionWebmx> getTransactions(DetachedCriteria criteria) {
        return dao.find(criteria);
    }

    @Override
    public List<VwTransactionWebmx> getTransactions(DetachedCriteria criteria, BigInteger page, BigInteger pageSize) {
        return dao.find(criteria, page.intValue(), pageSize.intValue());
    }
}
