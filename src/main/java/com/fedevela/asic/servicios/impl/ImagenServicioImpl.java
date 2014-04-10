package com.fedevela.asic.servicios.impl;

/**
 * Created by fvelazquez on 1/04/14.
 */
import com.fedevela.core.asic.definition.pojos.AdeamxDefinition;
import com.fedevela.core.asic.imagen.beans.Imagen;
import com.fedevela.core.asic.imagen.beans.Pagina;
import com.fedevela.core.asic.imagen.beans.PaginaPK;
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.asic.servicios.ImagenServicio;
import com.fedevela.asic.util.TypeCast;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImagenServicioImpl implements ImagenServicio {

    private Logger logger = LoggerFactory.getLogger(ImagenServicio.class);
    @Autowired
    private DmsDao dao;

    @Override
    public List<Imagen> getImagenes(final Long scltcod, final Long nunicodoc) {
        return getImagenes(scltcod, nunicodoc, null);
    }

    @Override
    public Imagen getImagen(final Long scltcod, final Long nunicodoc, final Long nunicodoct) {
        List<Imagen> rs = getImagenes(scltcod, nunicodoc, nunicodoct);
        if (rs == null || rs.isEmpty()) {
            return null;
        } else {
            if (rs.size() > 1) {
                logger.warn("Se han obtenidos mas de un resultado al obtener la imagen, solo se regresará el primer valor. " + rs.get(0).toString());
            }
            return rs.get(0);
        }
    }

    @Override
    public List<Imagen> getImagenes(final Long scltcod, final Long nunicodoc, final Long nunicodoct) {
        List<Object> params = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder(" SELECT A.* FROM PROD.VW_DIGITALIZACION A");
        sql.append(" WHERE A.NUNICODOC = ?");
        params.add(nunicodoc);
        if (nunicodoct != null) {
            sql.append(" AND A.NUNICODOCT = ?");
            params.add(nunicodoct);
        }
        if (scltcod != null) {
            sql.append(" AND A.SCLTCOD = ?");
            params.add(scltcod);
        }
        List<Imagen> imagenes = dao.find(Imagen.class, sql, params.toArray());
        if (imagenes != null && !imagenes.isEmpty()) {
            String formatoRutaImagen = getFormatoRutaImagen(scltcod);
            switch (scltcod.intValue()) {
                case 37:// HN
                    for (Imagen imagen : imagenes) {
                        final String substr = "www.adea.com.mx";
                        if (!TypeCast.isBlank(imagen.getRutaDestino()) && imagen.getRutaDestino().indexOf(substr) != -1) {
                            imagen.setRutaDestino(imagen.getRutaDestino().trim().substring(imagen.getRutaDestino().trim().indexOf(substr) + substr.length()));
                        }
                        List<Pagina> paginas = new ArrayList<Pagina>();
                        for (int idxPagina = 0; idxPagina < imagen.getImagenesTrans(); idxPagina++) {
                            Pagina pagina = new Pagina(new PaginaPK(imagen.getId().getNunicodoc(), imagen.getId().getNunicodoct(), idxPagina + 1));
                            pagina.setNombreImagen(TypeCast.CF("", "0", 8, idxPagina + 1));
                            pagina.setFormatoRutaImagen(formatoRutaImagen);
                            pagina.setRutaDestino(imagen.getRutaDestino());
                            pagina.setLote(imagen.getLote());
                            paginas.add(pagina);
                        }
                        imagen.setPaginas(paginas);
                    }
                    break;
                default:
                    sql = new StringBuilder("SELECT * FROM(");
                    sql.append(" SELECT P.NUNICODOC,");
                    sql.append("        P.NUNICODOCT,");
                    sql.append("        ROWNUM PAGINA,");
                    sql.append("        P.NOMBRE_IMAGEN,");
                    sql.append("        P.ESTATUS,");
                    sql.append("        ? FORMATO_RUTA_IMAGEN,");
                    sql.append("        ? RUTA_DESTINO,");
                    sql.append("        ? LOTE");
                    sql.append(" FROM PROD.PAGINA_DIG P");
                    sql.append(" WHERE    P.ESTATUS = 'A'");
                    sql.append("      AND P.NUNICODOC = ?");
                    sql.append("      AND P.NUNICODOCT = ?");
                    sql.append(") ORDER BY PAGINA ASC");
                    for (Imagen imagen : imagenes) {
                        params = new ArrayList<Object>();
                        /**
                         * PROJECTIONS
                         */
                        params.add(formatoRutaImagen);
                        params.add(imagen.getRutaDestino());
                        params.add(imagen.getLote());
                        /**
                         * RESTRICTIONS
                         */
                        params.add(imagen.getId().getNunicodoc());
                        params.add(imagen.getId().getNunicodoct());
                        imagen.setPaginas(dao.find(Pagina.class, sql, params.toArray()));
                    }
                    break;
            }
        }
        return imagenes;
    }

    private String getFormatoRutaImagen(final Long scltcod) {
        AdeamxDefinition definition = getDefinitionFormatoRutaImagen(scltcod);
        if (definition == null) {
            return null;
        } else {
            return definition.getFeature1();
        }
    }

    private AdeamxDefinition getDefinitionFormatoRutaImagen(final Long scltcod) {
        List<Object> params = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT CFG.* ");
        sql.append(" FROM PROD.ADEAMX_DEFINITION APP,");
        sql.append("      PROD.ADEAMX_DEFINITION CRU,");
        sql.append("      PROD.ADEAMX_DEFINITION CFG");
        sql.append(" WHERE    APP.TYPE_ID='APPLICATION'");
        sql.append("      AND APP.FEATURE='ADEAMX-WS-IMAGEN'");
        sql.append("      AND APP.ROW_STATUS='A'");
        sql.append("      AND CRU.PARENT_ID=APP.ID");
        sql.append("      AND CRU.TYPE_ID='CONFIG'");
        sql.append("      AND CRU.FEATURE='IMAGE-NAME-FORMAT'");
        sql.append("      AND CRU.ROW_STATUS='A'");
        sql.append("      AND CFG.PARENT_ID=CRU.ID");
        sql.append("      AND CFG.TYPE_ID='CONFIG'");
        sql.append("      AND CFG.ROW_STATUS='A'");
        sql.append("      AND CFG.FEATURE = ?");
        params.add(TypeCast.toString(scltcod));
        List<AdeamxDefinition> rs = dao.find(AdeamxDefinition.class, sql, params.toArray());
        if (rs == null || rs.isEmpty()) {
            return null;
        } else {
            return rs.get(0);
        }
    }

    @Override
    public byte[] getArchivo(final String archivo) throws IOException {
        return TypeCast.toBytes(new File(archivo));
    }

    @Override
    public Imagen getImagenPaginador(final Long scltcod, final Long nunicodoc, final Long nunicodoct, Integer start, Integer limit) {
        List<Imagen> rs = getImagenesPaginador(scltcod, nunicodoc, nunicodoct, start, limit);
        if (rs == null || rs.isEmpty()) {
            return null;
        } else {
            if (rs.size() > 1) {
                logger.warn("Se han obtenidos mas de un resultado al obtener la imagen, solo se regresará el primer valor. " + rs.get(0).toString());
            }
            return rs.get(0);
        }
    }

    @Override
    public List<Imagen> getImagenesPaginador(Long scltcod, Long nunicodoc, Long nunicodoct, Integer start, Integer limit) {
        List<Imagen> rs = getImagenes(scltcod, nunicodoc, nunicodoct);
        //TODO ver la manera en la que se puedan traer solamente n cantidad de paginas por imagenes.
        Integer pageCount = rs.size();
        int begin = (start == null) ? 0 : start;
        begin = (begin > pageCount) ? pageCount : begin;
        return getImagenes(scltcod, nunicodoc, nunicodoct).subList(begin, limit);

    }
}
