package com.fedevela.asic.servicios;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.core.asic.pojos.*;
import com.fedevela.asic.excepciones.ServicioException;
import java.util.List;

/**
 *
 * @author avillalobos
 */
public interface IngresoServicio {

    /**
     * Ingreso de documentos T
     *
     * @param nunicodoct
     * @return
     */
    public FlowIngresosDetaMpb getFlowIngresosDetaMpb(final Long nunicodoct);

    /**
     * Ingreso de expedientes U
     *
     * @param nunicodoc
     * @return
     * @deprecated No es que este deprecado, simplemente el POJO no está
     * anotado, usar por mientras
     * registroGeneralServicio.getFlowIngresosDetaPorNunicoDoc
     */
    public FlowIngresosDetaMp getFlowIngresosDetaMp(final Long nunicodoc);

    public FlowIngresosCabMp getFlowIngresosCabMp(final Long idrecibo);

    /**
     * Cierra una caja y agrega la informacion a mapeo para que la procese. Se
     * requiere para operatorias atipicas que no realizan ingreso
     *
     * @param cajaCerrada Pojo de la caja a cerrar.
     */
    public void cierraCaja(CajaCerrada cajaCerrada) throws ServicioException;

    /**
     * Obtiene los expedientes de un recibo por estatus
     *
     * @param recibo
     * @param status
     * @return
     * @throws ServicioException
     */
    public int getExpedientesByStatus(Long recibo, Long status) throws ServicioException;

    /**
     * Obtiene todos los expedientes de un recibo
     *
     * @param recibo
     * @return
     * @throws ServicioException
     */
    public int getExpedientesByRecibo(Long recibo) throws ServicioException;

    public void salvaCabIngreso(FlowIngresosCabMp cabMp) throws ServicioException;

    public FlowIngresosDetaMp getFlowIngresosDetaPorNunicoDoc(final Long nunicoDoc) throws ServicioException;

    public FlowIngresosDetaMpb getFlowIngresosDetaPorNunicoDoct(final Long nunicoDoct) throws ServicioException;

    public Integer getNumeroDocumentosPorCaja(final Long idCaja) throws ServicioException;

    public Integer getNumeroExpedientesPorCaja(final Long idCaja) throws ServicioException;

    public FlowOperatoria getOperatoriaPorCajaIngresada(Long idCaja) throws ServicioException;

    public FlowIngresosDetaMp getFlowIngresosDetaCabecera(Long cajaId);

    public Long getTotalExpedienteIngresadoCaja(final Long cajaId);

    //RVILLANUEVA
    public FlowOperatoria getOperatoriaPorExpedienteIngresado(Long nunicodoc) throws ServicioException;

    public Long save(List<?> ingresos);

    public void deleteFlowIngresosDetaMp(FlowIngresosDetaMp fidm);
}
