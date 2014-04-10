package com.fedevela.mx.asic.security.services.impl;

/**
 * Created by fvelazquez on 10/04/14.
 */
import com.fedevela.core.security.beans.UsuarioWebmxBean;
import com.fedevela.core.security.pojos.AplicacionWebmx;
import com.fedevela.core.security.pojos.Authority;
import com.fedevela.core.security.pojos.GrantappWebmx;
import com.fedevela.core.security.pojos.GrantappWebmxPK;
import com.fedevela.core.security.pojos.GrantedUser;
import com.fedevela.core.security.pojos.GrantedUserPK;
import com.fedevela.core.security.pojos.GrantrolWebmx;
import com.fedevela.core.security.pojos.GrantrolWebmxPK;
import com.fedevela.core.security.pojos.RolWebmx;
import com.fedevela.core.security.pojos.UsuarioWebmx;
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.asic.util.TypeCast;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import com.fedevela.mx.asic.security.services.UserService;
import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private DmsDao dao;

    @Override
    public UsuarioWebmx getUser(final String username) {
        UsuarioWebmx uw = dao.get(UsuarioWebmx.class, username);
        if (uw != null) {
            List<Authority> authorities = dao.find("SELECT a FROM Authority a,GrantedUser gu WHERE gu.grantedUserPK.idAuthority=a.idAuthority AND gu.grantedUserPK.username = ?", new Object[]{username});
            uw.setAuthorityList(authorities);
            List<RolWebmx> rols = dao.find("SELECT a FROM RolWebmx a,GrantrolWebmx b WHERE b.grantrolWebmxPK.idrol=a.idrol AND b.grantrolWebmxPK.login = ?", new Object[]{username});
            uw.setRols(rols);
            List<AplicacionWebmx> aplicaciones = dao.find("SELECT a FROM AplicacionWebmx a,GrantappWebmx b WHERE b.grantappWebmxPK.idaplicacion=a.idaplicacion AND b.grantappWebmxPK.login = ?", new Object[]{username});
            uw.setAplicaciones(aplicaciones);
//            List<TarjetaWebmx> tarjeta = dao.find("SELECT a FROM TarjetaWebmx a WHERE a.status='A' AND a.login = ?", new Object[]{username});
//            if (tarjeta != null && !tarjeta.isEmpty() && tarjeta.size() > 1) {
//                throw new RuntimeException("El usuario " + username + " tiene mas de una tarjeta de acceso seguro activas. Favor de validar.");
//            }
//            uw.setTarjeta(tarjeta == null || tarjeta.isEmpty() ? null : tarjeta.get(0));
        }
        return uw;
    }

    @Override
    public List<UsuarioWebmx> getUsers(DetachedCriteria criteria) {
        return dao.find(criteria);
    }

    @Override
    public List<UsuarioWebmx> getUsers(DetachedCriteria criteria, Integer page, Integer size) {
        return dao.find(criteria, page, size);
    }

    @Override
    public UsuarioWebmx saveUser(UsuarioWebmx usuario) {
        return dao.persist(usuario);
    }

    @Override
    public GrantedUser addAuthority(final String login, final String idAuthority) {
        return dao.persist(new GrantedUser(idAuthority, login));
    }

    @Override
    public void removeAuthority(final String login, final String idAuthority) {
        GrantedUser gu = dao.get(GrantedUser.class, new GrantedUserPK(idAuthority, login));
        if (gu != null) {
            dao.delete(gu);
        }
    }

    @Override
    public GrantrolWebmx addRol(final String login, final Long idrol) {
        return dao.persist(new GrantrolWebmx(idrol, login));
    }

    @Override
    public void removeRol(final String login, final Long idrol) {
        GrantrolWebmx gr = dao.get(GrantrolWebmx.class, new GrantrolWebmxPK(idrol, login));
        if (gr != null) {
            dao.delete(gr);
        }
    }

    @Override
    public GrantappWebmx addAplicacion(final String login, final Long idaplicacion) {
        return dao.persist(new GrantappWebmx(idaplicacion, login));
    }

    @Override
    public void removeAplicacion(final String login, final Long idaplicacion) {
        GrantappWebmx ga = dao.get(GrantappWebmx.class, new GrantappWebmxPK(idaplicacion, login));
        if (ga != null) {
            dao.delete(ga);
        }
    }

    private Integer total;
    private List<Object> params;

    private StringBuilder paginator(StringBuilder sql, StringBuilder order, Integer page, Integer size) {
        List md = dao.find(new StringBuilder("SELECT COUNT(*) FROM(" + sql.toString() + ")"), params.toArray());
        total = TypeCast.toInteger(md == null || md.isEmpty() ? 0 : md.get(0));
        page = page == null ? 0 : page;
        size = size == null ? 0 : size;
        StringBuilder pSql = new StringBuilder("SELECT * FROM(SELECT row_.*,ROWNUM rownum_ FROM (");
        if (order != null) {
            sql.append(" ORDER BY ").append(order);
        }
        pSql.append(sql).append(") row_) WHERE rownum_ >").append(page).append(" AND rownum_ <=").append(page + size);
        return pSql;
    }

    @Override
    public List<UsuarioWebmxBean> getUsuarios(
            final Integer page,
            final Integer size,
            final UsuarioWebmxBean criteria) {
        params = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT U.LOGIN,");
        sql.append("        U.NOMBRE,");
        sql.append("        U.CLIENTE SCLTCOD,");
        sql.append("        U.EMAIL,");
        sql.append("        U.FECHAALTA,");
        sql.append("        U.FECHABAJA,");
        sql.append("        U.STATUS,");
        sql.append("        U.INTENTOS,");
        sql.append("        U.FECHAREVOCADO,");
        sql.append("        C.ACLTRZSC CLIENTE");
        sql.append(" FROM MEXWEB.USUARIO_WEBMX U,");
        sql.append("      PROD.CLIENTES C");
        sql.append(" WHERE C.SCLTCOD=U.CLIENTE");
        if (criteria != null) {
            if (!TypeCast.isBlank(criteria.getLogin())) {
                sql.append(" AND U.LOGIN = ?");
                params.add(criteria.getLogin());
            }
            if (!TypeCast.isBlank(criteria.getNombre())) {
                sql.append(" AND UPPER(U.NOMBRE) LIKE '%'||?||'%'");
                params.add(criteria.getNombre().toUpperCase());
            }
            if (criteria.getStatus() != null) {
                sql.append(" AND U.STATUS = ?");
                params.add(criteria.getStatus());
            }
            if (criteria.getAuthorities() != null && !criteria.getAuthorities().isEmpty()) {
                if (criteria.getAuthorities().contains("MAILROOM")) {
                    // Todos los usuarios que cuenten con este rol no tienen acceso a ver los usuarios del ciente 2
                    sql.append(" AND U.CLIENTE <> ?");
                    params.add(2L);
                }
            }
        }
        return dao.find(UsuarioWebmxBean.class, paginator(sql, null, page, size), params.toArray());
    }

    @Override
    public Integer getTotal() {
        return total;
    }

}
