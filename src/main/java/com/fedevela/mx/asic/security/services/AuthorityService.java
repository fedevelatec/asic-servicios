package com.fedevela.mx.asic.security.services;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.security.beans.PersonalAutorizadoBean;
import java.util.List;

public interface AuthorityService {

    /**
     *
     * @param idAuthority
     * @return
     */
    public List<PersonalAutorizadoBean> getPersonalAutorizado(final String idAuthority);
}
