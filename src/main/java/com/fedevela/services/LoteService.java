package com.fedevela.services;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.asic.pojos.Lote;
import java.util.Date;

public interface LoteService {

    public boolean saveLote(Lote lote);

    public Lote validateLote(Short scltcod, Short operatoria, String login, Date now, Lote lote);

    public Lote updateNoItems(Lote lote);
}
