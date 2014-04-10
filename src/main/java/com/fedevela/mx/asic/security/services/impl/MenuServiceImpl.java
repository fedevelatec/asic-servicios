package com.fedevela.mx.asic.security.services.impl;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.asic.cpanel.pojos.VwDmsMenuSearch;
import com.fedevela.core.security.pojos.AplicacionWebmx;
import com.fedevela.core.security.pojos.MenuWebmx;
import com.fedevela.core.security.pojos.VwMenuWebmx;
import com.fedevela.asic.daos.DmsDao;
import java.util.Collection;
import java.util.List;
import javax.annotation.Resource;
import com.fedevela.mx.asic.security.services.MenuService;
import net.codicentro.core.TypeCast;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MenuServiceImpl implements MenuService {

    private Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);
    @Resource
    private DmsDao dao;

    @Override
    public List<MenuWebmx> getMenu(final String username, final Long idApplication) {
        DetachedCriteria criteria = DetachedCriteria.forClass(MenuWebmx.class);
        criteria.add(Restrictions.eq("aplicacionWebmx", new AplicacionWebmx(idApplication)));
        criteria.add(Restrictions.eq("status", 'A'));
        criteria.addOrder(Order.asc("idmenu"));
        return dao.find(criteria);
    }

    @Override
    @Deprecated
    public List<VwMenuWebmx> getMenu(String username, final Collection autorities, Long idApplication) {
        DetachedCriteria criteria = DetachedCriteria.forClass(VwMenuWebmx.class);
        criteria.add(Restrictions.eq("idaplicacion", idApplication.doubleValue()));
        criteria.add(Restrictions.in("idAuthority", autorities));
        criteria.add(Restrictions.eq("status", 'A'));
        criteria.addOrder(Order.asc("idmenu"));
        return dao.find(criteria);
    }

    @Override
    public List<MenuWebmx> getMenu(final Collection autorities, final Long idApplication) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT MW.*");
        sql.append(" FROM MEXWEB.MENU_WEBMX MW,");
        sql.append("      MEXWEB.GRANTED_MENU GM");
        sql.append(" WHERE    GM.ID_MENU=MW.IDMENU");
        sql.append("      AND MW.STATUS='A'");
        sql.append("      AND MW.IDAPLICACION=?");
        sql.append("      AND GM.ID_AUTHORITY IN(");
        if (!autorities.isEmpty()) {
            sql.append("?");
            if (autorities.size() > 1) {
                sql.append(TypeCast.repeat(", ?", autorities.size() - 1));
            }
        }
        sql.append(")");
        sql.append(" ORDER BY MW.IDMENU ASC");
        return dao.find(MenuWebmx.class, sql, TypeCast.join(new Object[]{idApplication}, autorities.toArray()));
    }

    @Override
    public List<MenuWebmx> getMenuSearch() {
        DetachedCriteria criteria = DetachedCriteria.forClass(MenuWebmx.class);
        criteria.add(Restrictions.or(Restrictions.isNull("menuWebmx"), Restrictions.eqProperty("idmenu", "menuWebmx.idmenu")));
        criteria.addOrder(Order.asc("idmenu"));
        return dao.find(criteria);
    }

    @Override
    public List<MenuWebmx> getMenuApp(final Long idApp, final Collection autorities, boolean eagerMode) {
        StringBuilder sql = new StringBuilder()
                .append(" SELECT DISTINCT MW.*")
                .append(" FROM MEXWEB.MENU_WEBMX MW,")
                .append("      MEXWEB.GRANTED_MENU GM")
                .append(" WHERE    GM.ID_MENU=MW.IDMENU")
                .append("      AND MW.STATUS='A'")
                .append("      AND MW.IDAPLICACION=?")
                .append("      AND (MW.IDMENU = MW.ID_PARENT OR MW.ID_PARENT IS NULL)")
                .append("      AND GM.ID_AUTHORITY IN(");
        if (!autorities.isEmpty()) {
            sql.append("?");
            if (autorities.size() > 1) {
                sql.append(TypeCast.repeat(", ?", autorities.size() - 1));
            }
        }
        sql.append(")");
        sql.append(" ORDER BY MW.E_ORDER ASC");

        List<MenuWebmx> menus = dao.find(MenuWebmx.class, sql, TypeCast.join(new Object[]{idApp}, autorities.toArray()));
        if (eagerMode) {
            for (MenuWebmx mnu : menus) {
                mnu.setMenuWebmxList(getMenus(mnu.getIdmenu(), eagerMode, autorities));
            }
        }
        return menus;
    }

    @Override
    public List<MenuWebmx> getMenus(Long idParent, boolean eagerMode, final Collection autorities) {
        StringBuilder query = new StringBuilder()
                .append(" SELECT DISTINCT MW.* ")
                .append(" FROM MEXWEB.MENU_WEBMX MW,")
                .append("       MEXWEB.GRANTED_MENU GM ")
                .append(" WHERE MW.IDMENU = GM.ID_MENU AND MW.ID_PARENT =  ? ")
                .append(" AND MW.STATUS = 'A' AND MW.IDMENU <> MW.ID_PARENT ")
                .append("  AND GM.ID_AUTHORITY IN(");
        if (!autorities.isEmpty()) {
            query.append("?");
            if (autorities.size() > 1) {
                query.append(TypeCast.repeat(", ?", autorities.size() - 1));
            }
        }
        query.append(")");
        query.append(" ORDER BY MW.E_ORDER ASC ");
        List<MenuWebmx> menus = dao.find(MenuWebmx.class, query, TypeCast.join(new Object[]{idParent}, autorities.toArray()));

        if (eagerMode && !menus.isEmpty()) {
            for (MenuWebmx mnu : menus) {
                mnu.setMenuWebmxList(getMenus(mnu.getIdmenu(), eagerMode, autorities));
            }
        }

        return menus;
    }

    @Override
    public List<VwDmsMenuSearch> getDmsMenuSearch() {
        return getDmsMenuSearch(null, null);
    }

    @Override
    public List<VwDmsMenuSearch> getDmsMenuSearch(Long idParent, String tipo) {
        StringBuilder query = new StringBuilder()
                .append(" SELECT * FROM PROD.VW_DMSMENU_SEARCH ")
                .append(" WHERE ");
        if (idParent == null) {
            query.append(" TIPO = 'DMS' ");
        } else if (tipo.equals("DMS")) {
            query.append(" TIPO = 'APP' ");
        } else if (tipo.equals("APP")) {
            query.append(" IDAPLICACION = ").append((idParent - 1000000))
                    .append(" AND TIPO = 'MNU' ")
                    .append(" AND (IDMENU = ID_PARENT OR ID_PARENT IS NULL) ");
        } else if (tipo.equals("MNU")) {
            query.append(" ID_PARENT = ").append(idParent)
                    .append(" AND IDMENU <> ID_PARENT ");
        }
        query.append(" ORDER BY E_ORDER ");

        return dao.find(VwDmsMenuSearch.class, query);
    }

    @Override
    public MenuWebmx getDmsMenuWebmx(Long idMenu) {
        return dao.get(MenuWebmx.class, idMenu);
    }

    @Override
    public MenuWebmx saveDmsMenuWebmx(MenuWebmx menu) {
        return dao.persist(menu);
    }

    @Override
    public void deleteDmsMenu(Long id) throws Exception {
        MenuWebmx mw = dao.get(MenuWebmx.class, id);
        if (mw != null) {
            dao.delete(mw);
        } else {
            throw new Exception("El objeto \"Menu\" con id=" + id + " no existe.");
        }
    }
}
