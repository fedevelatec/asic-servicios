package com.fedevela.asic.servicios.impl;

/**
 * Created by fvelazquez on 1/04/14.
 */
import com.fedevela.core.asic.pojos.Caja;
import com.fedevela.core.asic.pojos.Horizontalca;
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.asic.excepciones.ServicioException;
import com.fedevela.asic.servicios.UbicacionServicio;
import com.fedevela.asic.util.TypeCast;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

/**
 * Implementacion de UbicacionServicio que usa spring y hibernate.
 *
 * @author fvilla
 */
@Service("ubicacionServicio")
public class UbicacionServicioImpl implements UbicacionServicio, MethodInterceptor {

    @Autowired
    private DmsDao dao;
    SimpleJdbcInsert insertMovimientos;
    @Autowired(required = false)
    private HashMap<String, Object> sessionObject;
    private static String CLASE_REGGRALSERV = "RegistroGeneralServicio";
    private static String CLASE_CAPGRALSERV = "CapturaGeneralServicio";
    private static String METODO_GETCAJA = "getCaja";
    private static String METODO_GETETIQDOCUM = "getEtiqDocum";

    @Resource(name = "dataSourceCaptura")
    public void setDataSource(DataSource dataSource) {
        this.insertMovimientos = new SimpleJdbcInsert(dataSource).withSchemaName("PROD").withTableName("LOG_MOVIMIENTOS").usingColumns("CAJA_ID", "NUNICODOC", "NUNICODOCT", "USUARIO", "MAQUINA", "IP_MAQUINA", "APLICACION", "MODULO", "MOVIMIENTO");
    }

    @Override
    public Horizontalca getUbicacionCaja(Integer cajaId) throws ServicioException {
        if (cajaId == null) {
            throw new ServicioException(new IllegalArgumentException());
        }
        Caja c = dao.get(Caja.class, TypeCast.toLong(cajaId));
        if (c == null || c.getNunicoHz() == 0) {
            return null;
        }

        DetachedCriteria crit = DetachedCriteria.forClass(Horizontalca.class).add(Restrictions.eq("hzNunico", c.getNunicoHz()));
        List<Horizontalca> find = dao.find(crit);
        if (find.isEmpty()) { //no se encontro ningun registro.
            return null;
        } else {
            return find.get(0);//se regresa el primer elemento.
        }
    }

    @Override
    public void logMovimientos(Long caja, long nunicodoc, long nunicodoct, String usuario, String maquina, String ip, String app, String modulo, String movimiento) throws ServicioException {
        logMovimientos(caja.intValue(), nunicodoc, nunicodoct, usuario, maquina, ip, app, modulo, movimiento);
    }

    @Override
    public void logMovimientos(int caja, long nunicodoc, long nunicodoct, String usuario, String maquina, String ip, String app,
                               String modulo, String movimiento) throws ServicioException {
        Map<String, Object> parameters = new HashMap<String, Object>(3);
        parameters.put("CAJA_ID", caja);
        parameters.put("NUNICODOC", nunicodoc);
        parameters.put("NUNICODOCT", nunicodoct);
        parameters.put("USUARIO", usuario);
        parameters.put("MAQUINA", maquina);
        parameters.put("IP_MAQUINA", ip);
        parameters.put("APLICACION", app);
        parameters.put("MODULO", modulo);
        parameters.put("MOVIMIENTO", movimiento);
        insertMovimientos.execute(parameters);

    }

    @Override
    public Object invoke(MethodInvocation metodo) throws Throwable {
        if (sessionObject != null && sessionObject.size() > 0) {
            try {
                if (CLASE_REGGRALSERV.equals(metodo.getMethod().getDeclaringClass().getSimpleName()) && METODO_GETCAJA.equals(metodo.getMethod().getName())) {
                    Integer intCaja = (Integer) metodo.getArguments()[0];
                    logMovimientos(
                            intCaja,
                            0L,
                            0L,
                            (String) sessionObject.get("LOG_MOV.LOGIN"),
                            (String) sessionObject.get("LOG_MOV.PC"),
                            (String) sessionObject.get("LOG_MOV.IP"),
                            (String) sessionObject.get("LOG_MOV.APLICACION"),
                            sessionObject.get("LOG_MOV.MODULO") != null ? (String) sessionObject.get("LOG_MOV.MODULO") : "REGISTRO GENERAL",
                            sessionObject.get("LOG_MOV.MOVIMIENTO") != null ? (String) sessionObject.get("LOG_MOV.MOVIMIENTO") : "CONSULTA DE UBICACION");
                } else if (CLASE_REGGRALSERV.equals(metodo.getMethod().getDeclaringClass().getSimpleName()) && METODO_GETETIQDOCUM.equals(metodo.getMethod().getName())) {
                    Long nunicodoc = (Long) metodo.getArguments()[0];
                    logMovimientos(
                            0,
                            nunicodoc,
                            0L,
                            (String) sessionObject.get("LOG_MOV.LOGIN"),
                            (String) sessionObject.get("LOG_MOV.PC"),
                            (String) sessionObject.get("LOG_MOV.IP"),
                            (String) sessionObject.get("LOG_MOV.APLICACION"),
                            sessionObject.get("LOG_MOV.MODULO") != null ? (String) sessionObject.get("LOG_MOV.MODULO") : "REGISTRO GENERAL",
                            sessionObject.get("LOG_MOV.MOVIMIENTO") != null ? (String) sessionObject.get("LOG_MOV.MOVIMIENTO") : "CONSULTA DE UBICACION");
                } else if (CLASE_CAPGRALSERV.equals(metodo.getMethod().getDeclaringClass().getSimpleName())) {
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Object obj = metodo.proceed();
        return obj;
    }
}
