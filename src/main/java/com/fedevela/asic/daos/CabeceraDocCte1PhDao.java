package com.fedevela.asic.daos;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.core.asic.pojos.CabeceraDocCte1Ph;
import com.fedevela.core.asic.pojos.CabeceraDocCte1PhId;
import java.util.List;

/**
 *
 * @author fvilla
 * @deprecated Se ha sustituido por DmsDao
 */
public interface CabeceraDocCte1PhDao extends GenericDAO<CabeceraDocCte1Ph, CabeceraDocCte1PhId> {

    public CabeceraDocCte1Ph get(CabeceraDocCte1PhId id);

    public List<CabeceraDocCte1Ph> buscaPorNunicoDocT(Long nunicodoct);

    public Integer getSiguienteDocCodPorNunicodoc(Long nunicodoc);

    public List getDocCodDisponiblePorNunicodoc(Long nunicodoc, int scltcod);

    public List<CabeceraDocCte1Ph> getByDescLike(String descripcion, Long scltcod, int maxResults);

    public CabeceraDocCte1Ph findByNunicodoct(Long nunicodoct, Long nunicodoc, short doccod);

    public int deleteByUnicodoc(Long nunicodoc);

    public int deleteByNunicodoct(Long nunicodoct);
}
