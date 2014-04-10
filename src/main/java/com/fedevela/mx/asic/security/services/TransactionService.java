package com.fedevela.mx.asic.security.services;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.security.pojos.AccesoWebmx;
import com.fedevela.core.security.pojos.TransaccionWebmx;
import com.fedevela.core.security.pojos.VwTransactionWebmx;
import java.math.BigInteger;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;

public interface TransactionService {

    /**
     *
     * @param aw
     */
    public void register(AccesoWebmx aw);

    /**
     *
     * @param tw
     */
    public void register(TransaccionWebmx tw);

    public List<VwTransactionWebmx> getTransactions(DetachedCriteria criteria);

    public List<VwTransactionWebmx> getTransactions(DetachedCriteria criteria, final BigInteger page, final BigInteger pageSize);
}
