/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaz;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import java.sql.SQLException;

import Principal.SteamML;
import Servicio.UsuarioServicio;

public class InterfazModificarUsuario extends InterfazPrincipal implements ActionListener {
    JLabel nombre_user, contrasena_user;
    JTextField text_user;
    JPasswordField con_user; 
    JButton btn_Guardar;
    boolean validador_cierre = true;

    UsuarioServicio us;

    public InterfazModificarUsuario(boolean origen, UsuarioServicio serv) 
    {
        super("Modificar usuario", 700, 450);

        this.validador_cierre = origen;

        setBounds(400, 200, 500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null); 
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent wev)
            {
                if(validador_cierre)
                {
                    SteamML.abrir_InterfazAdmin();
                } else
                {
                    SteamML.abrir_InterfazCliente();
                }
            }
        });
        nombre_user = new JLabel("Ingrese el nuevo nombre que desea: ");
        nombre_user.setBounds(50,40,400,25);
        nombre_user.setForeground(new Color(25, 25, 112)); 
        add(nombre_user);
       
       text_user = new JTextField("Pepe");
       text_user.setForeground(Color.LIGHT_GRAY);
       text_user.setBounds(50,70,400,30);
       text_user.addFocusListener(new FocusListener() {
            
            @Override
            public void focusGained(FocusEvent fe)
            {
                if(text_user.getText() != "Pepe")
                {
                    text_user.setText("");
                    text_user.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent fev)
            {
                if(text_user.getText().isEmpty())
                {
                    text_user.setText("Pepe");
                    text_user.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
       add(text_user);
       
        contrasena_user = new JLabel("Ingrese la nueva contraseña que desea");
        contrasena_user.setBounds(50,120,400,25);
        contrasena_user.setForeground(new Color(25, 25, 112)); 
        add(contrasena_user);
       
        con_user = new JPasswordField("contrasena123");
        con_user.setForeground(Color.LIGHT_GRAY);
        //con_user.setEchoChar('º');
        con_user.setBounds(50,150,400,30);
        con_user.addFocusListener(new FocusListener() {
            
            @Override
            public void focusGained(FocusEvent fe)
            {
                if(!con_user.getPassword().toString().equals("contrasena123"))
                {
                    con_user.setText("");
                    con_user.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent fev)
            {
                if(con_user.getPassword().toString().isEmpty())
                {
                    con_user.setText("contrasena123");
                    con_user.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
        add(con_user);
        
        btn_Guardar = new JButton("Guardar");
        btn_Guardar.setBounds(200,220,100,30);
        btn_Guardar.setForeground(Color.WHITE);
        btn_Guardar.setBackground(new Color(0, 100, 60));
        
        btn_Guardar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me)
            {
                btn_Guardar.setBackground(new Color(25, 25, 112));
                btn_Guardar.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent mev)
            {
                btn_Guardar.setBackground(new Color(0, 100, 60));
                btn_Guardar.setForeground(Color.WHITE);
            }
        });
        btn_Guardar.addActionListener(this);
        add(btn_Guardar);
       
    }
  
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btn_Guardar) {
                try {
                
                    String nombre_nuevo = text_user.getText();
                    String contra_nueva = con_user.getPassword().toString();

                    this.us.modificar_usr(nombre_nuevo, contra_nueva);

                    String msg = "Se actualizaron los datos correctamente";
                    JOptionPane.showMessageDialog(this, msg, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Lógica de navegación
                    this.dispose();
                    
                } catch (InvalidKeySpecException ikse)
                {
                    JOptionPane.showMessageDialog(this, ikse);
                } catch (NoSuchAlgorithmException nsue)
                {
                    JOptionPane.showMessageDialog(this, nsue);
                } catch (SQLException sqlex)
                {
                    JOptionPane.showMessageDialog(this, sqlex);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Ocurrió un error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
