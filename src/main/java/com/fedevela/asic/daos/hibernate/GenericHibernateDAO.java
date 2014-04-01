package com.fedevela.asic.daos.hibernate;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.asic.daos.GenericDAO;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

@Deprecated
public abstract class GenericHibernateDAO<T, ID extends Serializable> extends HibernateDaoSupport
        implements GenericDAO<T, ID> {

    private Class<T> persistentClass;

    public GenericHibernateDAO() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void delete(T entity) {
        getHibernateTemplate().delete(entity);
    }

    @Override
    public List<T> findByExample(T exampleInstance) {
        return getHibernateTemplate().findByExample(exampleInstance);
    }

    @Override
    public T findById(ID id) {
        return (T) getHibernateTemplate().get(persistentClass, id);
    }

    @Override
    public T save(T entity) {
        getHibernateTemplate().saveOrUpdate(entity);
        return entity;
    }
}
