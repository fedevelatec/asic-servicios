package com.fedevela.asic.daos.jdbctemplate;

/**
 * Created by fvelazquez on 31/03/14.
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fedevela.asic.daos.BitacoraArchivosGralDao;
import com.fedevela.core.asic.pojos.BitacoraArchivosGral;
import org.springframework.jdbc.core.RowMapper;

@Repository("bitacoraArchivosGralDao")
public class BitacoraArchivosGralDaoJdbcTemplateImpl implements
        BitacoraArchivosGralDao {

    SimpleJdbcTemplate template;

    @Resource(name="dataSourceCaptura")
    public void setDataSource(DataSource dataSource) {
        template = new SimpleJdbcTemplate(dataSource);
    }

    @Override
    public List<BitacoraArchivosGral> getBitacoraArchivosGralPorClienteStatus(
            Long cliente, Long status) {
        String sql = "select * from prod.bitacora_archivos_gral where scltcod = ? and status = ?";
        RowMapper<BitacoraArchivosGral> rm = new RowMapper<BitacoraArchivosGral>() {

            @Override
            public BitacoraArchivosGral mapRow(ResultSet rs, int rowNum) throws SQLException {
                BitacoraArchivosGral gral = new BitacoraArchivosGral();
                gral.setArchivoDepositado(rs.getString("ARCHIVO_DEPOSITADO"));
                gral.setStatus(rs.getLong("STATUS"));
                gral.setRegistrosCorrectos(rs.getLong("REGISTROS_CORRECTOS"));
                gral.setTotalRegistros(rs.getLong("TOTAL_REGISTROS"));
                gral.setTipo(rs.getString("TIPO"));
                gral.setUsuario(rs.getString("USUARIO"));
                gral.setFechaProcesamiento(rs.getDate("FECHA_PROCESAMIENTO"));
                gral.setFechaCargaDescarga(rs.getDate("FECHA_CARGA_DESCARGA"));
                gral.setArchivo(rs.getString("ARCHIVO"));
                gral.setScltcod(rs.getLong("SCLTCOD"));
                return gral;
            }
        };
        return template.query(sql , rm , new Object[] {cliente, status});
    }

}
