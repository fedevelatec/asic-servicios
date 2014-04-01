package com.fedevela.asic.servicios.impl;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.core.asic.pojos.*;
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.asic.excepciones.ServicioException;
import com.fedevela.asic.servicios.DigitalizacionServicio;
import java.util.List;
import net.codicentro.core.TypeCast;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author fvilla
 */
@Service("digitalizacionServicio")
public class DigitalizacionServicioImpl implements DigitalizacionServicio {

    @Autowired
    private DmsDao dao;

    @Override
    public List<ChecklistDig> getChechlistByNunicodocCliente(Long nunicodoc, Long scltcod) throws ServicioException {
        String query = "from ChecklistDig cl where cl.checklistDigPK.nunicodoc = ? and cl.tipoDocumCte1Ph.tipoDocumCte1PhPK.scltcod = ?";
        return dao.find(query, new Object[]{nunicodoc, scltcod});
    }

    @Override
    public List<PaginaDig> getPaginaDigByUT(Long nunicodoc, Long nunicodoct) throws ServicioException {
        if (nunicodoc == null || nunicodoct == null) {
            throw new ServicioException("ERROR: Parametros nulos");
        }
        DetachedCriteria criteria = DetachedCriteria.forClass(PaginaDig.class);
        criteria.add(Restrictions.eq("paginaDigPK.nunicodoc", nunicodoc));
        criteria.add(Restrictions.eq("paginaDigPK.nunicodoct", nunicodoct));
        return dao.find(criteria);
    }

    @Override
    public PaginaDig getPaginaDigNunicodocNunicodocTPagina(Long nunicodoc, Long nunicodoct, Long pagina) throws ServicioException {
        return dao.get(PaginaDig.class, new PaginaDigPK(nunicodoc, nunicodoct, pagina));
    }

    @Override
    public ChecklistDig getChecklistDigById(Long nunicodoc, Long nunicodoct) throws ServicioException {
        if (nunicodoct != 0L) {
            ChecklistDigPK id = new ChecklistDigPK(nunicodoc, nunicodoct);
            return dao.get(ChecklistDig.class, id);
        } else {
            String query = "from ChecklistDig cl where cl.checklistDigPK.nunicodoc = ?";
            List<ChecklistDig> cls = dao.find(query, new Object[]{nunicodoc});
            if (cls != null && cls.size() > 0) {
                return cls.get(0);
            } else {
                return null;
            }
        }
    }

    @Override
    public Integer getNumPaginaDigByUT(Long nunicodoc, Long nunicodoct) throws ServicioException {
        List<?> rs = dao.find("select count(*) from PaginaDig where paginaDigPK.nunicodoc = ? and paginaDigPK.nunicodoct = ?", new Object[]{nunicodoc, nunicodoct});
        if (rs == null || rs.isEmpty()) {
            return null;
        } else {
            return TypeCast.toInteger(rs.get(0));
        }
    }

    @Override
    public ChecklistDig getChecklistDigByT(Long nunicodoct) throws ServicioException {
        DetachedCriteria criteria = DetachedCriteria.forClass(ChecklistDig.class);
        criteria.add(Restrictions.eq("checklistDigPK.nunicodoct", nunicodoct));
        List<ChecklistDig> rs = dao.find(criteria);
        if ((rs == null) || (rs.isEmpty())) {
            return null;
        } else {
            return rs.get(0);
        }
    }

    @Override
    public ChecklistDig getChecklistDigByU(Long nunicodoc) throws ServicioException {
        DetachedCriteria criteria = DetachedCriteria.forClass(ChecklistDig.class);
        criteria.add(Restrictions.eq("checklistDigPK.nunicodoc", nunicodoc));
        List<ChecklistDig> rs = dao.find(criteria);
        if ((rs == null) || (rs.isEmpty())) {
            return null;
        } else {
            return rs.get(0);
        }
    }

    @Override
    public List<VwDigitalizacion> getVwDigitalizacion(final Long etiqueta, final Character tipo) {
        DetachedCriteria criteria = DetachedCriteria.forClass(VwDigitalizacion.class);
        switch (tipo) {
            case 'T':
                criteria.add(Restrictions.eq("vwDigitalizacionPK.nunicodoct", etiqueta));
                break;
            default:
                criteria.add(Restrictions.eq("vwDigitalizacionPK.nunicodoc", etiqueta));
                break;
        }
        return dao.find(criteria);
    }
}
