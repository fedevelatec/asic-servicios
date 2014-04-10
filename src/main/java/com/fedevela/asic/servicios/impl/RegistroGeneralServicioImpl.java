package com.fedevela.asic.servicios.impl;

/**
 * Created by fvelazquez on 1/04/14.
 */
import com.fedevela.core.asic.definition.pojos.AdeamxDefinition;
import com.fedevela.core.asic.pojos.*;
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.asic.excepciones.ServicioException;
import com.fedevela.asic.servicios.RegistroGeneralServicio;
import com.fedevela.core.asic.beans.OperatoriaCfg;
import com.fedevela.core.asic.controlcalidad.beans.TipoDocumentoCfg;
import com.fedevela.core.asic.controlcalidad.pojos.ControlCalidad;
import com.fedevela.asic.util.TypeCast;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.codicentro.core.Utils;
import net.codicentro.utils.Scalar;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author fvilla
 */
@Service("registroGeneralServicio")
public class RegistroGeneralServicioImpl implements RegistroGeneralServicio {

    @Autowired
    private DmsDao dao;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(RegistroGeneralServicioImpl.class);

    @Override
    public Caja getCaja(Long idCaja) {
        return dao.get(Caja.class, idCaja);
    }

    @Override
    public Caja getCaja(Integer idCaja) {
        return dao.get(Caja.class, idCaja.longValue());
    }

    @Override
    public Caja saveCaja(Caja caja) {
        return dao.persist(caja);
    }

    @Override
    public Date getFechaActual() {
        List<?> d = dao.find(new StringBuilder("SELECT SYSDATE NOW FROM DUAL"), new Scalar[]{new Scalar("NOW", StandardBasicTypes.TIMESTAMP)});
        if (d == null || d.isEmpty()) {
            return null;
        } else {
            return (Date) d.get(0);
        }
    }

    @Override
    public Long getSiguienteValor(String nombeSecuencia) throws ServicioException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ").append(nombeSecuencia).append(".NEXTVAL ID FROM DUAL");
            List rs = dao.find(sql, new Scalar[]{new Scalar("ID", org.hibernate.type.StandardBasicTypes.LONG)});
            if (rs == null || rs.isEmpty()) {
                return null;
            } else {
                return TypeCast.toLong(rs.get(0));
            }
        } catch (Exception e) {
            throw new ServicioException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EtiqDocum getEtiqDocum(Long nUnicoDoc) throws ServicioException {
        if (nUnicoDoc == null) {
            throw new ServicioException("Parametro en NULL no permitido");
        }
        try {
            EtiqDocum etiqDocum = dao.get(EtiqDocum.class, nUnicoDoc);
            logger.debug("encontrado: " + etiqDocum);

            if (etiqDocum != null && !StringUtils.equalsIgnoreCase(etiqDocum.getEstadoUbicacion(), "ELP") && etiqDocum.getEstadoUbicacion() != null) {
                if (!StringUtils.equalsIgnoreCase(etiqDocum.getEstadoUbicacion(), "PLP")
                        && StringUtils.isNotEmpty(etiqDocum.getEstadoUbicacion())) {
                    throw new ServicioException("Estado de ubicación de expediente no válido: " + etiqDocum.getEstadoUbicacion());
                }

                logger.info("Cambiando valor a ELP");
                etiqDocum.setEstadoUbicacion("ELP");
                etiqDocum = dao.persist(etiqDocum);
            }
            //Estatu
            return etiqDocum;
        } catch (Exception e) {
            throw new ServicioException(e);
        }
    }

    /**
     *
     * @param nUnicoDoct
     * @return
     * @throws ServicioException
     */
    @Override
    public EtiqDocumHn getEtiqDocumHn(Long nUnicoDoct) throws ServicioException {
        if (nUnicoDoct == null) {
            throw new ServicioException("Parametro en NULL no permitido");
        }
        try {
            return dao.get(EtiqDocumHn.class, nUnicoDoct);
        } catch (Exception e) {
            throw new ServicioException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EtiqDocum getEtiqDocum(Long nUnicoDoc, String estadoUbicacion) throws ServicioException {
        if (nUnicoDoc == null) {
            throw new ServicioException("Parametro en NULL no permitido");
        }
        try {
            EtiqDocum etiqDocum = dao.get(EtiqDocum.class, nUnicoDoc);
            logger.debug("ETIQ_DOCUM: " + ((etiqDocum == null) ? "NULL" : etiqDocum.toString()));
            if ((etiqDocum != null) && (estadoUbicacion != null)) {
                logger.debug("Cambiando valor a " + estadoUbicacion);
                etiqDocum.setEstadoUbicacion(estadoUbicacion);
                etiqDocum = dao.persist(etiqDocum);
            }
            return etiqDocum;
        } catch (Exception e) {
            throw new ServicioException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EtiqDocumHn getEtiqDocumHn(Long nunicodoct, String estadoUbicacion) throws ServicioException {
        if (nunicodoct == null) {
            throw new ServicioException("Parametro en NULL no permitido");
        }
        try {
            EtiqDocumHn etiqDocumHn = dao.get(EtiqDocumHn.class, nunicodoct);
            logger.debug("ETIQ_DOCUM_HN: " + ((etiqDocumHn == null) ? "NULL" : etiqDocumHn.toString()));
            if ((etiqDocumHn != null) && (estadoUbicacion != null)) {
                logger.debug("Cambiando valor a " + estadoUbicacion);
                etiqDocumHn.setEstadoUbicacion(estadoUbicacion);
                etiqDocumHn = dao.persist(etiqDocumHn);
            }
            return etiqDocumHn;
        } catch (Exception e) {
            throw new ServicioException(e);
        }
    }

    @Override
    public List<Sucursal> getSucursalesPorCliente(Long numCliente)
            throws ServicioException {
        List<Sucursal> sucursalesByCliente;
        try {
            sucursalesByCliente = dao.find("from Sucursal s where s.sucursalPK.scltcod = ?", new Object[]{numCliente});
        } catch (EmptyResultDataAccessException e) {
            logger.warn("No existen sucursales para el cliente: " + numCliente);
            return new ArrayList<Sucursal>();
        } catch (Exception e) {
            logger.error("Ocurrio un error", e);
            throw new ServicioException(e);
        }
        return sucursalesByCliente;
    }

    @Override
    public List<TipoDocum> getTipoDocumPorClienteSucursal(final Long cliente, final Long sucursal) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT a.* FROM prod.tipo_docum a, prod.docum_cab b ");
        sql.append("WHERE a.scltcod = b.scltcod ");
        sql.append("AND a.doccod = b.doccod ");
        sql.append("AND b.scltcod=").append(cliente);
        sql.append("AND b.ssccod=").append(sucursal);
        List<TipoDocum> rs = dao.find(TipoDocum.class, sql);
        if (rs == null || rs.isEmpty()) {
            throw new RuntimeException("No existen tipos de documento para el cliente, sucursal: " + cliente + ", " + sucursal);
        }
        return rs;
    }

    @Override
    public List<FlowTrasvases> getTrasvasePorClienteCajadestino(short numCliente, Long idCaja) throws ServicioException {
        try {
            List<FlowTrasvases> flowTrasvases = dao.find("FROM FlowTrasvases f WHERE f.flowTrasvasesPK.scltcod = ? AND f.flowTrasvasesPK.cajaIdDestino = ? ", new Object[]{numCliente, idCaja});
            return flowTrasvases;
        } catch (Exception e) {
            throw new ServicioException(e);
        }
    }

    @Override
    public VwUltimoEstatusExpediente getUltimoEstatusExpediente(Long nunicodoc) throws ServicioException {
        return dao.get(VwUltimoEstatusExpediente.class, nunicodoc);
    }

    @Override
    public VwUltimoEstatusDocumento getUltimoEstatusDocumento(Long nunicodoct) throws ServicioException {
        return dao.get(VwUltimoEstatusDocumento.class, nunicodoct);
    }

    @Override
    public void saveLogMovimientos(LogMovimientos logMovimiento) throws ServicioException {
        dao.persist(logMovimiento);
    }

    @Override
    public void saveLogMovimientos(List<LogMovimientos> logMovimientos) throws ServicioException {
        dao.persist(logMovimientos);
    }

    @Override
    public TipoDocumCte1Ph getTipoDocumCte1Ph(Long scltcod, Long doccod) {
        return dao.get(TipoDocumCte1Ph.class, new TipoDocumCte1PhPK(scltcod, doccod));
    }

    @Override
    public List<TipoDocumCte1Ph> getTipoDocumCte1Ph(final Long scltcod, final List<Short> doccods) {
        if (doccods == null || doccods.isEmpty()) {
            return null;
        }
        StringBuilder hql = new StringBuilder("from TipoDocumCte1Ph where tipoDocumPK.scltcod=?");
        hql.append(" and doccod in(");

        for (int i = 0; i < doccods.size(); i++) {
            hql.append((i == 0) ? "?" : ",?");
        }
        hql.append(")");
        return dao.find(hql.toString(), TypeCast.join(new Object[]{scltcod}, doccods.toArray(new Short[]{})));  //get(TipoDocumCte1Ph.class, new TipoDocumCte1PhPK(scltcod, doccod));
    }

    @Override
    public List<TipoDocum> getTipoDocum(long scltcod) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TipoDocum.class);
        criteria.add(Restrictions.eq("tipoDocumPK.scltcod", scltcod));
        List<TipoDocum> documentos = dao.find(criteria);
        return documentos;
    }

    @Override
    public FovIntegraciones getIntegracionByNunicodoct(long nunicodoct) {
        DetachedCriteria criteria = DetachedCriteria.forClass(FovIntegraciones.class);
        criteria.add(Restrictions.eq("nunicodoct", nunicodoct));
        List<FovIntegraciones> rs = dao.find(criteria);
        if ((rs == null) || (rs.isEmpty())) {
            return null;
        } else {
            return rs.get(0);
        }
    }

    @Override
    public FovIntegraciones getIntegracionByNunicodoc(Long nunicodoc) {
        DetachedCriteria criteria = DetachedCriteria.forClass(FovIntegraciones.class);
        criteria.add(Restrictions.eq("fovIntegracionesPK.expedienteOrigen", nunicodoc));
        List<FovIntegraciones> rs = dao.find(criteria);
        if ((rs == null) || (rs.isEmpty())) {
            return null;
        } else {
            return rs.get(0);
        }
    }

    @Override
    public FovIntegraciones saveIntegracion(FovIntegraciones fovIntegracion) {
        return dao.persist(fovIntegracion);
    }

    @Override
    public List<ChecklistCap> getChecklistCapByNunicodoct(Long nunicodoct) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ChecklistCap.class);
        criteria.add(Restrictions.eq("checklistCapPK.nunicodoct", nunicodoct));
        List<ChecklistCap> documentos = dao.find(criteria);
        return documentos;
    }

    @Override
    public List<ChecklistDig> getCheckListDigByNunicodoct(Long nunicodoct) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ChecklistDig.class);
        criteria.add(Restrictions.eq("checklistDigPK.nunicodoct", nunicodoct));
        List<ChecklistDig> documentos = dao.find(criteria);
        return documentos;
    }

    @Override
    public VwEtiqueta getVwEtiqueta(final Long etiqueta, final Character tipo) {
        return dao.get(VwEtiqueta.class, new VwEtiquetaPK(etiqueta, tipo));
    }

    private OperatoriaCfg getCfg(final Long parentId, final Long idOperatoria) throws ServicioException {
        StringBuilder sqlDef = new StringBuilder("SELECT * FROM PROD.ADEAMX_DEFINITION WHERE PARENT_ID=? AND FEATURE=?");
        List<AdeamxDefinition> defs = dao.find(AdeamxDefinition.class, sqlDef, new Object[]{parentId, TypeCast.toString(idOperatoria)});
        if (defs == null || defs.isEmpty() || defs.size() > 1) {
            throw new ServicioException("ERROR AL OBTENER LOS DATOS DE CONFIGURACIÓN DE LA OPERATORIA " + idOperatoria + ", FAVOR DE VALIDAR EN EL ADEAMX DEFINITION.");
        }
        AdeamxDefinition def = defs.get(0);
        OperatoriaCfg cfg = Utils.convertToEntity(def.getConfigure(), OperatoriaCfg.class);
        if (TypeCast.isBlank(cfg.getDescripcion())) {
            cfg.setDescripcion(def.getDescription());
        }
        if (cfg.getIdOperatoria() == null) {
            cfg.setIdOperatoria(idOperatoria);
        }
        return cfg;
    }

    @Override
    public OperatoriaCfg getOperatoriaCfg(final Long idOperatoria) throws ServicioException {
        return getCfg(20000955L, idOperatoria);
    }

    @Override
    public OperatoriaCfg getCapturaCfg(Long idOperatoria) throws ServicioException {
        return getCfg(20000955L, idOperatoria);
    }

    @Override
    public OperatoriaCfg getIngresoCfg(final Long idOperatoria) throws ServicioException {
        return getCfg(20001546L, idOperatoria);
    }

    @Override
    public TipoDocumentoCfg getControlCalidadImagenCfg(final Long idOperatoria) throws ServicioException {
        StringBuilder sqlDef = new StringBuilder("SELECT * FROM PROD.ADEAMX_DEFINITION WHERE PARENT_ID=20001044 AND FEATURE=?");
        List<AdeamxDefinition> defs = dao.find(AdeamxDefinition.class, sqlDef, new Object[]{TypeCast.toString(idOperatoria)});
        if (defs == null || defs.isEmpty() || defs.size() > 1) {
            throw new ServicioException("ERROR AL OBTENER LOS DATOS DE CONFIGURACIÓN DE LA OPERATORIA, FAVOR DE VALIDAR EN EL ADEAMX DEFINITION.");
        }
        AdeamxDefinition def = defs.get(0);
        TipoDocumentoCfg cfg = Utils.convertToEntity(def.getConfigure(), TipoDocumentoCfg.class);
        return cfg;
    }

    @Override
    public List<String> getExpPendientesByCaja(Long caja, Long cliente) {
        List<String> expedientes = null;
        try {
            StringBuilder sql = new StringBuilder();

            sql.append("select deta.nunicodoc ");
            sql.append("from   prod.flow_ingresos_cab_mp cab, prod.flow_ingresos_deta_mp deta ");
            sql.append("where  cab.idrecibo = deta.idrecibo ");
            sql.append("and    cab.scltcod = ").append(cliente);
            sql.append("and    deta.NUNICODOC <> 0 ");
            sql.append("and    deta.caja_id = ");
            sql.append(caja);
            sql.append(" minus ");
            sql.append("select cd.nunicodoc ");
            sql.append("from   PROD.cabecera_doc cd, prod.detalle_sello ds ");
            sql.append("where  cd.nunicodoc = ds.nunicodoc ");
            sql.append("and    cd.scltcod = ").append(cliente);
            sql.append("and    ds.nunicosello = ");
            sql.append(caja);

            List<?> find = dao.find(sql);

            if (find != null && find.size() > 0) {
                expedientes = new ArrayList();

                for (int i = 0; i < find.size(); i++) {
                    expedientes.add(TypeCast.toString(find.get(i)));
                }
            }

            return expedientes;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public EtiqDocum saveEtiqDocum(EtiqDocum etiqDocum) {
        return dao.persist(etiqDocum);
    }

    @Override
    public ControlCalidad saveControlCalidad(ControlCalidad cc) {
        return dao.persist(cc);
    }
}
