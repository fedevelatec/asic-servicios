package com.fedevela.mx.asic.security.services;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.security.beans.UsuarioWebmxBean;
import com.fedevela.core.security.pojos.GrantappWebmx;
import com.fedevela.core.security.pojos.GrantedUser;
import com.fedevela.core.security.pojos.GrantrolWebmx;
import com.fedevela.core.security.pojos.UsuarioWebmx;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;

public interface UserService {

    public UsuarioWebmx getUser(final String username);

    public List<UsuarioWebmx> getUsers(DetachedCriteria criteria);

    public List<UsuarioWebmx> getUsers(DetachedCriteria criteria, Integer page, Integer size);

    public UsuarioWebmx saveUser(UsuarioWebmx usuario);

    public GrantedUser addAuthority(final String login, final String idAuthority);

    public void removeAuthority(final String login, final String idAuthority);

    public GrantrolWebmx addRol(final String login, final Long idrol);

    public void removeRol(final String login, final Long idrol);

    public GrantappWebmx addAplicacion(final String login, final Long idaplicacion);

    public void removeAplicacion(final String login, final Long idaplicacion);


    public List<UsuarioWebmxBean> getUsuarios(
            final Integer page,
            final Integer size,
            final UsuarioWebmxBean criteria);

    /**
     * Total de registros de una consulta, solo para aqellos que aplique y
     * utilicen el paginator.
     *
     * @return
     */
    public Integer getTotal();
}
