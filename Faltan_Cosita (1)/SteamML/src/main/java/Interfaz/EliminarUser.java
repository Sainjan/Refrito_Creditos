/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaz;

import java.io.IOException;

import java.awt.Color;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import Servicio.UsuarioServicio;
import Principal.SteamML;

import java.sql.SQLException;

public class EliminarUser extends InterfazPrincipal implements ActionListener {
    JButton btn_si, btn_no;
    JLabel lbl_mensaje;
    UsuarioServicio usSer;

    boolean validador_pasaje = true;

    public EliminarUser(boolean valor, UsuarioServicio us) 
    {
        super("Confirmar acción", 700, 450);

        this.usSer = us;
        setResizable(false); // Evita que redimensionen la ventana

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent wev)
            {
                if(valor && validador_pasaje)
                {
                    SteamML.abrir_InterfazAdmin();
                } else if (validador_pasaje && !valor)
                {
                    SteamML.abrir_InterfazCliente();
                } else
                {
                    SteamML.abrir_InterfazInicio();
                }
            }
        });

        // Mensaje de confirmación
        lbl_mensaje = new JLabel("¿Estás seguro de que deseas eliminar este usuario?", SwingConstants.CENTER);
        lbl_mensaje.setBounds(0, 50, 500, 30);
        add(lbl_mensaje);

        // Configuración botón SI
        btn_si = new JButton("Si");
        btn_si.setBounds(140, 150, 100, 25);
        btn_si.setForeground(Color.WHITE);
        btn_si.setBackground(new Color(0, 100, 60));
        btn_si.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent me)
                {
                    btn_si.setBackground(new Color(25, 25, 112));
                    btn_si.setForeground(Color.WHITE);
                }

                @Override
                public void mouseExited(MouseEvent mev)
                {
                    btn_si.setBackground(new Color(0, 100, 60));
                    btn_si.setForeground(Color.WHITE);}
            });
          btn_si.addActionListener(this);
        add(btn_si);

        // Configuración botón NO
        btn_no = new JButton("No");
        btn_no.setBounds(260, 150, 100, 25);
        btn_no.setForeground(Color.WHITE);
        btn_no.setBackground(new Color(0, 100, 60));
        btn_no.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent me)
                {
                    btn_no.setBackground(new Color(25, 25, 112));
                    btn_no.setForeground(Color.WHITE);
                }

                @Override
                public void mouseExited(MouseEvent mev)
                {
                    btn_no.setBackground(new Color(0, 100, 60));
                    btn_no.setForeground(Color.WHITE);}
            });
          btn_no.addActionListener(this);
        add(btn_no);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_si) {
           
            try
            {
                String res_elim = this.usSer.eliminarUsuario();

                JOptionPane.showMessageDialog(this, res_elim);

                validador_pasaje = false;

                this.dispose();
            } catch (SQLException sqle)
            {
                JOptionPane.showMessageDialog(this, sqle);
            } catch (Exception ex)
            {
                JOptionPane.showMessageDialog(this, ex);
            }
        } else if (e.getSource() == btn_no) {
            this.dispose(); // Cierra la ventana sin hacer nada
        }
    }
}