package com.fedevela.asic.servicios.impl;

/**
 * Created by fvelazquez on 1/04/14.
 */
import com.fedevela.core.asic.pojos.BitacoraArchivosCliente;
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.asic.excepciones.ServicioException;
import com.fedevela.asic.excepciones.ServicioOkException;
import com.fedevela.asic.servicios.CargaServicio;
import com.fedevela.asic.util.TypeCast;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

@Service("cargaServicio")
public class CargaServicioImpl implements CargaServicio {

    @Resource
    private DmsDao dao;

    /**
     *
     * @param cliente
     * @param status
     * @return
     * @throws ServicioException
     */
    @Override
    public List<BitacoraArchivosCliente> getBitacoraArchivosClientePorClienteStatus(Short cliente, Short status) throws ServicioException {
        BitacoraArchivosCliente bac = new BitacoraArchivosCliente();
        bac.setScltcod(cliente);
        bac.setStatus(status);
        return dao.find(bac);
    }

    /**
     *
     * @param bac
     * @throws ServicioException
     */
    @Override
    public BitacoraArchivosCliente salvaBitacoraArchivo(BitacoraArchivosCliente bac) throws ServicioException {
        return dao.persist(bac);
    }

    @Override
    public BitacoraArchivosCliente salvaBitacoraArchivo(BitacoraArchivosCliente bac, boolean rollback) throws ServicioOkException {
        BitacoraArchivosCliente bac1 = dao.persist(bac);
        if (rollback) {
            throw new ServicioOkException("Rollback");
        }
        return bac1;
    }

    /**
     *
     * @param cliente
     * @return
     * @throws ServicioException
     */
    @Override
    public List<BitacoraArchivosCliente> getBitacoraArchivosClientePorCliente(Short cliente) throws ServicioException {
        BitacoraArchivosCliente bac = new BitacoraArchivosCliente();
        bac.setScltcod(cliente);
        return dao.find(bac);
    }

    /**
     *
     * @param idArchivo
     * @return
     * @throws ServicioException
     */
    @Override
    public BitacoraArchivosCliente getBitacoraArchivosClientePorIdArchivo(Long idArchivo) throws ServicioException {
        if (idArchivo == null) {
            return null;
        }
        return dao.get(BitacoraArchivosCliente.class, idArchivo);
    }

    /**
     *
     * @param cliente
     * @param status
     * @param fechaProcesamiento
     * @return
     * @throws ServicioException
     */
    @Override
    public List<BitacoraArchivosCliente> getBitacoraArchivosClienteBy(Short cliente, Short status, Date fechaProcesamiento) throws ServicioException {
        StringBuilder hql = new StringBuilder("FROM BitacoraArchivosCliente ");
        hql.append("WHERE    status=").append(status);
        hql.append("     AND scltcod=").append(cliente);
        hql.append("     AND TO_CHAR(fechaProcesamiento,'yyyymmdd')='").append(TypeCast.toString(fechaProcesamiento, "yyyyMMdd")).append("'");
        return dao.find(hql.toString());
    }

    /**
     *
     * @param cliente
     * @param status
     * @param fechaProcesamiento
     * @param archivo
     * @return
     * @throws ServicioException
     */
    @Override
    public List<BitacoraArchivosCliente> getBitacoraArchivosClienteBy(Short cliente, Short status, Date fechaProcesamiento, String archivo) throws ServicioException {
        StringBuilder hql = new StringBuilder("FROM BitacoraArchivosCliente ");
        hql.append("WHERE    status=").append(status);
        hql.append("     AND scltcod=").append(cliente);
        hql.append("     AND TO_CHAR(fechaProcesamiento,'yyyymmdd')='").append(TypeCast.toString(fechaProcesamiento, "yyyyMMdd")).append("'");
        hql.append("     AND archivo='").append(archivo).append("'");
        return dao.find(hql.toString());
    }

    @Override
    public List<BitacoraArchivosCliente> getBitacoraArchivosClienteBy(Short cliente, Short status, String archivoDepositado) throws ServicioException {
        StringBuilder hql = new StringBuilder("FROM BitacoraArchivosCliente ");
        hql.append("WHERE    status=").append(status);
        hql.append("     AND scltcod=").append(cliente);
        hql.append("     AND archivoDepositado='").append(archivoDepositado).append("'");
        return dao.find(hql.toString());
    }

    @Override
    public List<BitacoraArchivosCliente> getBitacoraArchivosClienteBy(Short cliente, Date loadDate) throws ServicioException {
        DetachedCriteria criteria = DetachedCriteria.forClass(BitacoraArchivosCliente.class);
        criteria.add(Restrictions.eq("status", TypeCast.toShort(4)));
        criteria.add(Restrictions.eq("scltcod", cliente));
        Calendar c = new GregorianCalendar();
        c.setTime(loadDate);
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.SECOND, -1);
        criteria.add(Restrictions.between("fechaCargaDescarga", loadDate, c.getTime()));
        criteria.add(Restrictions.neProperty("totalRegistros", "registrosCorrectos"));
        return dao.find(criteria);
    }

    @Override
    public List<BitacoraArchivosCliente> getBitacoraArchivosCliente(StringBuilder sql) throws ServicioException {
        return dao.find(BitacoraArchivosCliente.class, sql);
    }
}
