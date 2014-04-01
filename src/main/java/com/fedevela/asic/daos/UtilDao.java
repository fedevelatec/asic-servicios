package com.fedevela.asic.daos;

/**
 * Created by fvelazquez on 31/03/14.
 */
import java.util.Date;

/**
 * Interface con metodos utilitarios de consultas.
 *
 * @deprecated Se ha cambiado por el DmsDao
 */
public interface UtilDao {

    public Date getDate();

    public Long getNextVal(String secuencia);
}
