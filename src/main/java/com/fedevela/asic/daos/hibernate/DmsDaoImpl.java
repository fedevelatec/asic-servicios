package com.fedevela.asic.daos.hibernate;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.asic.daos.DmsDao;
import net.codicentro.cliser.dao.impl.CliserSpringHibernateDao;
import javax.annotation.Resource;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DmsDaoImpl extends CliserSpringHibernateDao implements DmsDao {

    @Resource(name = "hibernateTemplate")
    public void setTemplate(HibernateTemplate hibernateTemplate) {
        setHibernateTemplate(hibernateTemplate);
    }
}
