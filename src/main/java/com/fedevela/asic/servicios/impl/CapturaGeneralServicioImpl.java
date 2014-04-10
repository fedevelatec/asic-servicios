package com.fedevela.asic.servicios.impl;

/**
 * Created by fvelazquez on 1/04/14.
 */
import com.fedevela.core.asic.beans.OperatoriaCfg;
import com.fedevela.core.asic.pojos.*;
import com.fedevela.asic.daos.CabeceraDocCte1PhDao;
import com.fedevela.asic.daos.CabeceraDocDao;
import com.fedevela.asic.daos.DetalleSelloDao;
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.asic.excepciones.ServicioException;
import com.fedevela.asic.servicios.CapturaGeneralServicio;
import com.fedevela.asic.util.TypeCast;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.codicentro.utils.Scalar;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author fvilla
 */
@Service("capturaGeneralServicio")
public class CapturaGeneralServicioImpl implements CapturaGeneralServicio {

    private Logger log = org.slf4j.LoggerFactory.getLogger(CapturaGeneralServicioImpl.class);
    @Autowired
    private CabeceraDocDao cabeceraDocDao;
    @Autowired
    private DetalleSelloDao detalleSelloDao;
    @Autowired
    private CabeceraDocCte1PhDao cabeceraDocCte1PhDao;
    @Autowired
    private DmsDao dao;

    public CabeceraDocCte1PhDao getCabeceraDocCte1PhDao() {
        return cabeceraDocCte1PhDao;
    }

    public void setCabeceraDocCte1PhDao(CabeceraDocCte1PhDao cabeceraDocCte1PhDao) {
        this.cabeceraDocCte1PhDao = cabeceraDocCte1PhDao;
    }

    public DetalleSelloDao getDetalleSelloDao() {
        return detalleSelloDao;
    }

    public void setDetalleSelloDao(DetalleSelloDao detalleSelloDao) {
        this.detalleSelloDao = detalleSelloDao;
    }

    public CabeceraDocDao getCabeceraDocDao() {
        return cabeceraDocDao;
    }

    public void setCabeceraDocDao(CabeceraDocDao cabeceraDocDao) {
        this.cabeceraDocDao = cabeceraDocDao;
    }

    @Override
    public List<CabeceraDoc> getCabeceraDoc(final Long scltcod, final Long doccod) {
        DetachedCriteria criteria = DetachedCriteria.forClass(CabeceraDoc.class);
        criteria.add(Restrictions.eq("scltcod", scltcod));
        criteria.add(Restrictions.eq("doccod", doccod));
        return dao.find(criteria);
    }

    @Override
    public List<CabeceraDoc> getCabeceraDoc(Long scltcod, Long[] doccod) {
        DetachedCriteria criteria = DetachedCriteria.forClass(CabeceraDoc.class);
        criteria.add(Restrictions.eq("scltcod", scltcod));
        criteria.add(Restrictions.in("doccod", doccod));
        return dao.find(criteria);
    }

    @Override
    public List<CabeceraDoc> getCabeceraDoc(final Long scltcod, final List<Long> nunicodocs) throws ServicioException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT CD.*");
            sql.append(" FROM PROD.CABECERA_DOC CD");
            sql.append(" WHERE    SCLTCOD=").append(scltcod);
            sql.append("      AND NUNICODOC IN(").append(TypeCast.toString(nunicodocs)).append(")");
            return dao.find(CabeceraDoc.class, sql);
        } catch (Exception e) {
            throw new ServicioException(e);
        }
    }

    @Override
    public void salvaCaptura(CapturaGeneralBean cgb) throws ServicioException {
        log.info("salvando: " + ToStringBuilder.reflectionToString(cgb));
        try {
            //GUARDA EN LA TABLA PERSONAS SI SE ASIGNA AL BEAN CapturaGeneralBean
            if (cgb.getPersonas() != null) {
                cgb.setPersonas(dao.persist(cgb.getPersonas()));
                cgb.getCabeceraDoc().setTitular(new Long(cgb.getPersonas().getCodigo()));
            }
            if (cgb.getCabeceraDoc() != null) {
                dao.persist(cgb.getCabeceraDoc());
            }
            if (cgb.getDetalleSello() != null) {
                dao.persist(cgb.getDetalleSello());
            }
            if (cgb.getListDetalleSelloDocums() != null && !cgb.getListDetalleSelloDocums().isEmpty()) {
                dao.persist(cgb.getListDetalleSelloDocums());
            }
        } catch (Exception e) {
            throw new ServicioException(e);
        }
    }

    @Override
    public DetalleSelloDocum saveDetalleSelloDocum(DetalleSelloDocum dsd) throws ServicioException {
        return dao.persist(dsd);
    }

    @Override
    public void salvaDetalleSelloDocum(CapturaGeneralBean capturaGeneralBean) throws ServicioException {
        try {
            if (capturaGeneralBean.getListDetalleSelloDocums() != null && !capturaGeneralBean.getListDetalleSelloDocums().isEmpty()) {
                for (DetalleSelloDocum object : capturaGeneralBean.getListDetalleSelloDocums()) {
                    log.info("NUNICODOC: " + object.getDetalleSelloDocumPK().getNunicodoc());
                    log.info("NUNICODOC-T: " + object.getDetalleSelloDocumPK().getNunicodoct());
                    log.info("NUNICOSELLO: " + object.getDetalleSelloDocumPK().getNunicosello());
                    dao.persist(object);
                }
            }
        } catch (Exception e) {
            throw new ServicioException(e);
        }
    }

    @Override
    public void salvaCaptura(CapturaGeneralBean capturaGeneralBean, CdDatosGrales cdDatosGrales) throws ServicioException {
        log.info("salvando: " + ToStringBuilder.reflectionToString(capturaGeneralBean));
        try {
            //GUARDA EN LA TABLA PERSONAS SI SE ASIGNA AL BEAN CapturaGeneralBean
            if (capturaGeneralBean.getPersonas() != null) {
                capturaGeneralBean.setPersonas(dao.persist(capturaGeneralBean.getPersonas()));
                capturaGeneralBean.getCabeceraDoc().setTitular(new Long(capturaGeneralBean.getPersonas().getCodigo()));
            }

            cdDatosGrales.setCabeceraDoc(capturaGeneralBean.getCabeceraDoc());
            capturaGeneralBean.getCabeceraDoc().setCdDatosGrales(cdDatosGrales);
            dao.persist(capturaGeneralBean.getCabeceraDoc());
            dao.persist(capturaGeneralBean.getDetalleSello());
            if ((capturaGeneralBean.getListDetalleSelloDocums() != null) && (!capturaGeneralBean.getListDetalleSelloDocums().isEmpty())) {
                for (DetalleSelloDocum object : capturaGeneralBean.getListDetalleSelloDocums()) {
                    log.info("NUNICODOC: " + object.getDetalleSelloDocumPK().getNunicodoc());
                    log.info("NUNICODOC-T: " + object.getDetalleSelloDocumPK().getNunicodoct());
                    log.info("NUNICOSELLO: " + object.getDetalleSelloDocumPK().getNunicosello());
                    dao.persist(object);
                }
            }
        } catch (Exception e) {
            throw new ServicioException(e);
        }
    }

    @Override
    public CabeceraDoc getCabeceraDocByNunicoDoc(Long nunicoDoc) throws ServicioException {
        CabeceraDoc c = null;
        try {
            c = dao.get(CabeceraDoc.class, nunicoDoc);
        } catch (Exception e) {
            throw new ServicioException(e);
        }
        return c;
    }

    @Override
    public Personas getPersonaByCodigo(Long codigo) throws ServicioException {
        Personas p = null;
        try {
            p = dao.get(Personas.class, codigo);
        } catch (Exception e) {
            throw new ServicioException(e);
        }
        return p;
    }

    @Override
    public Integer getNumeroExpedientesCapturadosPorCaja(Long cajaId) throws ServicioException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) TOTAL FROM PROD.CABECERA_DOC WHERE NUNICODOC IN(SELECT NUNICODOC FROM PROD.FLOW_INGRESOS_DETA_MP WHERE CAJA_ID = ? AND NUNICODOC <> 0)");
        List<?> rs = dao.find(sql, new Object[]{cajaId}, new Scalar[]{new Scalar("TOTAL", StandardBasicTypes.INTEGER)});
        if (rs == null || rs.isEmpty()) {
            return null;
        } else {
            return TypeCast.toInteger(rs.get(0));
        }
    }

    @Override
    public Integer getNumeroDocumentosCapturadosPorCaja(Long cajaId) throws ServicioException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) TOTAL FROM PROD.DETALLE_SELLO_DOCUM WHERE NUNICOSELLO = ?");
        List<?> rs = dao.find(sql, new Object[]{cajaId}, new Scalar[]{new Scalar("TOTAL", StandardBasicTypes.INTEGER)});
        if (rs == null || rs.isEmpty()) {
            return null;
        } else {
            return TypeCast.toInteger(rs.get(0));
        }
    }

    @Override
    public List<String> getExpedientesNoCapturadosPorCaja(Long cajaId) throws ServicioException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT 'U'||strcomplete(NUNICODOC,'0',10,'L') NUNICODOC FROM PROD.FLOW_INGRESOS_DETA_MP WHERE CAJA_ID = ? AND NUNICODOC <> 0");
        sql.append(" MINUS ");
        sql.append("SELECT 'U'||strcomplete(NUNICODOC,'0',10,'L') FROM PROD.CABECERA_DOC WHERE NUNICODOC IN(SELECT NUNICODOC FROM PROD.FLOW_INGRESOS_DETA_MP WHERE CAJA_ID = ? AND NUNICODOC <> 0)");
        List<String> rs = (List<String>) dao.find(sql, new Object[]{cajaId, cajaId}, new Scalar[]{new Scalar("NUNICODOC", StandardBasicTypes.STRING)});
        if (rs == null || rs.isEmpty()) {
            return null;
        } else {
            return rs;
        }
    }

    @Override
    public List<String> getDocumentosNoCapturadosPorCaja(Long cajaId) throws ServicioException {
        return detalleSelloDao.getDocumentosNoCapturadosPorCaja(cajaId);
    }

    @Override
    public DetalleSelloDocum getDetalleSelloDocumPorPK(DetalleSelloDocumPK pk) throws ServicioException {
        if (pk.getNunicodoc() == null || pk.getNunicodoct() == null || pk.getNunicosello() == null) {
            throw new ServicioException("Se esta tratando de buscar llaves nulas: " + ToStringBuilder.reflectionToString(pk));
        }
        return dao.get(DetalleSelloDocum.class, pk);
    }

    @Override
    public DetalleSelloDocum getDetalleSelloDocum(Long nunicodoct) throws ServicioException {
        DetachedCriteria criteria = DetachedCriteria.forClass(DetalleSelloDocum.class);
        criteria.add(Restrictions.eq("detalleSelloDocumPK.nunicodoct", nunicodoct));
        List<DetalleSelloDocum> rs = dao.find(criteria);
        if ((rs != null) && (!rs.isEmpty())) {
            return rs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public EtiqDocumHn getEtiqDocumHnPorCliente(Long nunicodoc, Short scltcod) throws ServicioException {
        try {
            EtiqDocumHn result = dao.get(EtiqDocumHn.class, nunicodoc);

            if (result != null && scltcod == result.getScltcod() && !StringUtils.equalsIgnoreCase(result.getEstadoUbicacion(), "ELP")) {
                log.info("cambiando a ELP: " + result.toString());
                if (!StringUtils.equalsIgnoreCase(result.getEstadoUbicacion(), "PLP")
                        && StringUtils.isNotEmpty(result.getEstadoUbicacion())) {
                    throw new ServicioException("Estatus de documento invalido: " + result.getEstadoUbicacion());
                }
                //result.setEstadoUbicacion("ELP");
                //dao.persist(result);
                return result;
            } else {
                return result;
            }
        } catch (Exception e) {
            throw new ServicioException(e);
        }
    }

    @Override
    public EtiqDocumHn getEtiqDocumHnPorCliente(Long nunicodoc, Short scltcod, String estadoUbicacion) throws ServicioException {
        try {
            EtiqDocumHn result = dao.get(EtiqDocumHn.class, nunicodoc);
            if ((result != null) && (scltcod == result.getScltcod())) {

                if (estadoUbicacion != null) {
                    log.info("cambiando a " + estadoUbicacion + ": " + result.toString());
                    result.setEstadoUbicacion(estadoUbicacion);
                    dao.persist(result);
                }

                return result;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new ServicioException(e);
        }
    }

    @Override
    public CabeceraDocCte1Ph getCabeceraDocCte1PhPorId(CabeceraDocCte1PhId id) throws ServicioException {
        try {
            return dao.get(CabeceraDocCte1Ph.class, id);
        } catch (Exception e) {
            throw new ServicioException(e);
        }
    }

    @Override
    public CabeceraDocCte1Ph getCabeceraDocCte1PhPorNunicoDocT(Long nunicodoct) throws ServicioException {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(CabeceraDocCte1Ph.class);
            criteria.add(Restrictions.eq("nunicodoct", nunicodoct));
            List<CabeceraDocCte1Ph> listCabecera = dao.find(criteria);
            if (listCabecera.isEmpty()) {
                return null;
            } else {
                //Regresa el primero encontrado. No deberia de haber mas de uno.
                //TODO revisar si vale la pena hacer una validacion aqui.
                return listCabecera.get(0);
            }
        } catch (Exception e) {
            throw new ServicioException(e);
        }
    }

    @Override
    public Integer getSiguienteDocCodPorNunicodoc(Long nunicodoc) throws ServicioException {
        Integer i = null;
        try {
            i = cabeceraDocCte1PhDao.getSiguienteDocCodPorNunicodoc(nunicodoc);
        } catch (Exception e) {
            throw new ServicioException(e);
        }
        return i;
    }

    @Override
    public Integer getCabeceraDocCredito(String nroidentdoc, Long scltcod) throws ServicioException {
        Integer i = null;
        try {
            i = cabeceraDocDao.getCabeceraDocCredito(nroidentdoc, scltcod);
        } catch (Exception e) {
            throw new ServicioException(e);
        }
        return i;
    }

    @Override
    public DetalleSello getDetalleSelloByNroidentdoc(String nroidentdoc, Long scltcod) throws ServicioException {
        List<CabeceraDoc> list = cabeceraDocDao.getCabeceraDocByNroidentdoc(nroidentdoc, scltcod);
        if (list == null || list.isEmpty()) {
            return null;
        }
        if (list.size() > 1) {
            throw new ServicioException("Esperado uno, encontrados: " + list.size());
        }
        CabeceraDoc c = list.get(0);
        return dao.get(DetalleSello.class, c.getNunicodoc());
    }

    @Override
    public CabeceraDoc getCabeceraDocByNroidentdoc(String nroidentdoc, Long scltcod) throws ServicioException {
        List<CabeceraDoc> list = cabeceraDocDao.getCabeceraDocByNroidentdoc(nroidentdoc, scltcod);
        if (list == null || list.isEmpty()) {
            return null;
        }
        if (list.size() > 1) {
            throw new ServicioException("Esperado uno, encontrados: " + list.size());
        }
        CabeceraDoc c = list.get(0);
        return c;
    }

    @Override
    public List<CabeceraDocCte1Ph> getCabeceraDocCte1PhPorDescripcion(String descripcion, Long scltcod, int maxResult) throws ServicioException {
        return cabeceraDocCte1PhDao.getByDescLike(descripcion, scltcod, maxResult);
    }

    @Override
    public boolean existNroidentdoc(String nroidentdoc, Long scltcod) {
        boolean bandera = true;
        List<CabeceraDoc> list = cabeceraDocDao.getCabeceraDocByNroidentdoc(nroidentdoc, scltcod);

        if (list != null) {
            if (list.size() > 0) {
                bandera = true;
            } else {
                bandera = false;
            }
        } else {
            bandera = false;
        }

        return bandera;
    }

    @Override
    public List<CatEntidadFed> getEntidadesFederativas() {
        return dao.find("from CatEntidadFed s");
    }

    @Override
    public DetalleSello getDetalleSelloById(Long nunicodoc) throws ServicioException {
        return dao.get(DetalleSello.class, nunicodoc);
    }

    @Override
    public CabeceraDoc saveCabeceraDoc(CabeceraDoc cabeceraDoc) throws ServicioException {
        return dao.persist(cabeceraDoc);
    }

    @Override
    public int deleteCabeceraDocCte1PhByUnicodoc(Long nunicodoc) {
        return cabeceraDocCte1PhDao.deleteByUnicodoc(nunicodoc);
    }

    @Override
    public int deleteCabeceraDocCte1PhByNunicodoct(Long nunicodoct) {
        return cabeceraDocCte1PhDao.deleteByNunicodoct(nunicodoct);
    }

    @Override
    public boolean getEtiqDocumHnByNunicodocT(Long nunicodoc, Short scltcod) throws ServicioException {
        boolean bandera = false;
        try {
            EtiqDocumHn result = dao.get(EtiqDocumHn.class, nunicodoc);
            if ((result != null) && (scltcod == result.getScltcod())) {
                bandera = true;
            }
        } catch (Exception e) {
            throw new ServicioException(e);
        } finally {
            return bandera;
        }
    }

    public void guardarBaCapturaAurora(Object object) {
        dao.persist(object);
    }

    @Override
    public DetalleSello getDetalleSelloByCaja(Long caja) throws Exception {
        DetachedCriteria criteria = DetachedCriteria.forClass(DetalleSello.class);
        criteria.add(Restrictions.eq("nunicosello", caja));
        List<DetalleSello> cls = dao.find(criteria);
        if (cls != null && cls.size() > 0) {
            return cls.get(0);
        }
        return null;
    }

    @Override
    public DetalleSelloDocum getDetalleSelloDocumByCaja(Long caja) throws Exception {
        DetachedCriteria criteria = DetachedCriteria.forClass(DetalleSelloDocum.class);
        criteria.add(Restrictions.eq("detalleSelloDocumPK.nunicosello", caja));
        List<DetalleSelloDocum> cls = dao.find(criteria);
        if (cls != null && cls.size() > 0) {
            return cls.get(0);
        }
        return null;
    }

    @Override
    public List<DetalleSello> getDetallesSelloByCaja(Long caja) throws Exception {
        return dao.find("from DetalleSello d WHERE d.nunicosello=?", new Object[]{caja});
    }

    @Override
    public ChecklistCap getChecklistCap(final Long nunicodoct) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ChecklistCap.class);
        criteria.add(Restrictions.eq("checklistCapPK.nunicodoct", nunicodoct));
        List<ChecklistCap> cls = dao.find(criteria);
        if (cls != null && cls.size() > 0) {
            return cls.get(0);
        }
        return null;
    }

    @Override
    public ChecklistCap getChecklistCap(final Long nunicodoc, final Short doccod) throws ServicioException {
        DetachedCriteria criteria = DetachedCriteria.forClass(ChecklistCap.class);
        criteria.add(Restrictions.eq("checklistCapPK.nunicodoc", nunicodoc));
        criteria.add(Restrictions.eq("checklistCapPK.doccod", doccod));
        List<ChecklistCap> cls = dao.find(criteria);
        if (cls == null || cls.isEmpty()) {
            return null;
        } else if (cls.size() == 1) {
            return cls.get(0);
        } else {
            throw new ServicioException("Existen mas de un documento de identificación oficial capturado, favor de validar.");
        }
    }

    @Override
    public List<ChecklistCap> getChecklistCaps(final Long nunicodoc) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ChecklistCap.class);
        criteria.add(Restrictions.eq("checklistCapPK.nunicodoc", nunicodoc));
        return dao.find(criteria);
    }

    @Override
    public List<ChecklistCap> getChecklistCap(Integer scltcod, Short[] doccod) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ChecklistCap.class);
        criteria.add(Restrictions.eq("scltcod", scltcod));
        criteria.add(Restrictions.in("checklistCapPK.doccod", doccod));
        return dao.find(criteria);
    }

    @Override
    public List<ChecklistCap> getChecklistCap(final Integer scltcod, List<Long> nunicodocs) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ChecklistCap.class);
        criteria.add(Restrictions.eq("scltcod", scltcod));
        criteria.add(Restrictions.in("checklistCapPK.nunicodoc", nunicodocs));
        return dao.find(criteria);
    }

    @Override
    public List<ChecklistCap> getChecklistCaps(final Long nunicodoc, final Integer doccod) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ChecklistCap.class);
        criteria.add(Restrictions.eq("checklistCapPK.nunicodoc", nunicodoc));
        criteria.add(Restrictions.eq("checklistCapPK.doccod", TypeCast.toShort(doccod)));
        return dao.find(criteria);
    }

    @Override
    public CdDatosGrales getCdDatosGrales(final Long nunicodoc) {
        return dao.get(CdDatosGrales.class, nunicodoc);
    }

    @Override
    public ChecklistCap saveChecklistCap(ChecklistCap checklistCap) {
        return dao.persist(checklistCap);
    }

    @Override
    public void saveChecklistCap(List<ChecklistCap> checklistCaps) {
        dao.persist(checklistCaps);
    }

    @Override
    public void deleteChecklistCap(Long nunicodoc) {
        List<ChecklistCap> checklistCaps = getChecklistCaps(nunicodoc);
        for (ChecklistCap checklistCap : checklistCaps) {
            dao.delete(checklistCap);
        }
    }

    @Override
    public void deleteChecklistCap(Long nunicodoc, Integer doccod) {
        List<ChecklistCap> checklistCaps = getChecklistCaps(nunicodoc, doccod);
        if ((checklistCaps != null) && (!checklistCaps.isEmpty()) && (checklistCaps.size() == 1)) {
            dao.delete(checklistCaps.get(0));
        }
    }

    @Override
    public List<ChecklistCap> getChecklistCaps(Long nunicodoc, String caracteristica) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ChecklistCap.class);
        criteria.add(Restrictions.eq("checklistCapPK.nunicodoc", nunicodoc));
        criteria.add(Restrictions.eq("caracteristica", caracteristica));
        return dao.find(criteria);
    }

    @Override
    public void deleteChecklistCap(final Short doccod, final Long nunicodoct) {
        ChecklistCap cap = getChecklistCap(nunicodoct);
        if (cap != null) {
            if (!cap.getDoccod().equals(doccod)) {
                throw new RuntimeException("No se pudo eliminar la etiqueta " + nunicodoct + " ya que existe capturada con un DOCCOD distinto a " + doccod + ".");
            }
            dao.delete(cap);
        } else {
            throw new RuntimeException("No se pudo eliminar la etiqueta " + nunicodoct + " ya que no existe capturada.");
        }
    }

    @Override
    public void deleteChecklistCap(ChecklistCap cap) {
        dao.delete(cap);
    }

    @Override
    public DetalleSello saveDetalleSello(DetalleSello detalleSello) {
        return dao.persist(detalleSello);
    }

    @Override
    public VwEtiqueta validarEtiqueta(final String etiqueta, final Character tipo, OperatoriaCfg cfg) throws ServicioException {
        return validarEtiqueta(etiqueta, tipo, cfg, null);
    }

    @Override
    public VwEtiqueta validarEtiqueta(final String etiqueta, final Character tipo, OperatoriaCfg cfg, final Long cajaId) throws ServicioException {
        /**
         * VALIDAR QUE LA ETIQUETA TENGA UN FORMATO VÁLIDO.
         */
        switch (tipo) {
            case 'S':
                if (!Pattern.matches("S\\d{8}", etiqueta)) {
                    throw new ServicioException("EL FORMATO DE LA ETIQUETA NO ES VALIDO.");
                }
                break;
            case 'U':
                if (!Pattern.matches("U\\d{10}", etiqueta)) {
                    throw new ServicioException("EL FORMATO DE LA ETIQUETA NO ES VALIDO.");
                }
                break;
            case 'T':
                if (!Pattern.matches("T\\d{11}", etiqueta)) {
                    throw new ServicioException("EL FORMATO DE LA ETIQUETA NO ES VALIDO.");
                }
                break;
            default:
                throw new ServicioException("EL FORMATO DE LA ETIQUETA NO ES VALIDO.");
        }
        VwEtiqueta etq = dao.get(VwEtiqueta.class, new VwEtiquetaPK(TypeCast.toLong(etiqueta.substring(1)), tipo));
        /**
         * VALIDAR EXISTENCIA.
         */
        if (etq == null) {
            throw new ServicioException("LA ETIQUETA " + etiqueta + " NO EXISTE.");
        }
        /**
         * VALIDAR QUE LA ETIQUETA SEA DEL CLIENTE SELECCIONADO.
         */
        if (!etq.getScltcod().equals(cfg.getScltcod())) {
            throw new ServicioException("LA ETIQUETA " + etiqueta + " NO PERTENECE AL CLIENTE " + cfg.getScltcod() + ".");
        }
        /**
         * VALIDAR INGRESO DE ETIQUETA SEGÚN LA CONFIGURACION.
         */
        if (!etq.getIngresada().equals('t')
                && ((etq.getVwEtiquetaPK().getTipo().equals('S') && cfg.getCapValidarIngresoS())
                || (etq.getVwEtiquetaPK().getTipo().equals('U') && cfg.getCapValidarIngresoU())
                || (etq.getVwEtiquetaPK().getTipo().equals('T') && cfg.getCapValidarIngresoT()))) {
            throw new ServicioException("LA ETIQUETA " + etiqueta + " NO SE ENCUENTRA INGRESADA.");
        }
        /**
         * VALIDAR QUE LA ETIQUETA SEA DE LA OPERATORIA.
         */
        if (!cfg.getCfgIngreso() || etq.getOperatoria() != null) {
            if ((etq.getOperatoria() == null || !etq.getOperatoria().equals(cfg.getIdOperatoria()))
                    && ((tipo.equals('S') && cfg.getCapValidarOperatoriaS())
                    || (tipo.equals('U') && cfg.getCapValidarOperatoriaU())
                    || (tipo.equals('T') && cfg.getCapValidarOperatoriaT()))) {
                throw new ServicioException("LA ETIQUETA " + etiqueta + " NO PERTENECE A LA OPERATORIA " + cfg.getIdOperatoria() + ".");
            }
        }
        /**
         * VALIDAR CAJA DE INGRESO DE ETIQUETA VS CAJA DE CAPTURA
         */
        if (cajaId != null && !etq.getNunicosello().equals(cajaId)) {
            throw new ServicioException("LA ETIQUETA " + etiqueta + " NO SE INGRESO EN LA CAJA " + com.fedevela.asic.util.TypeCast.SF(cajaId) + ".");
        }
        /**
         * VALIDAMOS QUE LA ETIQUETA SEA DE TIPO U Y ESTE CAPTURADA SIEMPRE Y
         * CUANDO ESTE CONTEMPLADA LA VALIDACION EN CFG.
         */
        if (etq.getVwEtiquetaPK().getTipo().equals('U') && cfg.getCapValidarCapturaU() && etq.getCapturada().equals('t')) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("ETIQUETA", etq);
            result.put("CABECERA_DOC", getCabeceraDocByNunicoDoc(etq.getNunicodoc()));
            throw new ServicioException("LA ETIQUETA " + etiqueta + " SE ENCUENTRA CAPTURADA.", result);
        }
        return etq;
    }
}
