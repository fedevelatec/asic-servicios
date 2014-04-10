package com.fedevela.mx.asic.security.services.impl;

/**
 * Created by fvelazquez on 10/04/14.
 */
import com.fedevela.core.security.pojos.AplicacionWebmx;
import com.fedevela.asic.daos.DmsDao;
import java.util.List;
import javax.annotation.Resource;
import com.fedevela.mx.asic.security.services.ApplicationService;
import net.codicentro.core.TypeCast;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private Logger logger = LoggerFactory.getLogger(ApplicationService.class);
//    @Resource
//    private SecurityDao dao;

    @Resource
    private DmsDao dao;

    @Override
    public List<AplicacionWebmx> getAplicacions() {
        return getAplicacions(null, null, null, null, null, null, null);
    }

    @Override
    public List<AplicacionWebmx> getAplicacions(
            final Long idaplicacion,
            final String nombre,
            final String descripcion,
            final Character status,
            final String login,
            final Integer page,
            final Integer size) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AplicacionWebmx.class);
        if (idaplicacion != null) {
            criteria.add(Restrictions.eq("idaplicacion", idaplicacion));
        }
        if (!TypeCast.isBlank(nombre)) {
            criteria.add(Restrictions.like("nombre", nombre, MatchMode.ANYWHERE).ignoreCase());
        }
        if (!TypeCast.isBlank(descripcion)) {
            criteria.add(Restrictions.like("descripcion", descripcion, MatchMode.ANYWHERE).ignoreCase());
        }
        if (status != null) {
            criteria.add(Restrictions.eq("status", status));
        }
        if (page == null || size == null) {
            return dao.find(criteria);
        } else {
            return dao.find(criteria, page, size);
        }
    }

    @Override
    public AplicacionWebmx saveApplication(AplicacionWebmx app) {
        return dao.persist(app);
    }

    @Override
    public AplicacionWebmx getApplication( Long idApp) {
        return dao.get( AplicacionWebmx.class, idApp );
    }

    @Override
    public void deleteAplicacion(Long id) throws Exception {
        AplicacionWebmx aw = dao.get( AplicacionWebmx.class, id );
        if ( aw != null ) {
            dao.delete( aw );
        } else {
            throw new Exception("El objeto \"Aplicacion\" con id=" + id + " no existe.");
        }
    }
}
