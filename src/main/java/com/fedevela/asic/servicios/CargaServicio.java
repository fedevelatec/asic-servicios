package com.fedevela.asic.servicios;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.core.asic.pojos.BitacoraArchivosCliente;
import com.fedevela.asic.excepciones.ServicioException;
import com.fedevela.asic.excepciones.ServicioOkException;
import java.util.Date;
import java.util.List;

/**
 * Servicio de apoyo para manejar la carga de archivos.
 *
 * @author fvilla
 */
public interface CargaServicio {

    /**
     *
     * @param cliente
     * @param status
     * @return
     * @throws ServicioException
     */
    public List<BitacoraArchivosCliente> getBitacoraArchivosClientePorClienteStatus(Short cliente, Short status) throws ServicioException;

    public List<BitacoraArchivosCliente> getBitacoraArchivosCliente(StringBuilder sql) throws ServicioException;

    /**
     *
     * @param cliente
     * @param status
     * @param fechaProcesamiento
     * @return
     * @throws ServicioException
     */
    public List<BitacoraArchivosCliente> getBitacoraArchivosClienteBy(final Short cliente, final Short status, final Date fechaProcesamiento) throws ServicioException;

    /**
     * Este método regresa todos aquellos archivos que estan en: STATUS=4 AND
     * TOTAL_REGISTROS<>REGISTROS_CORRECTOS
     *
     * @param cliente
     * @param loadDate
     * @return
     * @throws ServicioException
     */
    public List<BitacoraArchivosCliente> getBitacoraArchivosClienteBy(final Short cliente, final Date loadDate) throws ServicioException;

    /**
     *
     * @param cliente
     * @param status
     * @param archivoDepositado
     * @return
     * @throws ServicioException
     */
    public List<BitacoraArchivosCliente> getBitacoraArchivosClienteBy(final Short cliente, final Short status, final String archivoDepositado) throws ServicioException;

    /**
     *
     * @param cliente
     * @param status
     * @param fechaProcesamiento
     * @param archivo
     * @return
     * @throws ServicioException
     */
    public List<BitacoraArchivosCliente> getBitacoraArchivosClienteBy(final Short cliente, final Short status, final Date fechaProcesamiento, final String archivo) throws ServicioException;

    /**
     *
     * @param bac
     * @throws ServicioException
     */
    public BitacoraArchivosCliente salvaBitacoraArchivo(BitacoraArchivosCliente bac) throws ServicioException;

    public BitacoraArchivosCliente salvaBitacoraArchivo(BitacoraArchivosCliente bac, boolean rollback) throws ServicioOkException;

    /**
     *
     * @param cliente
     * @return
     * @throws ServicioException
     */
    public List<BitacoraArchivosCliente> getBitacoraArchivosClientePorCliente(Short cliente) throws ServicioException;

    /**
     *
     * @param idArchivo
     * @return
     * @throws ServicioException
     */
    public BitacoraArchivosCliente getBitacoraArchivosClientePorIdArchivo(Long idArchivo) throws ServicioException;
}
