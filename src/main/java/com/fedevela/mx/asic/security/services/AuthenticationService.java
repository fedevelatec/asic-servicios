package com.fedevela.mx.asic.security.services;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.security.pojos.BloqueoUsuarioWebmx;
import java.math.BigInteger;

public interface AuthenticationService {

    /**
     *
     * @param username
     * @param position
     * @param digits
     * @return
     */
    public boolean allowAccess(final String username, final BigInteger position, final String digits);

    /**
     *
     * @param login
     * @param password
     * @param idAuthority, Aplicacion
     * @return
     */
    public String validarAcceso(final String login, final String password, final String idAuthority);

    public String validarAcceso(final String login, final String pas, final String idAuthority, final String idAuthorityAdeadms);

    public BloqueoUsuarioWebmx getUsuarioBloqueo( final String usuario );

    public void saveUsuarioBloqueo( BloqueoUsuarioWebmx usuario ) throws Exception;

    public void deleteUsuarioBloqueo( BloqueoUsuarioWebmx usuario ) throws Exception;
}
