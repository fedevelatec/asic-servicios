package com.fedevela.asic.daos;

/**
 * Created by fvelazquez on 31/03/14.
 */
import java.util.List;

/**
 *
 * @deprecated Se ha sustituido por DmsDao
 */
public interface DetalleSelloDao {

    @Deprecated
    public List<String> getDocumentosNoCapturadosPorCaja(Long cajaId);
}
