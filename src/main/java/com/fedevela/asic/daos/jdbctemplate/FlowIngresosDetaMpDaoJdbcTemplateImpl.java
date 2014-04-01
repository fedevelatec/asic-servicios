package com.fedevela.asic.daos.jdbctemplate;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.asic.daos.FlowIngresosDetaMpDao;
import com.fedevela.core.asic.pojos.FlowIngresosDetaMp;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("flowIngresosDetaMpDao")
public class FlowIngresosDetaMpDaoJdbcTemplateImpl implements FlowIngresosDetaMpDao {

    private SimpleJdbcTemplate template;
    private Logger logger = LoggerFactory.getLogger(FlowIngresosDetaMpDao.class);

    @Resource(name = "dataSourceCaptura")
    public void setDataSource(DataSource ds) {
        template = new SimpleJdbcTemplate(ds);
    }


    @Override
    public Integer getNumeroExpedientesPorCaja(Long idCaja) {
        String query = "select count(*) from PROD.flow_ingresos_deta_mp where caja_id = ? and nunicodoc <> 0";
        return template.queryForInt(query, new Object[]{idCaja});
    }

    @Override
    public Integer getNumeroDocumentosPorCaja(Long idCaja) {
        String query = "select count(*) from PROD.flow_ingresos_deta_mpb where caja_id = ? and nunicodoct <> 0";
        return template.queryForInt(query, new Object[]{idCaja});
    }


    @Override
    public void guardaFlowIngresoDetaMp(FlowIngresosDetaMp detaMp) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into prod.flow_ingresos_deta_mp(CAJA_ID, NUNICODOC, STATUS, IDRECIBO, USUARIO) values (?,?,?,?,?) ");
        template.update(sb.toString(), new Object[]{detaMp.getFlowIngresosDetaMpPK().getCajaId(), detaMp.getFlowIngresosDetaMpPK().getNunicodoc(),
                detaMp.getStatus(), detaMp.getFlowIngresosCabMp(), detaMp.getUsuario()});
    }

    @Override
    public void borraFlowIngresoDetaMpPorCajaUnicodoc(Long cajaId, Long nunicodoc) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from prod.flow_ingresos_deta_mp where CAJA_ID = ? and NUNICODOC = ?");
        template.update(sb.toString(), new Object[]{cajaId, nunicodoc});
    }

    @Override
    public void actualizaEstatusFlowIngresoDetaMpPorCaja(Long cajaId, Long status) {
        StringBuilder sb = new StringBuilder();
        sb.append("update prod.flow_ingresos_deta_mp set STATUS = ? where CAJA_ID = ?");
        template.update(sb.toString(), new Object[]{status, cajaId});
    }

    @Override
    public void actualizaEstatusFlowIngresoDetaMpPorCajas(Long cajaOrigen, Long cajaDestino, int status, Long nunicodoc) {
        StringBuilder sb = new StringBuilder();
        sb.append("update prod.flow_ingresos_deta_mp set STATUS = ?, CAJA_ID = ? where CAJA_ID = ? AND nunicodoc = ?");
        template.update(sb.toString(), new Object[]{status, cajaDestino, cajaOrigen, nunicodoc});
    }


    @Override
    public int getExpedientesByStatus(Long recibo, Long status) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from prod.flow_ingresos_deta_mp where IDRECIBO = ? and STATUS = ?");
        return template.queryForInt(sb.toString(), new Object[]{recibo, status});
    }

    @Override
    public int getExpedientesByRecibo(Long recibo) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from prod.flow_ingresos_deta_mp where IDRECIBO = ?");
        return template.queryForInt(sb.toString(), new Object[]{recibo});
    }
}
