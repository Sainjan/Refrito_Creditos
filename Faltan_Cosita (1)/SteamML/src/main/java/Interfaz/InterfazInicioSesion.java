/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaz;

import java.awt.Color;
import java.awt.event.*;
import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;
import javax.swing.JFrame; 
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.sql.SQLException;

import Principal.SteamML;
import Servicio.UsuarioServicio;
import Modelo.Usuario;

public class InterfazInicioSesion extends InterfazPrincipal implements ActionListener {
    JLabel  lb_nombre, lb_contrasena;
    JTextField txt_nombre;
    JPasswordField txt_contrasena; 
    JButton btn_ingresar, btn_registrar; 
    boolean validador_cierre = true;

    UsuarioServicio us;

    public InterfazInicioSesion(UsuarioServicio usServicio) 
    {
        super("Inicio sesión", 700, 450);

        this.us = usServicio;

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent wev)
            {
                if(validador_cierre)
                {
                    SteamML.abrir_InterfazInicio();
                }
            }
        });
    
        lb_nombre = new JLabel("Ingrese su nombre de usuario:");
        lb_nombre.setBounds(50,50,300,40);
        lb_nombre.setForeground(new Color(25, 25, 112)); 
        
        txt_nombre = new JTextField("Pepe");
        txt_nombre.setForeground(Color.LIGHT_GRAY);
        txt_nombre.setBounds(235,56,150,30);
        txt_nombre.addFocusListener(new FocusListener() {
            
            @Override
            public void focusGained(FocusEvent fe)
            {
                if(txt_nombre.getText() != "Pepe")
                {
                    txt_nombre.setText("");
                    txt_nombre.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent fev)
            {
                if(txt_nombre.getText().isEmpty())
                {
                    txt_nombre.setText("Pepe");
                    txt_nombre.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
        
        lb_contrasena = new JLabel("Ingrese su contraseña: ");
        lb_contrasena.setBounds(50,100,300,40);
        lb_contrasena.setForeground(new Color(25,25,112));
        
        txt_contrasena = new JPasswordField("contrasena123");
        txt_contrasena.setForeground(Color.LIGHT_GRAY);
        //txt_contrasena.setEchoChar('º');
        txt_contrasena.setBounds(188,108,200,30);
        txt_contrasena.addFocusListener(new FocusListener() {
            
            @Override
            public void focusGained(FocusEvent fe)
            {
                if(!txt_contrasena.getPassword().toString().equals("contrasena123"))
                {
                    txt_contrasena.setText("");
                    txt_contrasena.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent fev)
            {
                if(txt_contrasena.getPassword().toString().isEmpty())
                {
                    txt_contrasena.setText("contrasena123");
                    txt_contrasena.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
        
         btn_ingresar = new JButton("Ingresar");
         btn_ingresar.setBounds(175,180,150,50);
         btn_ingresar.setForeground(Color.WHITE);
         btn_ingresar.setBackground(new Color(0, 100, 60));
         
            btn_ingresar.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent me)
                {
                    btn_ingresar.setBackground(new Color(25, 25, 112));
                    btn_ingresar.setForeground(Color.WHITE);
                }

                @Override
                public void mouseExited(MouseEvent mev)
                {
                    btn_ingresar.setBackground(new Color(0, 100, 60));
                    btn_ingresar.setForeground(Color.WHITE);}
            });
            btn_ingresar.addActionListener(this);

        add(lb_nombre);
        add(txt_nombre);
        add(lb_contrasena);
        add(txt_contrasena);
        add(btn_ingresar);
         
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btn_ingresar) {

            String nom_obt = txt_nombre.getText();
            String con_obt = txt_contrasena.getPassword().toString();

            try {

                Integer resultado = this.us.iniciar_sesion(nom_obt, con_obt);

                if(!resultado.equals(-1)) {

                    if(this.us.obtTipoUs() == null) 
                    {
                        SteamML.abrir_InterfazAdmin();
                        validador_cierre = false;
                        this.dispose();
                    } else 
                    {
                        SteamML.abrir_InterfazCliente();
                        validador_cierre = false;
                        this.dispose();
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "El usuario indicado no existe en el sistema");
                }

            } catch(InvalidKeySpecException ikse) {
                JOptionPane.showMessageDialog(this, ikse);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, nfe);
            } catch (NoSuchAlgorithmException nsue) {
                JOptionPane.showMessageDialog(this, nsue);
            } catch (IOException iox)
            {
                JOptionPane.showMessageDialog(this, iox);
            } catch (SQLException sqle)
            {
                JOptionPane.showMessageDialog(this, sqle);
            }
            
        } else {
            SteamML.abrir_InterfazRegistro(0);
        }
    }
}