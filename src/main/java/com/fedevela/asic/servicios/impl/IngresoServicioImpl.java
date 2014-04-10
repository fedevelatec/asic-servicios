package com.fedevela.asic.servicios.impl;

/**
 * Created by fvelazquez on 1/04/14.
 */
import com.fedevela.core.asic.pojos.*;
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.asic.daos.FlowIngresosDetaMpDao;
import com.fedevela.asic.excepciones.ServicioException;
import com.fedevela.asic.servicios.IngresoServicio;
import com.fedevela.asic.util.TypeCast;
import java.util.List;
import javax.annotation.Resource;
import net.codicentro.utils.Scalar;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author avillalobos
 */
@Service
public class IngresoServicioImpl implements IngresoServicio {

    @Resource
    private DmsDao dao;
    @Resource
    private FlowIngresosDetaMpDao flowIngresosDetaMpDao;
    private Logger log = LoggerFactory.getLogger(IngresoServicioImpl.class);

    @Override
    public FlowIngresosDetaMpb getFlowIngresosDetaMpb(Long nunicodoct) {
        return dao.get(FlowIngresosDetaMpb.class, nunicodoct);
    }

    @Override
    public FlowIngresosDetaMp getFlowIngresosDetaMp(Long nunicodoc) {
        return dao.get(FlowIngresosDetaMp.class, nunicodoc);
    }

    @Override
    public FlowIngresosCabMp getFlowIngresosCabMp(Long idrecibo) {
        return dao.get(FlowIngresosCabMp.class, idrecibo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cierraCaja(CajaCerrada cajaCerrada) throws ServicioException {
        log.debug("Cerrando caja: " + cajaCerrada);
        if (cajaCerrada == null) {
            throw new ServicioException(new IllegalArgumentException("argumento cajaCerrada no puede ser nulo"));
        }
        //TODO validar caja.
        FlowOperatoria cajaOperatoria = getOperatoriaPorCajaIngresada(cajaCerrada.getCajaId());
        if (cajaOperatoria == null) {
            throw new ServicioException("La caja: " + cajaCerrada + " no existe en ingreso.");
        }
        if (!cajaOperatoria.getTipoCierreCaja().equals(new Short((short) 0))) {
            throw new ServicioException("La caja: " + cajaCerrada + " no es de cierre manual.");
        }
        CajaCerrada getCaja = dao.get(CajaCerrada.class, cajaCerrada.getCajaId());
        if (getCaja != null) {
            throw new ServicioException("La caja: " + cajaCerrada + " ya fue cerrada.");
        }

        DetachedCriteria dc = DetachedCriteria.forClass(DetalleSello.class).add(Restrictions.eq("nunicosello", cajaCerrada.getCajaId()));
        List<DetalleSello> detalleSellos = dao.find(dc, 0, 1);
        if (detalleSellos.isEmpty()) {
            throw new ServicioException("La caja: " + cajaCerrada + " no se puede cerrar porque no tiene ningun expediente capturado.");
        }

        FlowIngresosDetaMp flowIngresosDetaCabecera = getFlowIngresosDetaCabecera(cajaCerrada.getCajaId());

        DetalleSello detalleSello = detalleSellos.get(0); //obtenemos el primero.
        FlowIngresosDetaMp detaMp = new FlowIngresosDetaMp();
        detaMp.setStatus(new Short("5"));
        detaMp.setFlowIngresosDetaMpPK(new FlowIngresosDetaMpPK(detalleSello.getNunicodoc(), cajaCerrada.getCajaId())); //setNunicodoc(detalleSello.getNunicodoc());
        detaMp.setFlowIngresosCabMp(flowIngresosDetaCabecera.getFlowIngresosCabMp());
        detaMp.setUsuario(cajaCerrada.getUsuario());
        //detaMp.setMaquina(cajaCerrada.getMaquina());
        //TODO agregar dato a flow_ingreso para meter caja a mapeo.
        flowIngresosDetaMpDao.guardaFlowIngresoDetaMp(detaMp);
        flowIngresosDetaMpDao.actualizaEstatusFlowIngresoDetaMpPorCaja(cajaCerrada.getCajaId(), new Long(5));
        //TODO guardar registro.
        dao.persist(cajaCerrada);
    }

    @Override
    public int getExpedientesByStatus(Long recibo, Long status) throws ServicioException {
        return flowIngresosDetaMpDao.getExpedientesByStatus(recibo, status);
    }

    @Override
    public int getExpedientesByRecibo(Long recibo) throws ServicioException {

        return flowIngresosDetaMpDao.getExpedientesByRecibo(recibo);
    }

    @Override
    public void salvaCabIngreso(FlowIngresosCabMp cabMp) throws ServicioException {
        dao.persist(cabMp);
    }

    @Override
    public FlowIngresosDetaMp getFlowIngresosDetaPorNunicoDoc(Long nunicoDoc) throws ServicioException {
        DetachedCriteria criteria = DetachedCriteria.forClass(FlowIngresosDetaMp.class);
        criteria.add(Restrictions.eq("flowIngresosDetaMpPK.nunicodoc", nunicoDoc));
        List<FlowIngresosDetaMp> rs = dao.find(criteria);
        if (rs == null || rs.isEmpty()) {
            return null;
        } else {
            return rs.get(0);
        }
    }

    @Override
    public Integer getNumeroExpedientesPorCaja(Long idCaja) throws ServicioException {
        try {
            return flowIngresosDetaMpDao.getNumeroExpedientesPorCaja(idCaja);
        } catch (Exception e) {
            throw new ServicioException(e);
        }
    }

    @Override
    public Integer getNumeroDocumentosPorCaja(Long idCaja) throws ServicioException {
        try {
            return flowIngresosDetaMpDao.getNumeroDocumentosPorCaja(idCaja);
        } catch (Exception e) {
            throw new ServicioException(e);
        }
    }

    @Override
    public FlowIngresosDetaMpb getFlowIngresosDetaPorNunicoDoct(Long nunicodoct) throws ServicioException {
        return dao.get(FlowIngresosDetaMpb.class, nunicodoct);
    }

    @Override
    public FlowOperatoria getOperatoriaPorCajaIngresada(Long idCaja) throws ServicioException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT DISTINCT oper.* ");
            sql.append("FROM PROD.flow_ingresos_cab_mp cab, ");
            sql.append("PROD.flow_ingresos_deta_mp deta, ");
            sql.append("PROD.flow_operatoria oper ");
            sql.append("WHERE cab.idrecibo = deta.idrecibo ");
            sql.append("AND oper.idoperatoria = cab.idoperatoria ");
            sql.append("AND deta.caja_id = ? ");
            List<FlowOperatoria> rs = dao.find(FlowOperatoria.class, sql, new Object[]{idCaja});
            if (rs == null || rs.isEmpty()) {
                return null;
            } else {
                return rs.get(0);
            }
        } catch (Exception e) {
            throw new ServicioException(e);
        }
    }

    //RVILLANUEVA
    @Override
    public FlowOperatoria getOperatoriaPorExpedienteIngresado(Long nunicodoc) throws ServicioException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT DISTINCT oper.* ");
            sql.append("FROM PROD.flow_ingresos_cab_mp cab, ");
            sql.append("PROD.flow_ingresos_deta_mp deta, ");
            sql.append("PROD.flow_operatoria oper ");
            sql.append("WHERE cab.idrecibo = deta.idrecibo ");
            sql.append("AND oper.idoperatoria = cab.idoperatoria ");
            sql.append("AND deta.nunicodoc = ? ");
            List<FlowOperatoria> rs = dao.find(FlowOperatoria.class, sql, new Object[]{nunicodoc});
            if (rs == null || rs.isEmpty()) {
                return null;
            } else {
                return rs.get(0);
            }
        } catch (Exception e) {
            throw new ServicioException(e);
        }
    }

    @Override
    public FlowIngresosDetaMp getFlowIngresosDetaCabecera(Long cajaId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(FlowIngresosDetaMp.class);
        criteria.add(Restrictions.eq("flowIngresosDetaMpPK.nunicodoc", 0L));
        criteria.add(Restrictions.eq("flowIngresosDetaMpPK.cajaId", cajaId));
//        StringBuilder sql = new StringBuilder("SELECT * FROM PROD.FLOW_INGRESOS_DETA_MP WHERE NUNICODOC=0 AND CAJA_ID=?");
//        List<FlowIngresosDetaMp> rs = dao.find(FlowIngresosDetaMp.class, sql, new Object[]{cajaId});
        List<FlowIngresosDetaMp> rs = dao.find(criteria);
        if (rs == null || rs.isEmpty()) {
            return null;
        } else {
            return rs.get(0);
        }
    }

    @Override
    public Long save(List<?> ingresos) {
        Long recibo = null;
        List<VwEtiqueta> lstEtq = null;
        // Guardamos los registros
        dao.persist(ingresos);

        // Obtenemos el recibo que se acaba de generar
        StringBuilder query = new StringBuilder()
                .append( "SELECT * FROM PROD.VW_ETIQUETA WHERE ETIQUETA = ? AND TIPO = ? " );

        for (Object obj : ingresos) {
            if( obj instanceof FlowIngresosDetaMp ) {
                FlowIngresosDetaMp detaMp = (FlowIngresosDetaMp)obj;
                boolean isCaja = detaMp.getFlowIngresosDetaMpPK().getNunicodoc().compareTo( 0l ) == 0;
                lstEtq = dao.find( VwEtiqueta.class, query, new Object[]{ isCaja ? detaMp.getFlowIngresosDetaMpPK().getCajaId() : detaMp.getFlowIngresosDetaMpPK().getNunicodoc(),
                        isCaja ? 'S' : 'U' } );
                break;
            } else if( obj instanceof FlowIngresosDetaMpb ) {
                FlowIngresosDetaMpb detaMpb = (FlowIngresosDetaMpb)obj;
                lstEtq = dao.find( VwEtiqueta.class, query, new Object[]{ detaMpb.getNunicodoct(), 'T' } );
                break;
            }
        }

        if( lstEtq != null && !lstEtq.isEmpty() ) {
            recibo = lstEtq.get( 0 ).getReciboIngreso();
        }

        return recibo;
    }

    @Override
    public Long getTotalExpedienteIngresadoCaja(final Long cajaId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) TOTAL FROM PROD.FLOW_INGRESOS_DETA_MP WHERE CAJA_ID=? AND NUNICODOC>0");
        List<?> rs = dao.find(sql, new Object[]{cajaId}, new Scalar[]{new Scalar("TOTAL", StandardBasicTypes.LONG)});
        if (rs == null || rs.isEmpty()) {
            return null;
        } else {
            return TypeCast.toLong(rs.get(0));
        }
    }

    @Override
    public void deleteFlowIngresosDetaMp(FlowIngresosDetaMp fidm) {
        dao.delete(fidm);
    }
}
