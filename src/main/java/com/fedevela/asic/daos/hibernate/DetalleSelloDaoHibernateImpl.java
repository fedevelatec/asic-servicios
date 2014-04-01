package com.fedevela.asic.daos.hibernate;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.asic.daos.DetalleSelloDao;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

/**
 *
 * @author fvilla
 */
@Repository("detalleSelloDao")
public class DetalleSelloDaoHibernateImpl extends HibernateDaoSupport implements DetalleSelloDao {

    @Resource(name = "templateCapturaGralAnnotations")
    public void setTemplate(HibernateTemplate hibernateTemplate) {
        setHibernateTemplate(hibernateTemplate);
    }

    @Override
    public List<String> getDocumentosNoCapturadosPorCaja(Long cajaId) {
        final long caja = cajaId;
        return getHibernateTemplate().executeFind(new HibernateCallback<List<String>>() {
            @Override
            public List<String> doInHibernate(Session session) throws HibernateException, SQLException {
                StringBuilder q = new StringBuilder();
                q.append("SELECT 'T' || nunicodoct as nunicodoct ");
                q.append("  FROM prod.flow_ingresos_deta_mpb ");
                q.append(" WHERE caja_id = :caja AND nunicodoct <> 0 ");
                q.append("MINUS ");
                q.append("SELECT 'T' || nunicodoct as nunicodoct ");
                q.append("  FROM prod.detalle_sello_docum ");
                q.append(" WHERE nunicosello = :caja1 ");
                SQLQuery query = session.createSQLQuery(q.toString());
                query.addScalar("nunicodoct", Hibernate.STRING);
                query.setLong("caja", caja);
                query.setLong("caja1", caja);
                return query.list();
            }
        });
    }
}
