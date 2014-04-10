package com.fedevela.services;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.avaluo.pojos.AvAvaluoCaja;
import com.fedevela.core.avaluo.pojos.AvDocumentoAvaluo;
import com.fedevela.core.avaluo.pojos.AvPaginaDocum;
import java.math.BigInteger;
import java.util.List;

public interface AvaluoService {

    /**
     *
     * @param nunicodoc
     * @return
     */
    public AvAvaluoCaja getAvaluoCaja(BigInteger nunicodoc);

    /**
     *
     * @param nunicodoc
     * @param nunicodoct
     * @return
     */
    public AvDocumentoAvaluo getDocumentoAvaluo(BigInteger nunicodoc, BigInteger nunicodoct);

    public boolean saveDocuentoAvaluo(AvDocumentoAvaluo documentoAvaluo);

    public boolean saveDocuentoAvaluo(List<AvDocumentoAvaluo> documentosAvaluo);

    public boolean savePaginaDocum(List<AvPaginaDocum> paginasDocum);

    public int countPaginaDocum(int nunicodoc);
}
