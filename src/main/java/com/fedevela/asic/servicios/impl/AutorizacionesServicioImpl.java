package com.fedevela.asic.servicios.impl;

/**
 * Created by fvelazquez on 1/04/14.
 */
import com.fedevela.core.hn.pojos.HnAutorizadores;
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.asic.excepciones.ServicioException;
import com.fedevela.asic.servicios.AutorizacionesServicio;
import com.fedevela.asic.util.TypeCast;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

/**
 *
 * @author egutierrez
 */
@Service("autorizacionesServicio")
public class AutorizacionesServicioImpl implements AutorizacionesServicio {

    @Resource
    private DmsDao dao;

    @Override
    public HnAutorizadores getHnAutorizadores(BigInteger numAutoriz) {
        DetachedCriteria criteria = DetachedCriteria.forClass(HnAutorizadores.class);
        criteria.add(Restrictions.eq("hnAutorizadoresPK.status", TypeCast.toShort(1)));
        criteria.add(Restrictions.eq("hnAutorizadoresPK.numAutoriz", numAutoriz));

        List<HnAutorizadores> rs = dao.find(criteria, 0, 1);
        if ((rs != null) && (!rs.isEmpty())) {
            return rs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public HnAutorizadores getAutorizacion(final BigInteger numAutoriz) throws ServicioException {
        DetachedCriteria criteria = DetachedCriteria.forClass(HnAutorizadores.class);
        criteria.add(Restrictions.eq("hnAutorizadoresPK.numAutoriz", numAutoriz));
        List<HnAutorizadores> rs = dao.find(criteria);
        if (rs == null || rs.isEmpty()) {
            throw new ServicioException("No existe la clave de autorización.");
        }
        if (rs.size() > 1) {
            throw new ServicioException("Ha ocurrido un error en la lógica de autorizadores, ya que la clave del autorizador tiene más de una coincidencia, favor de reportar a sistemas.");
        }
        HnAutorizadores ha = rs.get(0);
        if (ha.getHnAutorizadoresPK().getStatus() == 0) {
            throw new ServicioException("Clave de autorizador inactiva.");
        }
        if (TypeCast.isBlank(ha.getLogin())) {
            throw new ServicioException("La clave de autorización no está asociada a ningun usuario.");
        }
        return ha;
    }
}
