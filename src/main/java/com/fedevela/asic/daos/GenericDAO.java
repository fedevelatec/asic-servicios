package com.fedevela.asic.daos;

/**
 * Created by fvelazquez on 31/03/14.
 */
import java.io.Serializable;
import java.util.List;

/**
 *
 * @deprecated Se ha sustituido por DmsDao
 */
public interface GenericDAO<T, ID extends Serializable> {

    T findById(ID id);

    List<T> findByExample(T exampleInstance);

    T save(T entity);

    void delete(T entity);
}
