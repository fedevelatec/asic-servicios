package com.fedevela.services.impl;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.avaluo.pojos.AvAvaluoCaja;
import com.fedevela.core.avaluo.pojos.AvDocumentoAvaluo;
import com.fedevela.core.avaluo.pojos.AvDocumentoAvaluoPK;
import com.fedevela.core.avaluo.pojos.AvPaginaDocum;
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.asic.util.TypeCast;
import com.fedevela.services.AvaluoService;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AvaluoServiceImpl implements AvaluoService {

    private Logger logger = LoggerFactory.getLogger(AvaluoServiceImpl.class);
    @Resource
    private DmsDao dao;

    @Override
    public AvAvaluoCaja getAvaluoCaja(BigInteger nunicodoc) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AvAvaluoCaja.class);
        criteria.add(Restrictions.eq("nunicodoc", nunicodoc));
        List<AvAvaluoCaja> rs = dao.find(criteria);
        if ((rs != null) && (!rs.isEmpty())) {
            return rs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public AvDocumentoAvaluo getDocumentoAvaluo(BigInteger nunicodoc, BigInteger nunicodoct) {
        return dao.get(AvDocumentoAvaluo.class, new AvDocumentoAvaluoPK(nunicodoc.intValue(), nunicodoct.longValue()));
    }

    @Override
    public boolean saveDocuentoAvaluo(AvDocumentoAvaluo documentoAvaluo) {
        try {
            dao.persist(documentoAvaluo);
            return true;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override
    public boolean saveDocuentoAvaluo(List<AvDocumentoAvaluo> documentosAvaluo) {
        try {
            dao.persist(documentosAvaluo.toArray());
            return true;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override
    public boolean savePaginaDocum(List<AvPaginaDocum> paginasDocum) {
        try {
            dao.persist(paginasDocum.toArray());
            return true;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override
    public int countPaginaDocum(int nunicodoc) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AvPaginaDocum.class);
        /**
         * * PROJECTIONS **
         */
        criteria.setProjection(Projections.count("avPaginaDocumPK.nunicodoc"));
        /**
         * * RESTRICTIONS **
         */
        criteria.add(Restrictions.eq("avPaginaDocumPK.nunicodoc", nunicodoc));
        List<Object> rs = dao.find(criteria);
        Integer cNunicodoc = 0;
        if ((rs != null) && (!rs.isEmpty())) {
            cNunicodoc = TypeCast.toInteger(rs.get(0));
            if (cNunicodoc != null) {
                return cNunicodoc.intValue();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }
}
