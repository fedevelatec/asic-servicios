package com.fedevela.asic.servicios;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.core.asic.beans.OperatoriaCfg;
import com.fedevela.core.asic.controlcalidad.beans.TipoDocumentoCfg;
import com.fedevela.core.asic.controlcalidad.pojos.ControlCalidad;
import com.fedevela.core.asic.pojos.*;
import com.fedevela.asic.excepciones.ServicioException;
import java.util.Date;
import java.util.List;

/**
 * Metodos para realizar las tareas de registro para cualquier expediente.
 *
 * @author Francisco Villa Ramos
 */
public interface RegistroGeneralServicio {

    public Caja getCaja(Long idCaja);

    /**
     *
     * @param idCaja
     * @return
     * @deprecated
     */
    public Caja getCaja(Integer idCaja);

    public Caja saveCaja(Caja caja);

    public Date getFechaActual();

    /**
     *
     * @param nombeSecuencia
     * @return
     * @throws ServicioException
     */
    public Long getSiguienteValor(final String nombeSecuencia) throws ServicioException;

    /**
     * Obtiene una entidad EtiqDocum. Si lo encuentra lo cambia a estado ELP =
     * EN LINEA DE PROCESO. <b>No usar este método si solo se quiere obtener un
     * EtiqDocum sin cambiarlo de estado<b>.
     *
     * @param nUnicoDoc Llave del EtiqDocum buscado
     * @return EtiqDocum encontrado para un nUnicoDoc o null en caso de no
     * encontrarlo.
     * @throws ServicioException en caso de cualquier error de base de datos.
     */
    public EtiqDocum getEtiqDocum(Long nUnicoDoc) throws ServicioException;

    /**
     * Obtiene una entidad EtiqDocum. Si estadoUbicacion==null entonces solo se
     * retorna un EtiqDocum sin cambiarle su estado de ubicacion.
     *
     * @param nUnicoDoc
     * @param estadoUbicacion
     * @return
     * @throws ServicioException
     */
    public EtiqDocum getEtiqDocum(final Long nUnicoDoc, final String estadoUbicacion) throws ServicioException;

    /**
     * Obtiene una entidad EtiqDocumHn. Si estadoUbicacion==null entonces solo
     * se retorna un EtiqDocumHn sin cambiarle su estado de ubicacion.
     *
     * @param nunicodoct
     * @param estadoUbicacion
     * @return
     * @throws ServicioException
     */
    public EtiqDocumHn getEtiqDocumHn(final Long nunicodoct, final String estadoUbicacion) throws ServicioException;

    public EtiqDocumHn getEtiqDocumHn(final Long nunicodoct) throws ServicioException;

    public List<Sucursal> getSucursalesPorCliente(Long numCliente) throws ServicioException;

    public List<TipoDocum> getTipoDocumPorClienteSucursal(final Long cliente, final Long sucursal);

    public List<FlowTrasvases> getTrasvasePorClienteCajadestino(short numCliente, Long idCaja) throws ServicioException;

    /**
     * Retorna el ultimo estatus en el que se econtraba un expediente antes del
     * estatus actual.
     *
     * @param nunicodoc
     * @return
     * @throws ServicioException
     */
    public VwUltimoEstatusExpediente getUltimoEstatusExpediente(Long nunicodoc) throws ServicioException;

    /**
     * Retorna el ultimo estatus en el que se econtraba un documento antes del
     * estatus actual.
     *
     * @param nunicodoct
     * @return
     * @throws ServicioException
     */
    public VwUltimoEstatusDocumento getUltimoEstatusDocumento(Long nunicodoct) throws ServicioException;

    /**
     *
     * @param logMovimiento
     * @throws ServicioException
     */
    public void saveLogMovimientos(LogMovimientos logMovimiento) throws ServicioException;

    /**
     *
     * @param logMovimientos
     * @throws ServicioException
     */
    public void saveLogMovimientos(List<LogMovimientos> logMovimientos) throws ServicioException;

    /**
     *
     * @param scltcod
     * @param doccod
     * @return
     */
    public TipoDocumCte1Ph getTipoDocumCte1Ph(Long scltcod, Long doccod);

    public List<TipoDocumCte1Ph> getTipoDocumCte1Ph(final Long scltcod, final List<Short> doccods);

    /**
     * INTEGRACIONES DE EXPEDIENTES/DOCUMENTOS
     */
    /**
     *
     * @param nunicodoct
     * @return
     */
    public FovIntegraciones getIntegracionByNunicodoct(final long nunicodoct);

    /**
     * INTEGRACIONES DE EXPEDIENTES/DOCUMENTOS
     */
    /**
     *
     * @param nunicodoc
     * @return
     */
    public FovIntegraciones getIntegracionByNunicodoc(final Long nunicodoc);

    /**
     *
     * @param fovIntegracion
     * @return
     */
    public FovIntegraciones saveIntegracion(FovIntegraciones fovIntegracion);

    /**
     *
     * @param scltcod
     * @return
     */
    public List<TipoDocum> getTipoDocum(long scltcod);

    public List<ChecklistCap> getChecklistCapByNunicodoct(Long nunicodoct);

    public List<ChecklistDig> getCheckListDigByNunicodoct(Long nunicodoct);

    public VwEtiqueta getVwEtiqueta(final Long etiqueta, final Character tipo);

    /**
     *
     * @param idOperatoria
     * @return
     * @throws ServicioException
     * @deprecated Utilizar método getCapturaCfg
     */
    public OperatoriaCfg getOperatoriaCfg(final Long idOperatoria) throws ServicioException;

    public OperatoriaCfg getCapturaCfg(final Long idOperatoria) throws ServicioException;

    public OperatoriaCfg getIngresoCfg(final Long idOperatoria) throws ServicioException;

    public TipoDocumentoCfg getControlCalidadImagenCfg(final Long idOperatoria) throws ServicioException;

    public List<String> getExpPendientesByCaja(Long caja, Long cliente);

    public EtiqDocum saveEtiqDocum(EtiqDocum etiqDocum);

    public ControlCalidad saveControlCalidad(ControlCalidad cc);
}
