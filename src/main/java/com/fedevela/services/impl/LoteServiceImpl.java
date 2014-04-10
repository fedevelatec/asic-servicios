package com.fedevela.services.impl;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.asic.pojos.Lote;
import com.fedevela.core.avaluo.pojos.AvDocumentoAvaluo;
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.asic.util.TypeCast;
import com.fedevela.services.LoteService;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoteServiceImpl implements LoteService {

    private Logger log = LoggerFactory.getLogger(LoteServiceImpl.class);
    @Resource
    private DmsDao dao;

    private Lote newLote(Short scltcod, Short operatoria, String login, Date fechaCreacion) {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Lote.class);
            /**
             * * PROJECTIONS **
             */
            criteria.setProjection(Projections.max("lotePK.idLote"));
            /**
             * * RESTRICTIONS **
             */
            criteria.add(Restrictions.eq("status", 'C'));
            criteria.add(Restrictions.eq("lotePK.scltcod", scltcod));
            criteria.add(Restrictions.eq("operatoria", operatoria));
            List<Lote> rs = dao.find(criteria);
            BigInteger idLote = null;
            if ((rs == null) || (rs.isEmpty())) {
                idLote = TypeCast.toBigInteger(1);
            } else {
                idLote = TypeCast.toBigInteger(rs.get(0).getLotePK().getIdLote().intValue() + 1);
            }
            Lote lote = new Lote(idLote, scltcod);
            lote.setNoItems(TypeCast.toBigInteger(0));
            lote.setStatus('A');
            lote.setNombre(TypeCast.CF("AV", "0", 3, 1));
            lote.setFechaCreacion(fechaCreacion);
            lote.setUsuarioCreacion(login);
            lote.setNoMaxItems(TypeCast.toBigInteger(20000));
            lote.setOperatoria(TypeCast.toShort(0));
            return dao.persist(lote);
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
            return null;
        }
    }

    private Lote getLote(Short scltcod, Short operatoria) {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Lote.class);
            /**
             * * RESTRICTIONS **
             */
            criteria.add(Restrictions.eq("status", 'A'));
            criteria.add(Restrictions.eq("lotePK.scltcod", scltcod));
            criteria.add(Restrictions.eq("operatoria", operatoria));
            List<Lote> rs = dao.find(criteria);
            if ((rs == null) || (rs.isEmpty())) {
                return null;
            }
            return rs.get(0);
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
            return null;
        }
    }

    @Override
    public boolean saveLote(Lote lote) {
        try {
            dao.persist(lote);
            return true;
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
            return false;
        }
    }

    @Override
    public Lote validateLote(Short scltcod, Short operatoria, String login, Date now, Lote lote) {
        /**
         * Si el lote es NULO entonces intentamos abrir uno
         */
        if (lote == null) {
            lote = getLote(scltcod, operatoria);
            /**
             * Si el lote es NULO entonces no exite aun ningun lote con para el
             * cliente + operatoria por lo que generamos 1 totalmente nuevo
             */
            if (lote == null) {
                return newLote(scltcod, operatoria, login, now);
            }
        }
        /**
         * Si el numero de items >= maximo permitido Entonces cerramos el lote y
         * generamos 1 nuevo
         */
        if (lote.getNoItems().intValue() >= lote.getNoMaxItems().intValue()) {
            lote.setStatus('C');
            lote.setFechaCierre(now);
            lote.setUsuarioCierre(login);
            saveLote(lote);
            return newLote(scltcod, operatoria, login, now);
        }
        return lote;
    }

    @Override
    public Lote updateNoItems(Lote lote) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AvDocumentoAvaluo.class);
        /**
         * * PROJECTIONS **
         */
        criteria.setProjection(Projections.countDistinct("avDocumentoAvaluoPK.nunicodoc"));
        /**
         * * RESTRICTIONS **
         */
        criteria.add(Restrictions.eq("lote", lote.getNombre()));
        List<Object> rs = dao.find(criteria);
        if ((rs == null) || (rs.isEmpty()) || (rs.size() > 1)) {
            /**
             * Error, no existe algun lote, primero hay que generarlos
             */
        } else {
            Integer cNunicodoc = TypeCast.toInteger(rs.get(0));
            if ((cNunicodoc != null) && (lote.getNoItems().intValue() != cNunicodoc.intValue())) {
                lote.setNoItems(TypeCast.toBigInteger(cNunicodoc));
                saveLote(lote);
            }
        }
        return lote;
    }
}
