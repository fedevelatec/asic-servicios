package com.fedevela.mx.asic.security.services.impl;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.security.pojos.*;
import com.fedevela.asic.daos.DmsDao;
//import com.adeamx.persistencia.dao.SecurityDao;
import com.fedevela.persistencia.utilerias.SHA1;
import net.codicentro.core.TypeCast;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import com.fedevela.mx.asic.security.services.AuthenticationService;
import com.fedevela.mx.asic.security.services.CardService;
import com.fedevela.mx.asic.security.services.UserService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

//    @Resource
//    private SecurityDao dao;

    @Resource
    private DmsDao dao;

    @Autowired
    private UserService userService;
    @Autowired
    private CardService cardService;

    @Override
    public boolean allowAccess(final String username, final BigInteger position, final String digits) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TarjetaWebmx.class);
        criteria.add(Restrictions.eq("status", 'A'));
        criteria.add(Restrictions.eq("login", username));
        Short positionS = new Short( position.toString() );
        List<TarjetaWebmx> tws = dao.find(criteria);
        if ((tws == null) || (tws.isEmpty()) || (tws.size() > 1)) {
            return false;
        }
        ClaveWebmx cw = dao.get(ClaveWebmx.class, new ClaveWebmxPK(tws.get(0).getIdtarjeta(), positionS));
        if ((cw == null) || (TypeCast.isBlank(cw.getCaracteres()))) {
            return false;
        }
        return cw.getCaracteres().toLowerCase().equals(digits.toLowerCase());
    }

    @Override
    public String validarAcceso(final String login, final String pas, final String idAuthority) {
        return validaAcceso( login, pas, idAuthority, null);
    }

    @Override
    public String validarAcceso(final String login, final String pas, final String idAuthority, final String idAuthorityAdeadms) {
        return validaAcceso( login, pas, idAuthority, idAuthorityAdeadms);
    }

    @Override
    public BloqueoUsuarioWebmx getUsuarioBloqueo( final String usuario ) {
        return dao.get( BloqueoUsuarioWebmx.class, usuario );
    }

    @Override
    public void saveUsuarioBloqueo( BloqueoUsuarioWebmx usuario ) throws Exception {
        dao.persist( usuario );
    }

    @Override
    public void deleteUsuarioBloqueo( BloqueoUsuarioWebmx usuario ) throws Exception {
        dao.delete( usuario );
    }

    private String validaAcceso( final String login, final String pas, final String idAuthority, final String idAuthorityAdeadms ) {
        try {
            String rs = null;
            UsuarioWebmx usuario = userService.getUser(login);
            if (usuario != null) {
                if (usuario.getStatus() == 'A') {
                    String sha1Password = SHA1.encriptarBase64(pas);
                    if (usuario.getPassword().equals(sha1Password)) {
                        usuario.setIntentos(TypeCast.toShort(0));
                        if (usuario.getAuthorities().contains(idAuthority) || usuario.getAuthorities().contains(idAuthorityAdeadms)) {
                            if (usuario.getFechaVigencia() == null || usuario.getFechaVigencia().before(new Date())) {
                                if (usuario.getNoAcceso() == null) {
                                    usuario.setNoAcceso(1L);
                                } else {
                                    usuario.setNoAcceso(usuario.getNoAcceso() + 1);
                                }
                                if (cardService.getCard(login) != null) {
                                    rs = "acceso";
                                } else {
                                    rs = "principal";
                                }
                            } else {
                                rs = "El usuario a expirado, favor de verificar vigencia.";
                            }
                        } else {
                            rs = "El usuario no cuenta con privilegios para accesar a esta aplicación.";
                        }
                    } else {
                        usuario.setIntentos(TypeCast.toShort(usuario.getIntentos() + 1));
                        if (usuario.getIntentos() >= 3) {
                            usuario.setStatus('R');
                            usuario.setFecharevocado(new Date());
                            rs = "Usuario revocado";
                        } else {
                            rs = "Contraseña incorrecta";
                        }
                    }
                } else if (usuario.getStatus() == 'R') {
                    rs = "Usuario revocado";
                } else {
                    rs = "Usuario incorrecto";
                }
                userService.saveUser(usuario);
            } else {
                rs = "Usuario incorrecto";
            }
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}
