package com.fedevela.controlimagen.servicios.impl;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.asic.pojos.ChecklistDig;
import com.fedevela.core.asic.pojos.DocumentoSolicitadoWeb;
import com.fedevela.core.asic.pojos.ImgAvaluos;
import com.fedevela.core.asic.pojos.PaginaDig;
import com.fedevela.core.creditos.pojos.CreditosOrigenDet;
import com.fedevela.core.hn.pojos.HnMonitorDig;
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.asic.excepciones.ServicioException;
import com.fedevela.asic.util.TypeCast;
import com.fedevela.controlimagen.beans.ChecklistControlImagen;
import com.fedevela.controlimagen.beans.PaginaControlImagen;
import com.fedevela.controlimagen.servicios.ControlImagenServicio;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("controlImagenServicio")
@Deprecated
public class ControlImagenServicioImpl implements ControlImagenServicio {

    private Logger logger = LoggerFactory.getLogger(ControlImagenServicio.class);
    public static final long CLIENTE_BBVA_AURORA = 1L;
    public static final long CLIENTE_BBVA_TDC = 172L;
    public static final long CLIENTE_PRESER = 10L;
    public static final long CLIENTE_BBVAPENSIONES = 11L;
    public static final long CLIENTE_BBVASEGUROS = 18L;
    public static final long CLIENTE_PROMOTORABOSQUES = 20L;
    public static final long CLIENTE_HIPOTECARIANACIONAL = 37L;
    public static final long CLIENTE_BANCOFACIL = 41L;
    public static final long CLIENTE_BANCOCOMPARTAMOS = 44L;
    public static final long CLIENTE_UNIDADAVALUOSMEXICO = 60L;
    public static final long CLIENTE_BANCOMER = 69L;
    public static final long CLIENTE_TELEFONICA = 78L;
    public static final long CLIENTE_CERVECERIAMODELO = 80L;
    public static final long CLIENTE_LOREAL = 87L;
    public static final long CLIENTE_PENDULUM = 89L;
    public static final long CLIENTE_FOVISSTE = 91L;
    public static final long CLIENTE_PROVEEDORESVTM = 94L;
    public static final long CLIENTE_CREDITO_MAESTRO = 96L;
    public static final long CLIENTE_EXP_UNICO = 162L;
    protected static String RE_RUTA_IMAGEN = ""
            + "((http://){1}([0-9]{1,3}\\.){3}[0-9]{1,3}/)|"
            + "((http://){1}[0-9a-zA-z\\.]+/)|"
            + "^/";
    //0         1    2         3         4             5
    //ruta_base lote nunicodoc unicodoct nombre_imagen nunicosello
    protected static String FORMATO_NOMBRE_BBVA_AURORA = "{0}{1}/{2,number,U0000000000}/{3,number,T0000000000}/{4}";
    protected static String FORMATO_NOMBRE_TELEFONICA = "{0}{1}/{2,number,U0000000000}/{3,number,T0000000000}/{4}";
    protected static String FORMATO_NOMBRE_PROVEEDORESVTM = "{0}{1}/{2,number,U0000000000}/{3}";
    protected static String FORMATO_NOMBRE_BBVA_TDC = "{0}{1}/{2,number,U0000000000}/{4}";
    protected static String FORMATO_NOMBRE_BBVAPENSIONES = "{0}{1}/{4}";
    protected static String FORMATO_NOMBRE_FOVISSTE = "{0}{1}/{4}";
    protected static String FORMATO_NOMBRE_PENDULUM = "{0}/{4}";
    protected static String FORMATO_NOMBRE_LOREAL = "{0}{1}/{2,number,U0000000000}/{3,number,T0000000000}/{4}";
    protected static String FORMATO_NOMBRE_HN = "{0}{1}/{3,number,00000000000}/{4}";
    protected static String FORMATO_NOMBRE_AV = "{0}{1}/{2,number,U0000000000}/{4}";
    protected static String FORMATO_NOMBRE_AV_01 = "{0}{1}/{2,number,U0000000000}/{3,number,T000000000}/{4}";
    protected static String FORMATO_NOMBRE_AV_MF = "{0}{1}/{5,number,S00000000}/{2,number,U0000000000}/{4}";
    protected static String FORMATO_NOMBRE_AV_PD = "{0}{1}/{2,number,U0000000000}/{3,number,T000000000}/{4}";
    protected static String FORMATO_NOMBRE_BBVASEGUROS = "{0}{4}";
    protected static String FORMATO_NOMIMAG_FOVISSSTE = "{0,number,00000000000.pdf}";
    protected static String FORMATO_NOMIMAG_PENDULUM = "{0,number,T00000000000.pdf}";
    protected static String FORMATO_NOMIMAG_HN = "{0,number,00000000.TIF}";
    protected static String FORMATO_NOMIMAG_AV = "{0,number,T00000000000.tif}";
    protected static String FORMATO_NOMIMAG_AV_01 = "{0,number,00000000.tif}";
    protected static String FORMATO_NOMIMAG_AV_PD = "{0,number,00000000.TIF}";
    protected static String FORMATO_NOMIMAG_BBVASEGUROS = "{0}.tif";
    protected static String FORMATO_NOMBRE_EXP_UNICO = "{0}{1}/{2,number,U0000000000}/{3,number,T0000000000}/{4}";
    @Autowired
    private DmsDao dao;

    public ControlImagenServicioImpl() {
        Locale.setDefault(new Locale("es", "MX"));
    }

    @Override
    public List<ChecklistControlImagen> getChechlistCIByClienteU(Long scltcod, Long nunicodoc) throws ServicioException {
        List controlImagenRes = new ArrayList<ChecklistControlImagen>();
        List<ChecklistDig> checklist;
        List<ImgAvaluos> checklistAv;
        ChecklistControlImagen item;
        Integer imagenesActivas = TypeCast.toInteger(dao.find(new StringBuilder("SELECT COUNT(*) FROM PROD.PAGINA_DIG WHERE NUNICODOC=? AND NUNICODOCT=? AND ESTATUS='A'"),
                new Object[]{nunicodoc, 0L}).get(0));

        if (scltcod == CLIENTE_UNIDADAVALUOSMEXICO) {
            checklistAv = dao.find("from ImgAvaluos aa where aa.imgAvaluosPK.nunicodoc = ?", new Object[]{nunicodoc});
            for (Iterator<ImgAvaluos> it = checklistAv.iterator(); it.hasNext();) {
                ImgAvaluos avDocumentoAvaluo = it.next();
                item = new ChecklistControlImagen(
                        avDocumentoAvaluo.getImgAvaluosPK().getNunicodoc(),
                        avDocumentoAvaluo.getImgAvaluosPK().getNunicodoct(),
                        scltcod,
                        avDocumentoAvaluo.getRutaDestino(),
                        avDocumentoAvaluo.getImagenesDig(),
                        avDocumentoAvaluo.getImagenesTrans(),
                        imagenesActivas, // Imagenes activas
                        avDocumentoAvaluo.getLote(),
                        (short) avDocumentoAvaluo.getStatus(),
                        avDocumentoAvaluo.getUsuario(),
                        null);
                controlImagenRes.add(item);
            }
        } else {
            checklist = dao.find("from ChecklistDig cl where cl.checklistDigPK.nunicodoc = ? and cl.tipoDocumCte1Ph.tipoDocumCte1PhPK.scltcod = ?", new Object[]{nunicodoc, scltcod});
            for (Iterator<ChecklistDig> it = checklist.iterator(); it.hasNext();) {
                ChecklistDig checklistDig = it.next();
                item = new ChecklistControlImagen(
                        nunicodoc,
                        checklistDig.getChecklistDigPK().getNunicodoct(),
                        scltcod,
                        checklistDig.getRutaDestino(),
                        checklistDig.getImagenesDig(),
                        checklistDig.getImagenesTrans(),
                        imagenesActivas, // Imagenes activas
                        checklistDig.getLote(),
                        checklistDig.getStatus(),
                        checklistDig.getUsuario(),
                        checklistDig.getTipoDocumCte1Ph().getDocdesc());
                controlImagenRes.add(item);
            }
        }
        return controlImagenRes;
    }

    @Override
    public List<ChecklistControlImagen> getChechlistCIByClienteUT(Long scltcod, Long nunicodoc, Long nunicodoct) throws ServicioException {
        List controlImagenRes = new ArrayList<ChecklistControlImagen>();

        List<ChecklistDig> checklist;
        List<ImgAvaluos> checklistAv;
        List<HnMonitorDig> pagsHnMonitorDig;
        ChecklistControlImagen item;
        Integer imagenesActivas = TypeCast.toInteger(dao.find(new StringBuilder("SELECT COUNT(*) FROM PROD.PAGINA_DIG WHERE NUNICODOC=? AND NUNICODOCT=? AND ESTATUS='A'"),
                new Object[]{nunicodoc, nunicodoct}).get(0));
        if (scltcod == CLIENTE_BANCOMER
                || scltcod == CLIENTE_PRESER
                || scltcod == CLIENTE_PROMOTORABOSQUES
                || scltcod == CLIENTE_HIPOTECARIANACIONAL
                || scltcod == CLIENTE_BANCOFACIL
                || scltcod == CLIENTE_BANCOCOMPARTAMOS
                || scltcod == CLIENTE_CERVECERIAMODELO) {
            pagsHnMonitorDig = dao.find("from HnMonitorDig hmd where hmd.hnMonitorDigPK.nunicodoct = ?", new Object[]{nunicodoct});
            for (Iterator<HnMonitorDig> it = pagsHnMonitorDig.iterator(); it.hasNext();) {
                HnMonitorDig hnMonitorDig = it.next();
                item = new ChecklistControlImagen(
                        0L,
                        nunicodoct,
                        scltcod,
                        hnMonitorDig.getRutaImagen(),
                        hnMonitorDig.getNoImagenes(),
                        hnMonitorDig.getNoImagTrans(),
                        imagenesActivas, // Imagenes activas
                        hnMonitorDig.getLote(),
                        (short) hnMonitorDig.getStatus(),
                        hnMonitorDig.getUsuario(),
                        null);
                controlImagenRes.add(item);
            }
        } else if (scltcod == CLIENTE_UNIDADAVALUOSMEXICO) {
            checklistAv = dao.find("from ImgAvaluos aa where aa.imgAvaluosPK.nunicodoc = ? and aa.imgAvaluosPK.nunicodoct = ? and aa.cliente = ?", new Object[]{nunicodoc, nunicodoct, scltcod.intValue()});
            for (Iterator<ImgAvaluos> it = checklistAv.iterator(); it.hasNext();) {
                ImgAvaluos avDocumentoAvaluo = it.next();
                item = new ChecklistControlImagen(
                        avDocumentoAvaluo.getImgAvaluosPK().getNunicodoc(),
                        avDocumentoAvaluo.getImgAvaluosPK().getNunicodoct(),
                        scltcod,
                        avDocumentoAvaluo.getRutaDestino(),
                        avDocumentoAvaluo.getImagenesDig(),
                        avDocumentoAvaluo.getImagenesTrans(),
                        imagenesActivas, // Imagenes activas
                        avDocumentoAvaluo.getLote(),
                        (short) avDocumentoAvaluo.getStatus(),
                        avDocumentoAvaluo.getUsuario(),
                        null);
                controlImagenRes.add(item);
            }
        } else {
            checklist = dao.find("from ChecklistDig cl where cl.checklistDigPK.nunicodoc = ? and cl.checklistDigPK.nunicodoct = ? and cl.tipoDocumCte1Ph.tipoDocumCte1PhPK.scltcod = ?", new Object[]{nunicodoc, nunicodoct, scltcod});
            for (Iterator<ChecklistDig> it = checklist.iterator(); it.hasNext();) {
                ChecklistDig checklistDig = it.next();
                item = new ChecklistControlImagen(
                        nunicodoc,
                        checklistDig.getChecklistDigPK().getNunicodoct(),
                        scltcod,
                        checklistDig.getRutaDestino(),
                        checklistDig.getImagenesDig(),
                        checklistDig.getImagenesTrans(),
                        imagenesActivas, // Imagenes activas
                        checklistDig.getLote(),
                        checklistDig.getStatus(),
                        checklistDig.getUsuario(),
                        checklistDig.getTipoDocumCte1Ph().getDocdesc());
                controlImagenRes.add(item);
            }
        }

        return controlImagenRes;
    }

    private List<DocumentoSolicitadoWeb> getDocumentoSolicitadoWebPorIdDocto(final String idDocto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT");
        sql.append("        FOLIO,NUNICODOC,TIPO_DOCUMENTO,CAJA_ID,CAJA_ADEA,ID_DOCTO,TITULAR,NSS,CONSECUTIVO,CAPTURISTA,");
        sql.append("        PRODUCTO,FECHA_CAPTURA,FECHA_EMISION,FECHA_PUBLICACION,FECHA_CADUCIDAD,STATUS,PATH,OBSERVACIONES,");
        sql.append("        NO_IMAGENES,PATH_LOCAL");
        sql.append(" FROM PROD.documento_solicitado_web");
        sql.append(" WHERE id_docto = ?");
        return dao.find(DocumentoSolicitadoWeb.class, sql, new Object[]{idDocto});
    }

    @Override
    public List<PaginaControlImagen> getPaginaCIByClienteIddocto(Long scltcod, String idDocto) throws ServicioException {
        List<PaginaControlImagen> paginaCIRes = new ArrayList<PaginaControlImagen>();
        PaginaControlImagen item;
        String rutaBase;
        String archivo;
        if (scltcod == CLIENTE_BBVASEGUROS) {
            List<DocumentoSolicitadoWeb> doctos;
            doctos = getDocumentoSolicitadoWebPorIdDocto(idDocto);
            for (Iterator<DocumentoSolicitadoWeb> it = doctos.iterator(); it.hasNext();) {
                DocumentoSolicitadoWeb documentoSolicitadoWeb = it.next();
                item = new PaginaControlImagen(
                        1L,
                        MessageFormat.format(FORMATO_NOMIMAG_BBVASEGUROS, new Object[]{documentoSolicitadoWeb.getIdDocto()}),
                        null);
                paginaCIRes.add(item);
                rutaBase = obtenerRutaBase(documentoSolicitadoWeb.getPath());
                archivo = MessageFormat.format(FORMATO_NOMBRE_BBVASEGUROS, new Object[]{rutaBase, null, 0, 0, item.getNombreImagen()});
                item.cargaArchivo(archivo);
            }
        } else {
            throw new ServicioException("ERROR: Para el cliente solicitado no existe esta consulta");
        }

        return paginaCIRes;
    }

    @Override
    public List<PaginaControlImagen> getPaginaCIByClienteU(Long scltcod, Long nunicodoc) throws ServicioException {
        List<PaginaControlImagen> paginaCIRes = new ArrayList<PaginaControlImagen>();

        String rutaBase;
        String archivo;
        Integer imagenesActivas = TypeCast.toInteger(dao.find(new StringBuilder("SELECT COUNT(*) FROM PROD.PAGINA_DIG WHERE NUNICODOC=? AND NUNICODOCT=? AND ESTATUS='A'"),
                new Object[]{nunicodoc, 0L}).get(0));
        if (scltcod == CLIENTE_BBVA_AURORA || scltcod == CLIENTE_BBVA_TDC || scltcod == CLIENTE_TELEFONICA || scltcod == CLIENTE_BBVAPENSIONES || scltcod == CLIENTE_LOREAL || scltcod == CLIENTE_PROVEEDORESVTM) {
            ChecklistControlImagen checklist;
            PaginaControlImagen item;
            List<PaginaDig> paginas;
            paginas = dao.find("from PaginaDig pci where pci.paginaDigPK.nunicodoc = ?", new Object[]{nunicodoc});
            for (Iterator<PaginaDig> it = paginas.iterator(); it.hasNext();) {
                PaginaDig paginaDig = it.next();

                checklist = new ChecklistControlImagen(
                        paginaDig.getPaginaDigPK().getNunicodoc(),
                        paginaDig.getPaginaDigPK().getNunicodoct(),
                        scltcod,
                        paginaDig.getChecklistDig().getRutaDestino(),
                        paginaDig.getChecklistDig().getImagenesDig(),
                        paginaDig.getChecklistDig().getImagenesTrans(),
                        imagenesActivas, // Imagenes activas
                        paginaDig.getChecklistDig().getLote(),
                        paginaDig.getChecklistDig().getStatus(),
                        paginaDig.getChecklistDig().getUsuario(),
                        paginaDig.getChecklistDig().getTipoDocumCte1Ph().getDocdesc());

                item = new PaginaControlImagen(
                        paginaDig.getPaginaDigPK().getPagina(),
                        paginaDig.getNombreImagen(),
                        checklist);

                paginaCIRes.add(item);



                if (scltcod == CLIENTE_BBVAPENSIONES) {
                    rutaBase = obtenerRutaBase(paginaDig.getChecklistDig().getRutaDestino());
                    archivo = MessageFormat.format(FORMATO_NOMBRE_BBVAPENSIONES, new Object[]{rutaBase, paginaDig.getChecklistDig().getLote(), paginaDig.getPaginaDigPK().getNunicodoc(), paginaDig.getPaginaDigPK().getNunicodoct(), paginaDig.getNombreImagen()});
                    item.cargaArchivo(archivo);
                } else if (scltcod == CLIENTE_PROVEEDORESVTM) {
                    rutaBase = obtenerRutaBase(paginaDig.getChecklistDig().getRutaDestino());
                    archivo = MessageFormat.format(FORMATO_NOMBRE_PROVEEDORESVTM, new Object[]{rutaBase, paginaDig.getChecklistDig().getLote(), paginaDig.getPaginaDigPK().getNunicodoc(), paginaDig.getNombreImagen()});
                    item.cargaArchivo(archivo);
                }
            }
        } else if (scltcod == CLIENTE_FOVISSTE) {
            List<ChecklistControlImagen> checklist = getChechlistCIByClienteU(scltcod, nunicodoc);
            PaginaControlImagen item;

            for (Iterator<ChecklistControlImagen> it = checklist.iterator(); it.hasNext();) {
                ChecklistControlImagen checklistControlImagen = it.next();

                item = new PaginaControlImagen(
                        1L,
                        MessageFormat.format("{0,number,00000000000.pdf}", new Object[]{checklistControlImagen.getNunicodoct()}),
                        checklistControlImagen);

                paginaCIRes.add(item);
            }
        } else if (scltcod == CLIENTE_PENDULUM) {
            throw new ServicioException("ERROR: No se ha determinado la forma de guardar los archivos");
        } else if (scltcod == CLIENTE_PRESER) {
            throw new ServicioException("ERROR: No hay forma de obtener el nombre medianta una tabla");
        } else {
            throw new ServicioException("ERROR: Para el cliente solicitado no existe esta consulta");
        }

        return paginaCIRes;
    }

    @Override
    public List<PaginaControlImagen> getPaginaCIByClienteUT(Long scltcod, Long nunicodoc, Long nunicodoct) throws ServicioException {
        List<PaginaControlImagen> paginaCIRes = new ArrayList<PaginaControlImagen>();
        String rutaBase;
        String archivo;
        Integer imagenesActivas = TypeCast.toInteger(dao.find(new StringBuilder("SELECT COUNT(*) FROM PROD.PAGINA_DIG WHERE NUNICODOC=? AND NUNICODOCT=? AND ESTATUS='A'"),
                new Object[]{nunicodoc, nunicodoct}).get(0));
        if (scltcod == CLIENTE_BBVA_AURORA || scltcod == CLIENTE_BBVA_TDC || scltcod == CLIENTE_TELEFONICA || scltcod == CLIENTE_LOREAL || scltcod == CLIENTE_PROVEEDORESVTM) {
            ChecklistControlImagen checklist;
            PaginaControlImagen item;
            List<PaginaDig> paginas;

            paginas = dao.find("from PaginaDig pci where pci.paginaDigPK.nunicodoc = ? and pci.paginaDigPK.nunicodoct = ?", new Object[]{nunicodoc, nunicodoct});
            for (Iterator<PaginaDig> it = paginas.iterator(); it.hasNext();) {
                PaginaDig paginaDig = it.next();

                checklist = new ChecklistControlImagen(
                        paginaDig.getPaginaDigPK().getNunicodoc(),
                        paginaDig.getPaginaDigPK().getNunicodoct(),
                        scltcod,
                        paginaDig.getChecklistDig().getRutaDestino(),
                        paginaDig.getChecklistDig().getImagenesDig(),
                        paginaDig.getChecklistDig().getImagenesTrans(),
                        imagenesActivas, // Imagenes activas
                        paginaDig.getChecklistDig().getLote(),
                        paginaDig.getChecklistDig().getStatus(),
                        paginaDig.getChecklistDig().getUsuario(),
                        paginaDig.getChecklistDig().getTipoDocumCte1Ph().getDocdesc());

                item = new PaginaControlImagen(
                        paginaDig.getPaginaDigPK().getPagina(),
                        paginaDig.getNombreImagen(),
                        checklist);

                paginaCIRes.add(item);
            }
        } else if (scltcod == CLIENTE_BBVAPENSIONES) {
            throw new ServicioException("ERROR: Para el cliente solicitado no existe esta consulta");
        } else if (scltcod == CLIENTE_FOVISSTE) {
            List<ChecklistControlImagen> checklist = getChechlistCIByClienteUT(scltcod, nunicodoc, nunicodoct);
            PaginaControlImagen item;

            for (Iterator<ChecklistControlImagen> it = checklist.iterator(); it.hasNext();) {
                ChecklistControlImagen checklistControlImagen = it.next();

                item = new PaginaControlImagen(
                        1L,
                        MessageFormat.format(FORMATO_NOMIMAG_FOVISSSTE, new Object[]{checklistControlImagen.getNunicodoct()}),
                        checklistControlImagen);

                paginaCIRes.add(item);

                rutaBase = obtenerRutaBase(checklistControlImagen.getRutaDestino());
                archivo = MessageFormat.format(FORMATO_NOMBRE_FOVISSTE, new Object[]{rutaBase, checklistControlImagen.getLote(), nunicodoc, nunicodoct, item.getNombreImagen()});
                item.cargaArchivo(archivo);
            }
        } else if (scltcod == CLIENTE_PENDULUM) {
            List<ChecklistControlImagen> checklist = getChechlistCIByClienteUT(scltcod, nunicodoc, nunicodoct);
            PaginaControlImagen item;

            for (Iterator<ChecklistControlImagen> it = checklist.iterator(); it.hasNext();) {
                ChecklistControlImagen checklistControlImagen = it.next();

                item = new PaginaControlImagen(
                        1L,
                        MessageFormat.format(FORMATO_NOMIMAG_PENDULUM, new Object[]{checklistControlImagen.getNunicodoct()}),
                        checklistControlImagen);

                paginaCIRes.add(item);

                rutaBase = obtenerRutaBase(checklistControlImagen.getRutaDestino());
                archivo = MessageFormat.format(FORMATO_NOMBRE_PENDULUM, new Object[]{rutaBase, checklistControlImagen.getLote(), nunicodoc, nunicodoct, item.getNombreImagen()});
                item.cargaArchivo(archivo);
            }
            throw new ServicioException("ERROR: no está claro cómo se guardan los archivos pdf");
        } else {
            throw new ServicioException("ERROR: Para el cliente solicitado no existe esta consulta");
        }

        return paginaCIRes;
    }

    @Override
    public List<PaginaControlImagen> getPaginaCIClienteByUTPagina(Long scltcod, Long nunicodoc, Long nunicodoct, Long pagina) throws ServicioException {
        List<PaginaControlImagen> paginaCIRes = new ArrayList<PaginaControlImagen>();

        List<HnMonitorDig> pagsHnMonitorDig;

        String rutaBase;
        String archivo;
        String nombreImagen;

        Integer imagenesActivas = TypeCast.toInteger(dao.find(new StringBuilder("SELECT COUNT(*) FROM PROD.PAGINA_DIG WHERE NUNICODOC=? AND NUNICODOCT=? AND ESTATUS='A'"),
                new Object[]{nunicodoc, nunicodoct}).get(0));
        if (scltcod == CLIENTE_BBVA_AURORA
                || scltcod == CLIENTE_BBVA_TDC
                || scltcod == CLIENTE_TELEFONICA
                || scltcod == CLIENTE_LOREAL
                || scltcod == CLIENTE_PROVEEDORESVTM
                || scltcod == CLIENTE_EXP_UNICO) {
            ChecklistControlImagen checklist;
            PaginaControlImagen item;
            List<PaginaDig> paginas;
            StringBuilder sql = new StringBuilder("SELECT * FROM(");
            sql.append(" SELECT P.NUNICODOC,");
            sql.append("        P.NUNICODOCT,");
            sql.append("        ROWNUM PAGINA,");
            sql.append("        P.NOMBRE_IMAGEN,");
            sql.append("        P.ESTATUS");
            sql.append(" FROM PROD.PAGINA_DIG P");
            sql.append(" WHERE    P.ESTATUS	= 'A'");
            sql.append("      AND P.NUNICODOC	= ?");
            sql.append("      AND P.NUNICODOCT	= ?");
            sql.append(") WHERE PAGINA=?");

            paginas = dao.find(PaginaDig.class, sql, new Object[]{nunicodoc, nunicodoct, pagina});
            for (Iterator<PaginaDig> it = paginas.iterator(); it.hasNext();) {
                PaginaDig paginaDig = it.next();

                checklist = new ChecklistControlImagen(
                        paginaDig.getPaginaDigPK().getNunicodoc(),
                        paginaDig.getPaginaDigPK().getNunicodoct(),
                        scltcod,
                        paginaDig.getChecklistDig().getRutaDestino(),
                        paginaDig.getChecklistDig().getImagenesDig(),
                        paginaDig.getChecklistDig().getImagenesTrans(),
                        imagenesActivas, // Imagenes activas
                        paginaDig.getChecklistDig().getLote(),
                        paginaDig.getChecklistDig().getStatus(),
                        paginaDig.getChecklistDig().getUsuario(),
                        paginaDig.getChecklistDig().getTipoDocumCte1Ph().getDocdesc());

                item = new PaginaControlImagen(
                        paginaDig.getPaginaDigPK().getPagina(),
                        paginaDig.getNombreImagen(),
                        checklist);

                paginaCIRes.add(item);

                if (scltcod == CLIENTE_TELEFONICA) {
                    rutaBase = obtenerRutaBase(paginaDig.getChecklistDig().getRutaDestino());
                    archivo = MessageFormat.format(FORMATO_NOMBRE_TELEFONICA, new Object[]{rutaBase, paginaDig.getChecklistDig().getLote(), paginaDig.getPaginaDigPK().getNunicodoc(), paginaDig.getPaginaDigPK().getNunicodoct(), paginaDig.getNombreImagen()});
                    item.cargaArchivo(archivo);
                } else if (scltcod == CLIENTE_BBVA_AURORA) {
                    rutaBase = obtenerRutaBase(paginaDig.getChecklistDig().getRutaDestino());
                    archivo = MessageFormat.format(FORMATO_NOMBRE_BBVA_AURORA, new Object[]{rutaBase, paginaDig.getChecklistDig().getLote(), paginaDig.getPaginaDigPK().getNunicodoc(), paginaDig.getPaginaDigPK().getNunicodoct(), paginaDig.getNombreImagen()});
                    item.cargaArchivo(archivo);
                } else if (scltcod == CLIENTE_BBVA_TDC) {
                    rutaBase = obtenerRutaBase(paginaDig.getChecklistDig().getRutaDestino());
                    archivo = MessageFormat.format(FORMATO_NOMBRE_BBVA_TDC, new Object[]{rutaBase, paginaDig.getChecklistDig().getLote(), paginaDig.getPaginaDigPK().getNunicodoc(), paginaDig.getPaginaDigPK().getNunicodoct(), paginaDig.getNombreImagen()});
                    item.cargaArchivo(archivo);
                } else if (scltcod == CLIENTE_LOREAL) {
                    rutaBase = obtenerRutaBase(paginaDig.getChecklistDig().getRutaDestino());
                    archivo = MessageFormat.format(FORMATO_NOMBRE_LOREAL, new Object[]{rutaBase, paginaDig.getChecklistDig().getLote(), paginaDig.getPaginaDigPK().getNunicodoc(), paginaDig.getPaginaDigPK().getNunicodoct(), paginaDig.getNombreImagen()});
                    item.cargaArchivo(archivo);
                } else if (scltcod == CLIENTE_PROVEEDORESVTM) {
                    rutaBase = obtenerRutaBase(paginaDig.getChecklistDig().getRutaDestino());
                    archivo = MessageFormat.format(FORMATO_NOMBRE_PROVEEDORESVTM, new Object[]{rutaBase, paginaDig.getChecklistDig().getLote(), paginaDig.getPaginaDigPK().getNunicodoc(), paginaDig.getNombreImagen()});
                    item.cargaArchivo(archivo);
                }
            }
        } else if (scltcod == CLIENTE_BANCOMER
                || scltcod == CLIENTE_PRESER
                || scltcod == CLIENTE_PROMOTORABOSQUES
                || scltcod == CLIENTE_HIPOTECARIANACIONAL
                || scltcod == CLIENTE_BANCOFACIL
                || scltcod == CLIENTE_BANCOCOMPARTAMOS
                || scltcod == CLIENTE_CERVECERIAMODELO) {
            pagsHnMonitorDig = dao.find("from HnMonitorDig hmd where hmd.hnMonitorDigPK.nunicodoct = ?", new Object[]{nunicodoct});
            for (Iterator<HnMonitorDig> it = pagsHnMonitorDig.iterator(); it.hasNext();) {
                HnMonitorDig hnMonitorDig = it.next();
                PaginaControlImagen itemPagina;
                ChecklistControlImagen itemChecklist;

                nombreImagen = MessageFormat.format(FORMATO_NOMIMAG_HN, new Object[]{pagina});

                itemChecklist = new ChecklistControlImagen(
                        0L,
                        nunicodoct,
                        scltcod,
                        hnMonitorDig.getRutaImagen(),
                        hnMonitorDig.getNoImagenes(),
                        hnMonitorDig.getNoImagTrans(),
                        imagenesActivas, // Imagenes activas
                        hnMonitorDig.getLote(),
                        (short) hnMonitorDig.getStatus(),
                        hnMonitorDig.getUsuario(),
                        null);

                itemPagina = new PaginaControlImagen(
                        pagina,
                        nombreImagen,
                        itemChecklist);

                paginaCIRes.add(itemPagina);

                rutaBase = obtenerRutaBase(hnMonitorDig.getRutaImagen());
                archivo = MessageFormat.format(FORMATO_NOMBRE_HN, new Object[]{rutaBase, hnMonitorDig.getLote(), 0L, nunicodoct, nombreImagen});
                itemPagina.cargaArchivo(archivo);
            }
        } else if (scltcod == CLIENTE_UNIDADAVALUOSMEXICO) {
            List<ImgAvaluos> checklistAv = dao.find("from ImgAvaluos aa where aa.imgAvaluosPK.nunicodoc = ? and aa.imgAvaluosPK.nunicodoct = ? and aa.cliente = ?", new Object[]{nunicodoc, nunicodoct, scltcod.intValue()});
            ChecklistControlImagen itemChecklist;
            PaginaControlImagen itemPagina;

            for (Iterator<ImgAvaluos> it = checklistAv.iterator(); it.hasNext();) {
                ImgAvaluos avDocumentoAvaluo = it.next();
                itemChecklist = new ChecklistControlImagen(
                        avDocumentoAvaluo.getImgAvaluosPK().getNunicodoc(),
                        avDocumentoAvaluo.getImgAvaluosPK().getNunicodoct(),
                        scltcod,
                        avDocumentoAvaluo.getRutaDestino(),
                        avDocumentoAvaluo.getImagenesDig(),
                        avDocumentoAvaluo.getImagenesTrans(),
                        imagenesActivas, // Imagenes activas
                        avDocumentoAvaluo.getLote(),
                        (short) avDocumentoAvaluo.getStatus(),
                        avDocumentoAvaluo.getUsuario(),
                        null);

                rutaBase = obtenerRutaBase(avDocumentoAvaluo.getRutaDestino());

                if (nunicodoct < 10L) {
                    nombreImagen = MessageFormat.format(FORMATO_NOMIMAG_AV_01, new Object[]{pagina});
                } else {
                    nombreImagen = MessageFormat.format(FORMATO_NOMIMAG_AV, new Object[]{nunicodoct});
                }

                if (avDocumentoAvaluo.getOrigen().equals("AV_DOCUMENTO_AVALUO")) {
                    if (nunicodoct < 10L) {
                        archivo = MessageFormat.format(FORMATO_NOMBRE_AV_01, new Object[]{rutaBase, avDocumentoAvaluo.getLote(), nunicodoc, nunicodoct, nombreImagen});
                    } else {
                        archivo = MessageFormat.format(FORMATO_NOMBRE_AV, new Object[]{rutaBase, avDocumentoAvaluo.getLote(), nunicodoc, nunicodoct, nombreImagen});
                    }
                } else if (avDocumentoAvaluo.getOrigen().equals("AV_MONITOR_FACTURA")) {
                    archivo = MessageFormat.format(FORMATO_NOMBRE_AV_MF, new Object[]{rutaBase, avDocumentoAvaluo.getLote(), nunicodoc, nunicodoct, nombreImagen, avDocumentoAvaluo.getNunicosello().longValue()});
                } else if (avDocumentoAvaluo.getOrigen().equals("AV_PERITO_DOCUM")) {
                    nombreImagen = MessageFormat.format(FORMATO_NOMIMAG_AV_PD, new Object[]{pagina});
                    archivo = MessageFormat.format(FORMATO_NOMBRE_AV_PD, new Object[]{rutaBase, avDocumentoAvaluo.getLote(), nunicodoc, nunicodoct, nombreImagen});
                } else {
                    throw new ServicioException("ERROR: Origen no conocido en ImgAvaluos");
                }

                itemPagina = new PaginaControlImagen(
                        pagina,
                        nombreImagen,
                        itemChecklist);

                paginaCIRes.add(itemPagina);

                itemPagina.cargaArchivo(archivo);
            }
        } else {
            throw new ServicioException("ERROR: Para el cliente solicitado no existe esta consulta");
        }

        return paginaCIRes;
    }

    @Override
    public List<PaginaControlImagen> getPaginaCI(Object[] parametros) throws ServicioException {
        List<PaginaControlImagen> paginaCIRes = new ArrayList<PaginaControlImagen>();
        Long scltcod;

        scltcod = (Long) parametros[0];

        if (scltcod == CLIENTE_CREDITO_MAESTRO) {
            String idSolicitud;
            short tipo;
            String nombre;
            PaginaControlImagen itemPagina;

            idSolicitud = (String) parametros[1];
            tipo = (Short) parametros[2];
            nombre = "%" + (String) parametros[3];

            Collection<CreditosOrigenDet> creditos;
            CreditosOrigenDet credito = null;

            creditos = dao.find("SELECT c FROM CreditosOrigenDet c WHERE c.creditosOrigenDetPK.idSolicitud = ? AND c.creditosOrigenDetPK.tipo = ? AND c.creditosOrigenDetPK.rutaDestino LIKE ?", new Object[]{idSolicitud, tipo, nombre});

            if (creditos != null && creditos.size() > 0) {
                credito = creditos.iterator().next();

                itemPagina = new PaginaControlImagen(
                        null,
                        credito.getCreditosOrigenDetPK().getRutaDestino(),
                        null);

                paginaCIRes.add(itemPagina);

                itemPagina.cargaArchivo(credito.getCreditosOrigenDetPK().getRutaDestino());
            }
        }
        return paginaCIRes;
    }

    private static String obtenerRutaBase(String rutaDestino) {
        String rutaBase = null;
        if (rutaDestino != null) {
            Pattern pat = Pattern.compile(RE_RUTA_IMAGEN);
            Matcher mat = pat.matcher(rutaDestino);
            if (mat.find()) {
                rutaBase = mat.replaceAll("/");
            }
        }

        return rutaBase;
    }

    public static void main(String args[]) {
        Locale.setDefault(new Locale("es", "MX"));

        String rutaDestino;

        rutaDestino = "http://www.adea.com.mx/uno/dos/";
        rutaDestino = "http://164.4.16.164/tres/cuatro/";
        rutaDestino = "/cinco/seis/";
        rutaDestino = "http://www.adea.com.mx/emc2a/hnt/";
        rutaDestino = "http://221.211.14.5/emc2a/hnt/";
        System.out.println(obtenerRutaBase(rutaDestino));

        Object[] arguments = {new Integer(7), new java.util.Date(System.currentTimeMillis()), "a disturbance in the Force"};
        String result = MessageFormat.format("At {1,time} on {1,date}, there was {2} on planet {0,number,U00000.TIF}", arguments);
        System.out.println(result);
    }
}
