package com.fedevela.asic.util;

/**
 * Created by fvelazquez on 9/04/14.
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FechasUtil {

    public Date StringToDate(String fecha) {

        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;

        try {
            formatoDelTexto.setLenient(false);
            date = formatoDelTexto.parse(fecha);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return date;
    }

    public static int restarFechas(Calendar fechaInicio, Calendar fechaFin) {
        int diasDiferencia = 0;

        //Las fehas son del mismo año
        if (fechaInicio.get(Calendar.YEAR) == fechaFin.get(Calendar.YEAR)) {
            diasDiferencia = fechaFin.get(Calendar.DAY_OF_YEAR) - fechaInicio.get(Calendar.DAY_OF_YEAR);
        } //Las fehas son de años diferentes
        else {

            long diferencia = fechaFin.getTimeInMillis() - fechaInicio.getTimeInMillis();
            try {
                diasDiferencia = (int) (diferencia / (1000 * 60 * 60 * 24));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return diasDiferencia;
    }

    /**
     * Le da formato a un Date segun el formato pasado como parametro
     * ejem. "yyyy-MM-dd"
     * @param date
     * @param formato
     * @return
     */
    public static String formatDate(Date date, String formato) {
        SimpleDateFormat formateador = new SimpleDateFormat(formato);

        return formateador.format(date);
    }

    /**
     * agrega el número de días a la fecha de entrada
     *
     * @param fecha actual
     * @param dias a agregar
     * @return fecha más los días agregados
     */
    public static Date agregarDias(Date fecha, int dias) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        c.add(Calendar.DATE, dias);

        return c.getTime();
    }
}
