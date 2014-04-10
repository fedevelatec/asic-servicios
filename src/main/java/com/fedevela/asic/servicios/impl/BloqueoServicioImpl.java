package com.fedevela.asic.servicios.impl;

/**
 * Created by fvelazquez on 1/04/14.
 */
import com.fedevela.core.asic.pojos.Bloqueo;
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.asic.excepciones.ServicioException;
import com.fedevela.asic.servicios.BloqueoServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bloqueoServicio")
public class BloqueoServicioImpl implements BloqueoServicio {

    @Autowired
    private DmsDao dao;

    @Override
    public Bloqueo getBloqueo(Long etiqueta) throws ServicioException {
        return dao.get(Bloqueo.class, etiqueta);
    }

    @Override
    public void registrarBloqueo(Bloqueo bloqueo) throws ServicioException {
        dao.persist(bloqueo);
    }

    @Override
    public void eliminarBloqueo(Bloqueo bloqueo) throws ServicioException {
        dao.delete(bloqueo);
    }

    @Override
    public void eliminarBloqueo(List<Bloqueo> bloqueos) throws ServicioException {
        dao.delete( bloqueos.toArray() );
    }
}
