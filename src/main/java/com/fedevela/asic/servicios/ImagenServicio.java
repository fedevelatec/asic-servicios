package com.fedevela.asic.servicios;

/**
 * Created by fvelazquez on 31/03/14.
 */
import com.fedevela.core.asic.imagen.beans.Imagen;
import java.io.IOException;
import java.util.List;

public interface ImagenServicio {

    public List<Imagen> getImagenes(final Long scltcod, final Long nunicodoc);

    public List<Imagen> getImagenes(final Long scltcod, final Long nunicodoc, final Long nunicodoct);

    public Imagen getImagen(final Long scltcod, final Long nunicodoc, final Long nunicodoct);

    public byte[] getArchivo(final String archivo) throws IOException;

    public Imagen getImagenPaginador(final Long scltcod, final Long nunicodoc, final Long nunicodoct, Integer start, Integer limit);

    public List<Imagen> getImagenesPaginador(final Long scltcod, final Long nunicodoc, final Long nunicodoct, Integer start, Integer limit);
}
