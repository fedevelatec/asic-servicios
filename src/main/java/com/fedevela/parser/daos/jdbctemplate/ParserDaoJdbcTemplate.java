package com.fedevela.parser.daos.jdbctemplate;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.parser.daos.ParserDao;

import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("parserDao")
public class ParserDaoJdbcTemplate implements ParserDao {
    JdbcTemplate template;

    @Resource(name="dataSourceCaptura")
    public void setDatasource(DataSource ds) {
        template = new JdbcTemplate(ds);
    }

    @Override
    public int borraAnteriores(String cargaId) {
        String sql = "DELETE FROM prod.carga WHERE id LIKE ? ";

        return template.update(sql,new Object[]{cargaId});
    }
}
