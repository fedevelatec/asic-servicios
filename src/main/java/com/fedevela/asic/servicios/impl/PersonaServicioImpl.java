package com.fedevela.asic.servicios.impl;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.core.asic.pojos.Personas;
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.asic.excepciones.ServicioException;
import com.fedevela.asic.servicios.PersonaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author fvilla
 */
@Service("personaServicio")
public class PersonaServicioImpl implements PersonaServicio {

    @Autowired
    private DmsDao dao;

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
}