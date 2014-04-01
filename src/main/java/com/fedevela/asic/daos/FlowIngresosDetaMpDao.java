package com.fedevela.asic.daos;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.core.asic.pojos.FlowIngresosDetaMp;

/**
 *
 * @deprecated Se ha sustituido por DmsDao
 */
public interface FlowIngresosDetaMpDao {


    public Integer getNumeroExpedientesPorCaja(Long idCaja);

    public Integer getNumeroDocumentosPorCaja(Long idCaja);

    public void guardaFlowIngresoDetaMp(FlowIngresosDetaMp detaMp);

    public void borraFlowIngresoDetaMpPorCajaUnicodoc(Long cajaId, Long nunicodoc);

    public void actualizaEstatusFlowIngresoDetaMpPorCaja(Long cajaId, Long aLong);


    public void actualizaEstatusFlowIngresoDetaMpPorCajas(Long cajaOrigen, Long cajaDestino, int status, Long nunicodoc);

    /**
     * Obtiene los expedientes de un recibo por estatus
     *
     * @param recibo
     * @param status
     * @return
     * @throws ServicioException
     */
    public int getExpedientesByStatus(Long recibo, Long status);

    /**
     * Obtiene todos los expedientes de un recibo
     *
     * @param recibo
     * @return
     * @throws ServicioException
     */
    public int getExpedientesByRecibo(Long recibo);
}
