package BDD;
 
//IMPORTS DE ARCHIVOS
 
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import Modelo.Solicitud;
import Modelo.Usuario;
 
public class SolicitudBDD {
 
    //private static Map<Integer, Solicitud> registro_sol = new HashMap<>();
    Integer id_soli = 0;

 
    public SolicitudBDD()
    {
        //llena_base_sol();
    }
    
    private static Solicitud mapear_fila_solicitud(ResultSet rs) throws SQLException {
        

        int idSolicitud = rs.getInt("ID_Solicitud");
        String nombre = rs.getString("Nombre");
        double ingresos = rs.getDouble("Ingresos");
        double gastos = rs.getDouble("Gastos");
        String estado = rs.getString("Estado");
        String fecha = rs.getString("Fecha");
        int idUsr = rs.getInt("idUsuario");

        return new Solicitud(idSolicitud, nombre, ingresos, gastos, estado, fecha, );
    }
 
    
    
    public static Solicitud extractor_sol(Integer id_sol) throws SQLException{
        String sql = "{CALL SteaMLBDD.Extractor_Solicitud(?, ?, ?)}";
 
        try (CallableStatement cs = ConexionBDD.cn.prepareCall(sql)) {
            cs.setInt(1, id_sol);
            cs.registerOutParameter(2, java.sql.Types.BOOLEAN); 
            cs.registerOutParameter(3, java.sql.Types.VARCHAR); 
 
            ResultSet rs = cs.executeQuery();    
 
            boolean exito = cs.getBoolean(2);
            String mensaje = cs.getString(3);
 
            // 1. Si falló en MySQL, lanzamos la excepción con el mensaje
            if (!exito) {
                throw new IllegalArgumentException(mensaje);
            }
 
            // 2. Si fue exitoso y trae filas
            if (rs.next()) {
                Solicitud soli = SolicitudBDD.mapear_fila_solicitud(rs);
                return soli;
            }
 
            return null;
 
        } catch (SQLException e) {
            throw new SQLException("Error al intentar buscar la solicitud con ID " + id_sol + " en extractor_sol(): " + e.getMessage(), e);
        }
    }
 
    public static ArrayList<Solicitud> buscar_solicitudes(boolean ban, String est, LocalDate fecha, Integer id_resp, boolean men_mas_fec) throws SQLException, IOException, Exception
    {
        String sql = "{CALL SteaMLBDD.Buscar_Solicitudes(?, ?, ?, ?, ?, ?, ?)}";//dos parametros son OUT, por eso son 7
        ArrayList<Solicitud> lista_solis = new ArrayList<>();
 
        try (CallableStatement cs = ConexionBDD.cn.prepareCall(sql)) {
 
            cs.setBoolean(1, ban);
            cs.setString(2, est);
            if (fecha != null) {
                cs.setDate(3, java.sql.Date.valueOf(fecha));
            } else {
                cs.setNull(3, java.sql.Types.DATE);
            }
            cs.setInt(4, id_resp);
            cs.setBoolean(5, men_mas_fec);
            cs.registerOutParameter(6, java.sql.Types.BOOLEAN);
            cs.registerOutParameter(7, java.sql.Types.VARCHAR);
 
            ResultSet rs = cs.executeQuery();
 
            boolean exito = cs.getBoolean(6);
            String mensaje = cs.getString(7);
 
            if (!exito) {
                throw new IllegalArgumentException(mensaje);
            }
 
            if(ban) // ESTA RUTA ES PARA GESTIONAR SOLICITUDES
            {
                while (rs.next()) {
                    Solicitud soliBuscada = mapear_fila_solicitud(rs);
 
                    if(soliBuscada.estado.equals("Pendiente")) {
                        Double ingresos = soliBuscada.ingresos;
                        Double gastos = soliBuscada.gastos; // o gastos
                        
                        // Validamos el monto máximo a pedir
                        double montoMaximo = ReglaMaestra.calcularMontoRMC3(ingresos, gastos);
                        
                        if(montoMaximo <= 0) {
                            // Si no da el margen, lo rechazamos automáticamente
                            asignador_estado(soliBuscada.idSolicitud, 0); 
                            
                            Logs_Intermedio.intermedio_carga_logs(
                                Usuario.id, 
                                "Se cambió el estado de la solicitud ID = " + soliBuscada.idSolicitud + " a 'Rechazado' por monto máximo menor o igual a 0",
                                "SolicitudBDD: buscar_solicitudes()",
                                true
                            );
                        } else {
                            // Si el monto es mayor a 0, NO llamamos a asignador_estado. 
                            // Lo dejamos pasar como "Pendiente" para que el administrador decida.
                        }
                    }
                    
                    lista_solis.add(soliBuscada);
                }   
            } else { // ESTA OTRA RUTA ES PARA MOSTRAR SOLICITUDES
                 while (rs.next()) {
                    lista_solis.add(mapear_fila_solicitud(rs));
                 }    
            } // FIN DEL TRATAMIENTO DE BANDERAS
            
            // Verificamos al final si la lista tiene elementos antes de retornar
            if (lista_solis != null && !lista_solis.isEmpty()) {
                return lista_solis;
            } else {
                throw new IllegalArgumentException("La lista de solicitudes está vacía.");
            }
        }catch(SQLException e){
            throw new SQLException("Error al intentar buscar solicitudes en buscar_solicitudes(): " + e.getMessage(), e);
        } 
    }
    
    
    public static boolean carga_solicitud(Integer id_responsable, String nombre, Double ingresos, Double gastos, String estado) throws SQLException, IOException {
        String sql = "{CALL Credito.Cargar_Solicitud(?, ?, ?, ?, ?, ?)}";
 
        try (CallableStatement cs = ConexionBDD.cn.prepareCall(sql)) {
            cs.setInt(1, id_responsable);                      
            cs.setString(2, nombre);                      
            cs.setDouble(3, ingresos);
            cs.setDouble(4, gastos);                      
            cs.setString(5, estado);   

            // Fecha actual
            LocalDate fechaActual = LocalDate.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            cs.setString(6, fechaActual.format(formato));
 
            cs.execute();
 
            Logs_Intermedio.intermedio_carga_logs(
                id_responsable,
                "Se cargó correctamente la solicitud en la base de datos MySQL",
                "SolicitudBDD: carga_solicitud()",
                true
            );
 
            return true;
 
        } catch (SQLException e) {
            Logs_Intermedio.intermedio_carga_logs(
                id_responsable,
                "Error al intentar insertar la solicitud en MySQL: " + e.getMessage(),
                "SolicitudBDD: carga_solicitud()",
                false
            );
            throw new SQLException("Error al insertar la solicitud en la base de datos: " + e.getMessage(), e);
        }
    }
 
    
 
    public static boolean asignador_estado(Integer idSoli, int opcion) throws IOException, SQLException
    {
        String estado = "Pendiente"; 
        if (opcion == 1) {                                                                                      
            estado = "Aprobado";                                                               
        } else if (opcion == 0) {                                                                                
            estado = "Rechazado";                                                              
        }                                                                                      
 
        String sql = "{CALL SteaMLBDD.Asignador_Estado(?, ?)}";
 
        try (CallableStatement cs = ConexionBDD.cn.prepareCall(sql)) {
            cs.setInt(1, idSoli);
            cs.setString(2, estado);
 
            cs.execute();
            return true;
 
        } catch (SQLException e) {
            Logs_Intermedio.intermedio_carga_logs(
            Usuario.id, 
            "Ocurrió un error en asignador_estado() para ID " + idSoli + ": " + e.getMessage(), // <--- Cambiado de id a idSoli
            "SolicitudBDD: asignador_estado()", 
            false
        );
            throw new SQLException("Error al intentar cambiar el estado de la solicitud en MySQL: " + e.getMessage(), e);
        }
    }
}
    
 

