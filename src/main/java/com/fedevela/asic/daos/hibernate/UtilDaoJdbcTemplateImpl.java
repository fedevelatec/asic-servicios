package com.fedevela.asic.daos.hibernate;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.asic.daos.UtilDao;
import java.util.Date;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository("utilDao")
public class UtilDaoJdbcTemplateImpl implements UtilDao{

    private JdbcTemplate template;
    @Resource(name="dataSourceCaptura")
    public void setDatasource(DataSource ds) {
        template = new JdbcTemplate(ds);

    }
    @Override
    public Date getDate() {
        String query = "select sysdate from dual";
        return (Date) template.queryForObject(query, Date.class);

    }

    @Override
    public Long getNextVal(String secuencia) {
        String query = "select "+secuencia+".nextval from dual";
        return (Long) template.queryForObject(query, Long.class);
    }
}
