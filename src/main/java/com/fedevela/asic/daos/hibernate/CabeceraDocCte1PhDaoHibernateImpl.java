package com.fedevela.asic.daos.hibernate;

/**
 * Created by fvelazquez on 31/03/14.
 */
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.fedevela.asic.daos.CabeceraDocCte1PhDao;
import com.fedevela.core.asic.pojos.CabeceraDocCte1Ph;
import com.fedevela.core.asic.pojos.CabeceraDocCte1PhId;

/**
 *
 * @author fvilla
 */
@Repository("cabeceraDocCte1PhDao")
public class CabeceraDocCte1PhDaoHibernateImpl
        extends GenericHibernateDAO<CabeceraDocCte1Ph, CabeceraDocCte1PhId> implements CabeceraDocCte1PhDao{

    @Resource(name="templateCapturaGralAnnotations")
    public void setTemplate(HibernateTemplate hibernateTemplate) {
        setHibernateTemplate(hibernateTemplate);
    }

    @Override
    public CabeceraDocCte1Ph get(CabeceraDocCte1PhId id) {
        return (CabeceraDocCte1Ph) getHibernateTemplate().get(CabeceraDocCte1Ph.class, id);
    }

    @Override
    public List<CabeceraDocCte1Ph> buscaPorNunicoDocT(Long nunicodoct) {
        String query = "from CabeceraDocCte1Ph cab where cab.nunicodoct = ?";
        return getHibernateTemplate().find(query, nunicodoct);
    }

    @Override
    public Integer getSiguienteDocCodPorNunicodoc(Long nunicodoc) {
        String query = "select count(*) from CabeceraDocCte1Ph a where a.id.nunicodoc = ?";
        return DataAccessUtils.intResult(getHibernateTemplate().find(query, new Object[] {nunicodoc}));
    }

    @Override
    public List getDocCodDisponiblePorNunicodoc(Long nunicodoc, int scltcod) {
        final long NUNICODOC = nunicodoc;
        final int SCLTCOD = scltcod;

        return (List) getHibernateTemplate().executeFind(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String strQuery = new String();

                strQuery  = " select doccod  " ;
                strQuery += " from   prod.tipo_docum_cte1_ph        " ;
                strQuery += " where  scltcod = :scltcod and doccod not in( " ;
                strQuery += "        select doccod                      " ;
                strQuery += "        from   prod.cabecera_doc_cte1_ph   " ;
                strQuery += "        where  nunicodoc = :nunicodoc) " ;
                strQuery += " order  by doccod" ;

                SQLQuery query = session.createSQLQuery(strQuery);
                query.setLong("nunicodoc", NUNICODOC);
                query.setLong("scltcod", SCLTCOD);
                List list = query.list();
                return list;
            }
        });
    }

    @Override
    public List<CabeceraDocCte1Ph> getByDescLike(String descripcion, Long scltcod, int maxResults) {
        DetachedCriteria criteria = DetachedCriteria.forClass(CabeceraDocCte1Ph.class);
        criteria.add(Restrictions.ilike("observaciones", "%" + descripcion + "%"))
                .createAlias("cabeceraDoc", "cabeceraDoc")
                .add(Restrictions.eq("cabeceraDoc.scltcod", scltcod))
                .createAlias("cabeceraDoc.cabeceraDocCte1Ph", "cabeceraDocCte1Ph")
                .add(Restrictions.ilike("cabeceraDocCte1Ph.observaciones", "%" + descripcion + "%"));
        List<CabeceraDocCte1Ph> r = getHibernateTemplate().findByCriteria(criteria, 0, maxResults);
        for (CabeceraDocCte1Ph cabeceraDocCte1Ph : r) {
            if (!cabeceraDocCte1Ph.getCabeceraDoc().getScltcod().equals(scltcod)) {
                r.remove(cabeceraDocCte1Ph);
            }
        }
        return r;
    }

    @Override
    public CabeceraDocCte1Ph findByNunicodoct(Long nunicodoct, Long nunicodoc, short doccod) {
        List lst = getHibernateTemplate().find("FROM CabeceraDocCte1Ph c WHERE c.nunicodoct = " + nunicodoct);
        if ((lst == null) || (lst.isEmpty())) {
            return null;
        } else if (lst.size() == 1) {
            CabeceraDocCte1Ph c = (CabeceraDocCte1Ph) lst.get(0);
            if ((c.getId().getNunicodoc() == nunicodoc) && (c.getId().getDoccod() == doccod)) {
                return null;
            } else {
                return c;
            }
        } else {
            return (CabeceraDocCte1Ph) lst.get(0);
        }
    }

    @Override
    public int deleteByUnicodoc(Long nunicodoc){
        return getHibernateTemplate().bulkUpdate("DELETE FROM CabeceraDocCte1Ph c WHERE c.id.nunicodoc = " + nunicodoc);
    }

    @Override
    public int deleteByNunicodoct(Long nunicodoct){
        return getHibernateTemplate().bulkUpdate("DELETE FROM CabeceraDocCte1Ph c WHERE c.nunicodoct = " + nunicodoct);
    }
}
