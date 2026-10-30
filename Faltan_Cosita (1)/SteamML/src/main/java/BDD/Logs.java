package BDD;

import java.sql.CallableStatement;
import java.sql.SQLException;


public class Logs {

    public Logs()
    {
    }

    public static boolean carga_log(Integer id_u, String men, String obj, boolean estado) throws SQLException
    {
        String sql = "{CALL Credito.Cargar_Logs(?, ?, ?, ?)}";
        int estado_in = estado ? 1 : 2;
 
        try (CallableStatement cs = ConexionBDD.cn.prepareCall(sql)) {
 
            cs.setString(1, men);
            cs.setString(2, obj);
            cs.setInt(3, estado_in);
 
            if (id_u != null) {
                cs.setInt(4, id_u);
            } else {
                cs.setNull(4, java.sql.Types.INTEGER);
            }
 
            cs.execute();
            return true;
 
        } catch (SQLException sqle) {
            throw new SQLException("Ocurrió un error inesperado al intentar cargar el log en MySQL: " + sqle.getMessage(), sqle);
        }
    }


    /*public static ArrayList<Logs_Intermedio> extractor_logs(LocalDate fec, String proc, boolean est, boolean men_mas_comp) throws SQLException
    {
        String sql = "{CALL SteaMLBDD.Buscar_Logs(?, ?, ?, ?, ?)}";
        ArrayList<Logs_Intermedio> lista_log = new ArrayList<>();
 
        int def_mas_menos = men_mas_comp ? 0 : 1;
        int estado_in = est ? 1 : 2;
 
        try (CallableStatement cs = ConexionBDD.cn.prepareCall(sql)) {
 
            cs.setDate(1, java.sql.Date.valueOf(fec));
            if (proc != null) {
                cs.setString(2, proc);
            } else {
                cs.setNull(2, java.sql.Types.VARCHAR);
            }
            cs.setInt(3, estado_in);
            cs.setInt(4, def_mas_menos);
            cs.setNull(5, java.sql.Types.INTEGER); 
 
            ResultSet rs = cs.executeQuery();
 
            while (rs.next()) {
                Integer id_log_registro = rs.getInt("ID_Log_Registro");
                LocalDate fecha_registro = rs.getObject("Fecha", LocalDate.class);
                String mensaje = rs.getString("Mensaje");
                String proceso = rs.getString("Proceso");
                boolean estado_registro = (rs.getInt("ID_Estado_Log") == 1);
                Integer id_usuario = (Integer) rs.getObject("ID_Usuario"); //Puede venir NULL
 
                lista_log.add(new Logs_Intermedio(id_log_registro, fecha_registro, mensaje, proceso, estado_registro, id_usuario));
            }
 
        } catch (SQLException sqle) {
            throw new SQLException("Ocurrió un error inesperado al intentar buscar logs en MySQL: " + sqle.getMessage(), sqle);
        }
 
        if ((lista_log != null) && (!lista_log.isEmpty()))
        {
            return lista_log;
        } else
        {
            throw new IllegalArgumentException("La lista de logs está vacía.");
        }
    }*/
}
