package Servicio;
 
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import Modelo.Solicitud;
import BDD.SolicitudBDD;
import Modelo.Usuario;
 
public class SolicitudServicio {
    
    // ATRIBUTOS DE LA CLASE              
    Integer idUsuarioResponsable;
    SolicitudBDD solbd;
    static String soloLetras = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$";
 
    public SolicitudServicio(){
        this.solbd = new SolicitudBDD();
    }
 
    public static Solicitud informe(Integer id_soli) throws SQLException {
        return SolicitudBDD.extractor_sol(id_soli);
    }
    
    public static boolean solicitar_credito(Integer idUsuario, String nombre, String ingresosVerificar, String gastosVerificar) throws IOException, SQLException {

        Usuario usuarioActual = Usuario.buscarU(idUsuario); 
        
        if (usuarioActual != null && usuarioActual.numero_intentos >= 3) {
            return false;
        }
        
        double ingreso;
        double gasto;
        
        try {
        
            ingreso = Double.parseDouble(ingresosVerificar);
            gasto = Double.parseDouble(gastosVerificar);

            if (ingreso < 0 || gasto < 0) {
                throw new IllegalArgumentException("Los saldos no pueden ser negativos.");
            }
        
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException("Error al procesar los valores numéricos: " + nfe.getMessage());
        }
        
        String estado = "Pendiente";

        if (!SolicitudBDD.carga_solicitud(idUsuario, nombre, ingreso, gasto, estado)) {
            return false;
        }
        
        Logs_Intermedio.intermedio_carga_logs(idUsuario, "Solicitud cargada exitosamente en la base de datos.", "Solicitud: solicitar_credito", true);
        return true;
    }
    
    public static Solicitud expande_solicitud(String id_seleccionado) throws SQLException {
        int idSoli;
        try {
            idSoli = Integer.parseInt(id_seleccionado.trim());
            if (idSoli <= 0) {
                throw new IllegalArgumentException("ID indicado inválido. Por favor, ingrese un valor entero positivo.");
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException("El formato del ID es inválido. Ingrese un número entero positivo.");
        }
 
        Solicitud solicitudEncontrada = SolicitudBDD.extractor_sol(idSoli);
 
        if (solicitudEncontrada != null && solicitudEncontrada.obtEstado().equals("Pendiente")) {
            return solicitudEncontrada;
        } else {
            throw new IllegalArgumentException("No se encontró una solicitud pendiente con ese ID y estado aún 'Pendiente'.");
        }
    }
 
    public static ArrayList<Solicitud> mostrar_creditos(String estado, String fecha, Integer id_responsable, boolean decide_men_mas) throws SQLException, IOException, Exception
    {
        try
        {
            LocalDate fec_real = LocalDate.parse(fecha);
            
            return SolicitudBDD.buscar_solicitudes(false,estado, fec_real, id_responsable, decide_men_mas);
 
        } catch (DateTimeParseException dtpe)
        {
            throw new DateTimeParseException("Ocurrió un error inesperado con el formato de la fecha ingresada: " + dtpe,"",0);
        }
    }
 
    public static boolean segregador(Integer id_cambia, boolean op) throws IOException, SQLException
    {
        Solicitud fila = SolicitudBDD.extractor_sol(id_cambia);
 
        if (fila == null) {
            return false;
        }
 
        int decision = 0;
        String valor_aprueba = "'Rechazado'";
        if(op)
        {
            decision = 1;
            valor_aprueba = "'Aprobado'";
        }

        if(SolicitudBDD.asignador_estado(id_cambia, decision))
        {
            if (decision == 1){
                fila.setEstado("Aprobado");
            }else{
                fila.setEstado("Rechazado");
            }
            
            Logs_Intermedio.intermedio_carga_logs(Usuario.id, "Se cambió exitosamente el estado de la solicitud ID = " + id_cambia.toString() + " a " + valor_aprueba, "Solicitud: segregador", true);
            return true;
 
        } else
        {
            Logs_Intermedio.intermedio_carga_logs(Usuario.id, "Ocurrió un error inesperado al intentar cambiar el estado de la solicitud", "SolicitudBDD: asignador_estado", false);
            return false;
        }
    }

} 
 

