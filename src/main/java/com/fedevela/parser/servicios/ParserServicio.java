package com.fedevela.parser.servicios;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.parser.pojos.Carga;
import com.fedevela.core.parser.pojos.CargaInconsistencia;
import java.util.List;

/**
 *
 * @author egutierrez
 */
public interface ParserServicio {
    public int borraAnteriores(String cargaId);
    public List<CargaInconsistencia> obtenerInconsistencias(String layout,String cargaId);
    public void guardarCarga(List<Carga> carga);
}
