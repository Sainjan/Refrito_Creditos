package BDD;

/**
 *
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBDD {
    private static File conexion = new File("ConexionBDD.txt");
    private String servidor;
    private String bdd;
    private String usuario;
    private String contrasena;
    private String url;
    protected static Connection cn;
    
    public ConexionBDD() throws IOException, SQLException
    {
        establecer_conexion();
    }
    
    private void establecer_conexion() throws IOException, SQLException
    {
        if (!conexion.exists()) {
            //throw new IOException("El archivo ConexionBDD.txt no existe en la ruta especificada.\nSiga el formato de 'servidor;base_de_datos;usuario;contraseña'");
            if (ConexionBDD.conexion.createNewFile())
            {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(ConexionBDD.conexion)))
                {
                    bw.write("localhost:3306;mysql;root;tuclave");//Cambiar según los datos de tu base de datos
                }
                return;
            } else
            {
                throw new IOException("El archivo ConexionBDD.txt no fué creado.");
            }
        } else
        {
            try(BufferedReader bf = new BufferedReader(new FileReader(ConexionBDD.conexion))){
                String leer_bf = bf.readLine();
                while(leer_bf != null){
                    String[] dividir_leer_bf = leer_bf.split(";");
                    if(dividir_leer_bf.length >= 4)
                    {
                        this.servidor = dividir_leer_bf[0];
                        this.bdd = dividir_leer_bf[1];
                        this.usuario = dividir_leer_bf[2];
                        this.contrasena = dividir_leer_bf[3];


                        this.url = "jdbc:mysql://" + this.servidor + "/" +  this.bdd;
                        
                        ConexionBDD.cn = DriverManager.getConnection(this.url, this.usuario, this.contrasena);
                    }
                    leer_bf = bf.readLine();
                }
            } catch(IOException e)
            {
                throw new IOException("Hubo un error abriendo el archivo ConexionBDD: " + e,e);
            } catch(SQLException sqle)
            {
                throw new SQLException("Hubo un error estableciendo conexión con la base de datos en el método establecer_conexion(): " + sqle);
            }
        }  
    }  
}
