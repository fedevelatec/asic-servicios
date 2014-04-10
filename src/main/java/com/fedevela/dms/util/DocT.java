package com.fedevela.dms.util;

/**
 * Created by fvelazquez on 9/04/14.
 */
import java.util.HashSet;
import java.util.Set;

import com.fedevela.core.asic.pojos.EtiqDocumHn;
import com.fedevela.asic.servicios.CapturaGeneralServicio;
import com.fedevela.asic.util.TypeCast;

public class DocT {

    private Set memory = null;
    private CapturaGeneralServicio servicio = null;
    private Short CUSTOMER_CODE = null;

    public DocT(CapturaGeneralServicio servicio, Short scltcod) {
        memory = new HashSet();
        CUSTOMER_CODE = scltcod;
        this.servicio = servicio;

    }

    public String addDocT(Object t) {
        try {
            EtiqDocumHn edh = servicio.getEtiqDocumHnPorCliente(TypeCast.toLong(t), TypeCast.toShort(CUSTOMER_CODE));
            if (memory.contains(t)) {
                memory.add(t);
            } else {
                return "LA ETIQUETA " + t + " YA ESTÁ CAPTURADA EN ESTE EXPEDIENTE.";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Ha ocurrido el siguiente error: " + ex.getMessage();
        }
        return null;
    }
}
