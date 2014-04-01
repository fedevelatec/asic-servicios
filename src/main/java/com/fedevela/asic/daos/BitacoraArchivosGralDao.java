package com.fedevela.asic.daos;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.core.asic.pojos.BitacoraArchivosGral;
import java.util.List;

/**
 *
 * @author fvilla
 * @deprecated Se ha sustituido por DmsDao
 */
public interface BitacoraArchivosGralDao {

    public List<BitacoraArchivosGral> getBitacoraArchivosGralPorClienteStatus(Long cliente, Long status);
}
