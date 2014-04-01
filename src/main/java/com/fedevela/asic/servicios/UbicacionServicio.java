package com.fedevela.asic.servicios;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.core.asic.pojos.Horizontalca;
import com.fedevela.asic.excepciones.ServicioException;

/**
 * Servicio para operaciones de ubicacion.
 *
 * @author fvilla
 */
public interface UbicacionServicio {

    /**
     * Obtiene la ubicación de una caja a partir de su numero unico de caja.
     *
     * @param cajaId el numero unico de caja a buscar.
     * @return la ubicacion Horizontalca ó null si no encuentra ubicacion.
     */
    public Horizontalca getUbicacionCaja(Integer cajaId) throws ServicioException;

    /**
     * Inserta un dato en el log de movimientos de documentos, expedientes y
     * cajas.
     *
     * @throws ServicioException
     */
    public void logMovimientos(int caja, long nunicodoc, long nunicodoct, String usuario,String maquina, String ip, String app, String modulo, String movimiento) throws ServicioException;

    public void logMovimientos(Long caja, long nunicodoc, long nunicodoct, String usuario,String maquina, String ip, String app, String modulo, String movimiento) throws ServicioException;
}
