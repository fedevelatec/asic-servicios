package com.fedevela.asic.daos;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.core.asic.pojos.CabeceraDoc;
import java.util.List;

/**
 *
 * @deprecated Se ha sustituido por DmsDao
 */
public interface CabeceraDocDao {

    public void save(CabeceraDoc cabeceraDoc);

    public CabeceraDoc getCabeceraDocByNUnicoDoc(Long nunicoDoc);

    public Integer getCabeceraDocCredito(String nroidentdoc, Long scltcod);

    public List<CabeceraDoc> getCabeceraDocByNroidentdoc(String nroidentdoc, Long scltcod);
}
