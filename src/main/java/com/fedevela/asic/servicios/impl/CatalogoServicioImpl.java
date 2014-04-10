package com.fedevela.asic.servicios.impl;

/**
 * Created by fvelazquez on 1/04/14.
 */
import com.fedevela.core.asic.pojos.Clientes;
import com.fedevela.core.asic.pojos.FlowOperatoria;
import com.fedevela.core.asic.pojos.Personas;
import com.fedevela.core.asic.pojos.Sucursal;
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.asic.excepciones.ServicioException;
import com.fedevela.asic.servicios.CatalogoServicio;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author fvilla
 */
@Service("catalogoServicio")
public class CatalogoServicioImpl implements CatalogoServicio {

    @Autowired
    private DmsDao dao;

    /**
     *
     *
     */
    @Override
    public List<Clientes> getAllClientes() {
        return dao.find(Clientes.class);
    }

    //    @Override
//    public List<Clientes> getAllClientes() {
//        return dao.find("SELECT c FROM Clientes c");
//    }
    @Override
    public List<Clientes> getClientes() {
        return dao.find(Clientes.class);
    }

    @Override
    public List<Clientes> getClientes(final String nombre) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Clientes.class);
        criteria.add(Restrictions.like("acltrzsc", nombre, MatchMode.ANYWHERE).ignoreCase());
        return dao.find(criteria);
    }

    @Override
    public List<FlowOperatoria> getOperatorias(final Long scltcod) {
        DetachedCriteria criteria = DetachedCriteria.forClass(FlowOperatoria.class);
        criteria.add(Restrictions.eq("scltcod", scltcod));
        return dao.find(criteria);
    }

    @Override
    public List<Sucursal> getSucursal(final Long scltcod) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Sucursal.class);
        criteria.add(Restrictions.eq("sucursalPK.scltcod", scltcod));
        return dao.find(criteria);
    }

    @Override
    public Clientes getClienteByScltcod(Long scltcod) {
        Clientes clientes = dao.get(Clientes.class, scltcod);
        return clientes;
    }

    @Override
    public Personas getPersona(long codigo) throws ServicioException {
        Personas persona = null;
        try {
            persona = dao.get(Personas.class, codigo);
        } catch (Exception e) {
            throw new ServicioException(e);
        }
        return persona;
    }

    @Override
    public Personas getPersonaByDescription(String descripcion) throws ServicioException {
        try {
            StringBuilder sql = new StringBuilder("select * from prod.personas where descripcion = '").append(descripcion).append("'");
            List<Personas> personas = dao.find(Personas.class, sql);
            if (personas == null || personas.isEmpty()) {
                return null;
            } else {
                return personas.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServicioException(e);
        }
    }
}
