package com.fedevela.parser.servicios.impl;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.parser.pojos.Carga;
import com.fedevela.core.parser.pojos.CargaInconsistencia;
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.parser.daos.ParserDao;
import com.fedevela.parser.servicios.ParserServicio;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("parserServicio")
public class ParserServicioImpl implements ParserServicio {

    @Autowired
    private ParserDao parserDao;

    @Autowired
    private DmsDao genericDao;

    @Override
    public int borraAnteriores(String cargaId) {
        return parserDao.borraAnteriores(cargaId);
    }

    @Override
    public void guardarCarga(List<Carga> carga) {
        for (Carga c : carga) {
            genericDao.persist(c);
        }
    }

    @Override
    public List<CargaInconsistencia> obtenerInconsistencias(String layout,String cargaId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(CargaInconsistencia.class);
        criteria.add(Restrictions.eq("layout", layout));
        criteria.add(Restrictions.eq("cargaInconsistenciaPK.id", cargaId));

        return genericDao.find(criteria);
    }
}
