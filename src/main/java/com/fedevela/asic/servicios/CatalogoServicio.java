package com.fedevela.asic.servicios;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.core.asic.pojos.Clientes;
import com.fedevela.core.asic.pojos.FlowOperatoria;
import com.fedevela.core.asic.pojos.Personas;
import com.fedevela.core.asic.pojos.Sucursal;
import com.fedevela.asic.excepciones.ServicioException;
import java.util.List;

/**
 *
 * @author fvilla
 */
public interface CatalogoServicio {

    /**
     * @deprecated - Usar el métido getClientes
     * @return
     */
    public List<Clientes> getAllClientes();

    public List<Clientes> getClientes();

    public List<Clientes> getClientes(final String nombre);

    public Clientes getClienteByScltcod(final Long scltcod);

    public List<FlowOperatoria> getOperatorias(final Long scltcod);

    public List<Sucursal> getSucursal(final Long scltcod);

    public Personas getPersona(long codigo) throws ServicioException;

    public Personas getPersonaByDescription(String descripcion) throws ServicioException;
}
