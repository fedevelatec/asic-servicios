package com.fedevela.asic.servicios;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.core.asic.pojos.Bloqueo;
import com.fedevela.asic.excepciones.ServicioException;
import java.util.List;

public interface BloqueoServicio {

    /**
     * Consulta si determinada etiqueta esta bloqueada por alguna aplicacion.
     *
     * @param etiqueta Etiqueta a consultar si esta bloqueada.
     * @return Objeto Bloqueo si la etiqueta consultada esta bloqueda o null si
     * no esta bloqueda.
     * @throws ServicioException
     */
    public Bloqueo getBloqueo(Long etiqueta) throws ServicioException;

    public void registrarBloqueo(Bloqueo bloqueo) throws ServicioException;

    public void eliminarBloqueo(Bloqueo bloqueo) throws ServicioException;

    public void eliminarBloqueo(List<Bloqueo> bloqueos) throws ServicioException;
}
