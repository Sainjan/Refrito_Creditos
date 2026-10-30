/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package Principal;

import Servicio.UsuarioServicio;
import BDD.ConexionBDD;
import BDD.Logs;
import Servicio.SolicitudServicio;

import Interfaz.*;


import java.time.format.DateTimeParseException;

//IMPORTS DE ARCHIVOS

import java.io.IOException;

//IMPORTS DE SQL

import java.sql.SQLException;

//IMPORTS DE INTERFAZ

import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;


public class SteamML {
   
    static UsuarioServicio us;
    static Logs log;
    static ConexionBDD con_bd;
    static SolicitudServicio sol;
    
    public static void main(String[] args) {

        try
        {
            SteamML.con_bd = new ConexionBDD();
        } catch (SQLException sqle)
        {
            System.err.println(sqle);
        } catch (IOException iox_sql)
        {
            System.err.println(iox_sql);
        }

        try 
        {
            SteamML.us = new UsuarioServicio();
            
        } catch (NumberFormatException nfe) 
        {
            System.err.println(nfe);
        } catch (DateTimeParseException dtpe)
        {
            System.err.println(dtpe);
        }catch(Exception e){//Atrapamos el error de la Red Neuronal---------------
            System.err.println(e);
        }
        
        try 
        {
            SteamML.log = new Logs();
        } catch(SQLException sqlexcepcion)
        {
            System.err.println(sqlexcepcion);
        } catch(Exception excp)
        {
            System.err.println(excp);
        }

        try
        {
            SteamML.sol = new SolicitudServicio();
        } catch (SQLException sqlex)
        {
            System.err.println(sqlex);
        } catch (Exception excepto)
        {
            System.err.println(excepto);
        }

        //INICIO DE PROGRAMA

        abrir_InterfazInicio();
            
    }
    
    public static void abrir_InterfazInicio()
    {
        SwingUtilities.invokeLater(new Runnable() {

        @Override
        public void run() {
            InterfazInicio ini = new InterfazInicio();
            ini.setVisible(true);
            }
        });
    }

    public static void abrir_InterfazRegistro(int llamador) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                InterfazRegistro inre = new InterfazRegistro(llamador);
                inre.setVisible(true);
            }

        });
    }

    public static void abrir_InterfazInicioSesion() {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                InterfazInicioSesion inins = new InterfazInicioSesion();
                inins.setVisible(true);
            }

        });
    }


    public static void abrir_InterfazAdmin() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                InterfazAdmin inad = new InterfazAdmin();
                inad.setVisible(true);
            }

        });
    }

    public static void abrir_InterfazModificarUsuario(boolean indicador) {
       SwingUtilities.invokeLater(new Runnable() 
       {
            @Override
            public void run() {
                InterfazModificarUsuario inmous = new InterfazModificarUsuario(indicador);
                inmous.setVisible(true);
            }

        });
    }
     
    public static void abrir_EliminarUser(boolean origen) {
       SwingUtilities.invokeLater(new Runnable() 
       {
            @Override
            public void run() {
                EliminarUser elus = new EliminarUser(origen);
                elus.setVisible(true);
            }

        });
    }
    
    public static void abrir_InterfazInfoSolicitud(Integer id) {
       SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try
                {
                    InterfazInfoSolicitud ininfso = new InterfazInfoSolicitud(id, SteamML.us);
                    ininfso.setVisible(true);
                } catch (IllegalArgumentException ilae)
                {
                    JOptionPane.showMessageDialog(null, ilae.getMessage()); //Hago getMessage porque queda feo mostrar como "java.lang.error" al usuario.
                                                                            //Este es un error probable al ser generado intencionalmente como advertencia
                } catch (SQLException sqleee)
                {
                    JOptionPane.showMessageDialog(null, sqleee);
                }
            }

        });
    }

    public static void abrir_InterfazSolicitudesUsuario() {
        SwingUtilities.invokeLater(() -> {
            InterfazSolicitudesUsuario insous = new InterfazSolicitudesUsuario();
            insous.setVisible(true);
        });
    }

    public static void abrir_InterfazVerificaContrasena(int llamador, int direccionador)
    {
        SwingUtilities.invokeLater(() -> {
            InterfazVerificaContrasena invc = new InterfazVerificaContrasena(llamador, direccionador);
            invc.setVisible(true);
        });
    }
}