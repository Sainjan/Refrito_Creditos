package Servicio;

import java.sql.SQLException;

import BDD.Logs;

public class Logs_Intermedio {
    
    public Logs_Intermedio()
    {}

    /*public static ArrayList<Logs_Intermedio> listador_log(String fecha, String proc, boolean estado, boolean mmc) throws SQLException//ACORDARSE DE TRATAR EN INTERFAZ EL IllegalArgumentException
    {
        try
        {
            LocalDate fecha_confirma;
            if(fecha.equals("0000-00-00")) //Esta validación se hace porque,
            {                              // en caso de que la fecha ingresada sea 
                                           //posterior a la actual, se listan todas las
                                           //fechas. Esto se hace para evitar hacer una
                                           //sobrecarga y así no modificar el UML.
                LocalDate fecha_actual = LocalDate.now();
                fecha_confirma = fecha_actual.plusDays(1);
            } else
            {
                DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                fecha_confirma = LocalDate.parse(fecha, formateador);
            }
            return Logs.extractor_logs(fecha_confirma, proc, estado, mmc);
        } catch (DateTimeParseException dtpe)
        {
            throw new DateTimeParseException("Ocurrió un error inesperado con el formato de la fecha: " + dtpe, "", 0);
        }
    }*/
   
    public static boolean intermedio_carga_logs(Integer id_u, String men, String obj, boolean estado) throws SQLException
    {
        return Logs.carga_log(id_u, men, obj, estado);
    }
}