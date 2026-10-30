/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicio;

//IMPORTS DE HASHEO
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import java.sql.SQLException;

import Modelo.Usuario;
import BDD.UsuarioBDD;
import Modelo.Cargo;
import Modelo.Cliente;

public class UsuarioServicio {

    private UsuarioBDD usbd;
    private Usuario usur;

    public UsuarioServicio() throws SQLException
    {
        this.usbd = new UsuarioBDD();
    }

    public Usuario realizar_bus_us(String id) throws SQLException
    {

        int id_fin = 0;
        try 
        {
            id_fin = Integer.parseInt(id);
        } catch (NumberFormatException nfe) 
        {
            return null;
        }
        
        return this.usbd.buscar_usuario(id_fin);

    }

    public String realizar_bus_us(String nom, String con) throws InvalidKeySpecException, NoSuchAlgorithmException, SQLException 
    {
        return this.usbd.buscar_usuario(nom, con);
    }

    public String registrar_usuario(String nom, String cont, Cargo carg) throws InvalidKeySpecException, NoSuchAlgorithmException, SQLException {

        if((nom == null) || (cont == null))
        {
            return "Registro inválido. Complete todos los campos solicitados, por favor.";
        } else if(!nom.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*"))
        {
            return "Registro inválido. El nombre no sigue las reglas especificadas:\n debe ser únicamente texto, sin números ni caracteres especiales";
        }
        
        String buscador = realizar_bus_us(nom, null);  //Se busca solamente el nombre pues es el dato que no se puede repetir
                                                            //No es la forma más óptima, pero es necesario entrar a la BDD para saber si
                                                            //el usuario existe o no.

        if(!buscador.contains("No se encontró ningún usuario con esos datos")) {
            return buscador;
        } else
        {
            String respuesta = this.usbd.registrar_datos_usuario(nom,cont,carg);
            return respuesta;
        }
    }

    public int iniciar_sesion(String nom, String con) throws InvalidKeySpecException, NoSuchAlgorithmException, SQLException{

        String buscador = realizar_bus_us(nom, null); //Se busca solamente el nombre pues es el dato que no se puede repetir
                                                      //No es la forma más óptima, pero es necesario entrar a la BDD para saber si
                                                      //el usuario existe o no.

        if(buscador.contains("El nombre ingresado está en uso")) //Se espera esto porque la contraseña buscada es null; 
                                                                 // pues primero es preferible comparar que exista el usuario 
                                                                 // y después se hace el proceso de comparación de hasheos.
        {
            String permitido = String.valueOf(buscador.substring(31));
            try
            {
            
                int id_int = Integer.parseInt(permitido);
                Usuario busca = this.usbd.buscar_usuario(id_int);

                if(busca.verificador_con(con))
                {
                    this.usur = busca;
                    Logs_Intermedio.intermedio_carga_logs(busca.obtIdUsuario(), "El usuario " + String.valueOf(busca.obtIdUsuario()) + " inició sesión correctamente", "UsuarioServicio: iniciar_sesion", true);
                    return busca.obtIdUsuario();
                }

            } catch(NumberFormatException nfe)
            {
                throw new NumberFormatException("Ocurrió un error inesperado al intentar comparar la información del ID de usuario: ");
            }

        }

        return -1;

    }

    public String eliminarUsuario() throws SQLException
    {
        String msj;

        if(this.usur == null)
        {
            return "Aún no se cargó el usuario en el sistema. Por favor, inicie sesión para continuar.";
        } else if(this.usur.obtIdUsuario() == 1)
        {
            return "No se puede eliminar al Administrador del sistema.";
        }

        msj = this.usbd.eliminar_datos_usuario();
        if(msj.equals("¡Operación exitosa!"))
        {
            this.usur = null;
        }
        return msj;
    }
    
    public String modificar_usr(String nom, String contra) throws InvalidKeySpecException, NoSuchAlgorithmException, SQLException
    {
        if(this.usur == null)
        {
            return "Aún no se cargó el usuario en el sistema. Por favor, inicie sesión para continuar.";
        } else if(!nom.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*"))
        {
            return "Modificación inválida. El nombre no sigue las reglas especificadas:\n debe ser únicamente texto, sin números ni caracteres especiales";
        } else if ((nom.equals(this.usur.obtNomUs())) || (this.usur.verificador_con(contra)))
        {
            return "Modificación inválida. El nombre y la contraseña no pueden ser iguales a los actuales.";
        }

        if(!this.usbd.buscar_usuario(nom, null).contains("No se encontró ningún usuario con esos datos.")) {
            Logs_Intermedio.intermedio_carga_logs(this.usur.obtIdUsuario(), "El nombre ingresado ya existe en el sistema", "UsuarioServicio: modificar_usr", false);
            return "Modificación inválida. Los datos ingresados ya existen en el sistema. El nombre es único por cuenta.";
        }

        this.usur = this.usbd.modificar_usuariobdd(nom, contra);
        return "¡Se cambió correctamente la información del usuario!";
    }

    public int obtenerIDUsuario()
    {
        return this.usur.obtIdUsuario();
    }

    public String obtenerNomUs()
    {
        return this.usur.obtNomUs();
    }

    public Cliente obtTipoUs()
    {
        if(this.usur.obtEmpresa() != null)
        {
            return this.usur.obtEmpresa();
        } else if(this.usur.obtPersona() != null)
        {
            return this.usur.obtPersona();
        } else
        {
            return null;
        }
    }
}