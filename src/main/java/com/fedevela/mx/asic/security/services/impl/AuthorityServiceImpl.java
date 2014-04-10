package com.fedevela.mx.asic.security.services.impl;

/**
 * Created by fvelazquez on 10/04/14.
 */
import com.fedevela.core.security.beans.PersonalAutorizadoBean;
import com.fedevela.asic.daos.DmsDao;
//import com.adeamx.persistencia.dao.SecurityDao;
import java.util.List;
import javax.annotation.Resource;
import com.fedevela.mx.asic.security.services.AuthorityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    private Logger logger = LoggerFactory.getLogger(AuthorityServiceImpl.class);
//    @Resource
//    private SecurityDao dao;

    @Resource
    private DmsDao dao;

    @Override
    public List<PersonalAutorizadoBean> getPersonalAutorizado(final String idAuthority) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT us.LOGIN,us.PASSWORD, us.NOMBRE");
        sql.append(" FROM MEXWEB.GRANTED_USER gu,");
        sql.append(" 	  MEXWEB.USUARIO_WEBMX us");
        sql.append(" WHERE    gu.ID_AUTHORITY=?");
        sql.append("	  AND us.LOGIN=gu.USERNAME");
        return dao.find(PersonalAutorizadoBean.class, sql, new Object[]{idAuthority});
    }
}
