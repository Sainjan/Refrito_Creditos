/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaz;

import java.awt.Color;
import java.awt.event.*;
import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;

import javax.swing.JButton;
import javax.swing.JTextField;

import Principal.SteamML;
import Servicio.UsuarioServicio;
import Modelo.Cargo;

import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.sql.SQLException;

public class InterfazRegistro extends InterfazPrincipal implements ActionListener {
   JLabel  lb_nombre, lb_contrasena;
   JTextField txt_nombre;
   JPasswordField txt_contrasena;
   JButton btn_ingresar; 
   JComboBox<String> asignador_rol;

    boolean validador_cierre = true;
    int valor_pasaje;
    UsuarioServicio usuarioServ;

    public InterfazRegistro(int pasador, UsuarioServicio userv) {

        super("Registro", 700, 450);//Constructor

        this.usuarioServ = userv;

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent wev) {

                if((validador_cierre) && (pasador == 1)) 
                {
                    SteamML.abrir_InterfazInicio();
                } else if ((validador_cierre) && (pasador == 0))
                {
                    SteamML.abrir_InterfazInicioSesion();
                } else if((validador_cierre) && (pasador == 2))
                {
                    SteamML.abrir_InterfazAdmin();
                }

            } 

        });

        lb_nombre = new JLabel("Ingrese su nombre de usuario:");
        lb_nombre.setBounds(50,30,300,20);
        lb_nombre.setForeground(new Color(25, 25, 112));

        txt_nombre = new JTextField("Pepe");
        txt_nombre.setForeground(Color.LIGHT_GRAY);
        txt_nombre.setBounds(245,30,150,20);
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

        lb_contrasena = new JLabel("Ingrese su contraseña:");
        lb_contrasena.setBounds(50,110,300,20);
        lb_contrasena.setForeground(new Color(25, 25, 112));

        txt_contrasena = new JPasswordField("contrasena123");
        txt_contrasena.setForeground(Color.LIGHT_GRAY);
        //txt_contrasena.setEchoChar('°');
        txt_contrasena.setBounds(245,110,150,20);
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

        JLabel as_rol_lbl = new JLabel("Rol:");
        as_rol_lbl.setBounds(50,150,50,20);
        as_rol_lbl.setForeground(new Color(25,25,112));

        asignador_rol = new JComboBox<>(new String[]{Cargo.PERSONA.obtDescripcion(), Cargo.EMPRESA.obtDescripcion()});
        asignador_rol.setBounds(245,150,150,20);
        if(this.usuarioServ.obtenerIDUsuario() == 1)
        {
            asignador_rol.addItem(Cargo.ADMINISTRADOR.obtDescripcion());
        }

        btn_ingresar = new JButton("Registrarse");
        btn_ingresar.setBounds(150,210,150,40);
        btn_ingresar.setForeground(Color.WHITE);
        btn_ingresar.setBackground(new Color(0, 100, 60));
        btn_ingresar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                btn_ingresar.setBackground(new Color(25, 25, 112));
            }
            @Override
            public void mouseExited(MouseEvent mev) {
                btn_ingresar.setBackground(new Color(0, 100, 60));
            }
        });
        btn_ingresar.addActionListener(this);

        add(lb_nombre);
        add(txt_nombre);
        add(lb_contrasena);
        add(txt_contrasena);
        add(as_rol_lbl);
        add(asignador_rol);
        add(btn_ingresar);
    }
        public void actionPerformed(ActionEvent e) {
            String nom_obt = txt_nombre.getText();
            String con_obt = txt_contrasena.getPassword().toString();

            int cargo_elegido = asignador_rol.getSelectedIndex();

            Cargo cargo = Cargo.PERSONA;
            if(cargo_elegido == 1)
            {
                cargo = Cargo.EMPRESA;
            } else if ((cargo_elegido == 2) && (this.usuarioServ.obtenerIDUsuario() == 1))
            {
                cargo = Cargo.ADMINISTRADOR;
            }

            try {

                String resultado = this.usuarioServ.registrar_usuario(nom_obt, con_obt, cargo);

                JOptionPane.showMessageDialog(this, resultado);

            } catch(InvalidKeySpecException ikse) {
                JOptionPane.showMessageDialog(this, ikse);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, nfe);
            } catch (NoSuchAlgorithmException nsue) {
                JOptionPane.showMessageDialog(this, nsue);
            } catch (SQLException sqle_r)
            {
                JOptionPane.showMessageDialog(this, sqle_r);
            }
        }
    }