package com.fedevela.asic.servicios;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.core.asic.pojos.*;
import com.fedevela.asic.excepciones.ServicioException;
import com.fedevela.core.asic.beans.OperatoriaCfg;
import java.util.List;

/**
 *
 * @author fvilla
 */
public interface CapturaGeneralServicio {

    public void salvaCaptura(CapturaGeneralBean capturaGeneralBean) throws ServicioException;

    /**
     *
     * @param capturaGeneralBean
     * @throws ServicioException
     * @deprecated Utilizar el método salvaCaptura
     */
    public void salvaDetalleSelloDocum(CapturaGeneralBean capturaGeneralBean) throws ServicioException;

    public void salvaCaptura(CapturaGeneralBean capturaGeneralBean, CdDatosGrales cdDatosGrales) throws ServicioException;

    public List<CabeceraDoc> getCabeceraDoc(final Long scltcod, final Long doccod);

    public List<CabeceraDoc> getCabeceraDoc(final Long scltcod, final Long[] doccod);

    public List<CabeceraDoc> getCabeceraDoc(final Long scltcod, final List<Long> nunicodoc) throws ServicioException;

    public CabeceraDoc getCabeceraDocByNunicoDoc(Long nunicoDoc) throws ServicioException;

    public CabeceraDoc getCabeceraDocByNroidentdoc(String nroidentdoc, Long scltcod) throws ServicioException;

    public Personas getPersonaByCodigo(Long codigo) throws ServicioException;

    public Integer getNumeroExpedientesCapturadosPorCaja(Long cajaId) throws ServicioException;

    public Integer getNumeroDocumentosCapturadosPorCaja(Long cajaId) throws ServicioException;

    public DetalleSelloDocum getDetalleSelloDocumPorPK(DetalleSelloDocumPK pk) throws ServicioException;

    public DetalleSelloDocum getDetalleSelloDocum(Long nunicodoct) throws ServicioException;

    public DetalleSelloDocum saveDetalleSelloDocum(DetalleSelloDocum dsd) throws ServicioException;

    /**
     * Obtiene un EtiqDocumHn para un determinado cliente. Si lo encuentra
     * cambia el estatus del documento a ELP. <b>No usar si se requiere solo
     * consultar un EtiqDocumHn sin cambiar el estatus.</b>
     *
     * @param nunicodoc, Etiqueta de documento(T).
     * @param scltcod, Numero del cliente.
     * @return EtiqDocumHn o null en caso de no encontrarlo.
     * @throws ServicioException en caso de cualquier error.
     */
    public EtiqDocumHn getEtiqDocumHnPorCliente(Long nunicodoc, Short scltcod) throws ServicioException;

    /**
     * Obtiene un EtiqDocumHn para un determinado cliente. Si
     * estadoUbicacion==null entonces solo se retorna un EtiqDocumHn sin
     * cambiarle su estado
     *
     * @param nunicodoc, Etiqueta de documento(T).
     * @param scltcod, Numero del cliente.
     * @param estadoUbicacion
     * @return
     * @throws ServicioException
     */
    public EtiqDocumHn getEtiqDocumHnPorCliente(final Long nunicodoc, final Short scltcod, final String estadoUbicacion) throws ServicioException;

    /**
     * Obtiene un etiqdocumHN por nunicodoct sin hacerle nada
     *
     * @param nunicodoc
     * @param scltcod
     * @return
     * @throws ServicioException
     */
    public boolean getEtiqDocumHnByNunicodocT(final Long nunicodoc, final Short scltcod) throws ServicioException;

    public CabeceraDocCte1Ph getCabeceraDocCte1PhPorId(CabeceraDocCte1PhId id) throws ServicioException;

    public CabeceraDocCte1Ph getCabeceraDocCte1PhPorNunicoDocT(Long nunicodoct) throws ServicioException;

    public List<CabeceraDocCte1Ph> getCabeceraDocCte1PhPorDescripcion(String descripcion, Long scltcod, int maxResult) throws ServicioException;

    public Integer getSiguienteDocCodPorNunicodoc(Long nunicodoc) throws ServicioException;
    //Retorna el numero de expedientes que tienen asociado un credito

    public Integer getCabeceraDocCredito(String nroidentdoc, Long scltcod) throws ServicioException;

    /**
     * regresa un detalle sello buscado por el nroidentdoc
     *
     * @param nroidentdoc
     * @return Objeto DetalleSello si encuentra una relacion
     * @throws ServicioException
     */
    public DetalleSello getDetalleSelloByNroidentdoc(String nroidentdoc, Long scltcod) throws ServicioException;

    public DetalleSello getDetalleSelloById(final Long nunicodoc) throws ServicioException;

    /**
     * Obtiene los expedientes que no se han capturado de una determinada caja.
     *
     * @param cajaId caja a buscar.
     * @return Lista con los expedientes que no se han capturado.
     * @throws ServicioException
     */
    public List<String> getExpedientesNoCapturadosPorCaja(Long cajaId) throws ServicioException;

    public List<String> getDocumentosNoCapturadosPorCaja(Long cajaId) throws ServicioException;

    public boolean existNroidentdoc(String nroidentdoc, Long scltcod);

    /**
     * Obtiene lista de entidades federativas.
     *
     * @return lista de entidades federativas.
     */
    public List<CatEntidadFed> getEntidadesFederativas();

    /**
     *
     * @param cabeceraDoc
     */
    public CabeceraDoc saveCabeceraDoc(CabeceraDoc cabeceraDoc) throws ServicioException;

    public int deleteCabeceraDocCte1PhByUnicodoc(Long nunicodoc);

    public int deleteCabeceraDocCte1PhByNunicodoct(Long nunicodoct);

    public DetalleSello getDetalleSelloByCaja(Long caja) throws Exception;

    public DetalleSelloDocum getDetalleSelloDocumByCaja(Long caja) throws Exception;

    public List<DetalleSello> getDetallesSelloByCaja(Long caja) throws Exception;

    public ChecklistCap getChecklistCap(final Long nunicodoct);

    /**
     *
     * @param nunicodoc
     * @param doccod
     * @return
     * @throws En caso de que exista una U con mas de doccoc capturado.
     */
    public ChecklistCap getChecklistCap(final Long nunicodoc, final Short doccod) throws ServicioException;

    public List<ChecklistCap> getChecklistCap(final Integer scltcod, final Short[] doccod);

    public List<ChecklistCap> getChecklistCap(final Integer scltcod, final List<Long> nunicodocs);

    public List<ChecklistCap> getChecklistCaps(final Long nunicodoc);

    public List<ChecklistCap> getChecklistCaps(final Long nunicodoc, final String caracteristica);

    public List<ChecklistCap> getChecklistCaps(final Long nunicodoc, final Integer doccod);

    public ChecklistCap saveChecklistCap(ChecklistCap checklistCap);

    public void saveChecklistCap(List<ChecklistCap> checklistCaps);

    public void deleteChecklistCap(final Long nunicodoc);

    public void deleteChecklistCap(final Long nunicodoc, final Integer doccod);

    public void deleteChecklistCap(final Short doccod, final Long nunicodoct);

    public void deleteChecklistCap(ChecklistCap cap);

    public DetalleSello saveDetalleSello(DetalleSello detalleSello);

    public VwEtiqueta validarEtiqueta(final String etiqueta, final Character tipo, OperatoriaCfg cfg) throws ServicioException;

    public VwEtiqueta validarEtiqueta(final String etiqueta, final Character tipo, OperatoriaCfg cfg, final Long cajaId) throws ServicioException;

    public CdDatosGrales getCdDatosGrales(final Long nunicodoc);
}
