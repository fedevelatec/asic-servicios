package com.fedevela.asic.daos.hibernate;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.asic.daos.CabeceraDocDao;
import com.fedevela.core.asic.pojos.CabeceraDoc;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

/**
 *
 * @author fvilla
 */
@Repository("cabeceraDocDao")
public class CabeceraDocDaoHibernateImpl extends HibernateDaoSupport implements CabeceraDocDao {

    @Resource(name="templateCapturaGralAnnotations")
    public void setTemplate(HibernateTemplate hibernateTemplate) {
        setHibernateTemplate(hibernateTemplate);
    }

    @Override
    public void save(CabeceraDoc cabeceraDoc) {
        getHibernateTemplate().saveOrUpdate(cabeceraDoc);
    }

    @Override
    public CabeceraDoc getCabeceraDocByNUnicoDoc(Long nunicoDoc) {
        return (CabeceraDoc) getHibernateTemplate().get(CabeceraDoc.class, nunicoDoc);
    }

    @Override
    public Integer getCabeceraDocCredito(String nroidentdoc, Long scltcod) {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from CabeceraDoc where nroidentdoc = ? and scltcod = ?", new Object[] {nroidentdoc, scltcod}));

    }

    @Override
    public List<CabeceraDoc> getCabeceraDocByNroidentdoc(String nroidentdoc, Long scltcod) {
        String query = "from CabeceraDoc cab where cab.nroidentdoc = ? and scltcod = ?";
        return getHibernateTemplate().find(query, new Object[]{nroidentdoc, scltcod});
    }
}
