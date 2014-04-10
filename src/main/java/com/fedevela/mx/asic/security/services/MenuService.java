package com.fedevela.mx.asic.security.services;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.asic.cpanel.pojos.VwDmsMenuSearch;
import com.fedevela.core.security.pojos.MenuWebmx;
import com.fedevela.core.security.pojos.VwMenuWebmx;
import java.util.Collection;
import java.util.List;

public interface MenuService {

    /**
     *
     * @param username
     * @param autorities
     * @param idApplication
     * @return
     * @deprecated
     */
    public List<VwMenuWebmx> getMenu(final String username, final Collection autorities, final Long idApplication);

    /**
     *
     * @param autorities
     * @param idApplication
     * @return
     */
    public List<MenuWebmx> getMenu(final Collection autorities, final Long idApplication);

    public List<MenuWebmx> getMenu(final String username, final Long idApplication);

    /**
     * Solo utilizar para el cpanel.
     * @return
     */
    public List<MenuWebmx> getMenuSearch();

    public List<MenuWebmx> getMenuApp(final Long idApp, final Collection autorities, boolean eagerMode);

    public List<MenuWebmx> getMenus( Long idParent, boolean eagerMode, final Collection autorities );

    public List<VwDmsMenuSearch> getDmsMenuSearch();

    public List<VwDmsMenuSearch> getDmsMenuSearch(Long idParent, String tipo);

    public MenuWebmx getDmsMenuWebmx( Long idMenu );

    public MenuWebmx saveDmsMenuWebmx( MenuWebmx menu );

    public void deleteDmsMenu(Long id) throws Exception;
}
