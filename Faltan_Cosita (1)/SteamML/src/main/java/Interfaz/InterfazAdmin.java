package Interfaz;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import Principal.SteamML;
import Servicio.UsuarioServicio;

public class InterfazAdmin extends InterfazPrincipal implements ActionListener {
    
    JButton btnNeuronal, btnCrearUser, btn_elimina, btnListar, btn_modifica;
    boolean validador_cierre = true;

    UsuarioServicio us;

    public InterfazAdmin(UsuarioServicio usServicio) 
    {
        super("Interfaz administrador", 700, 450);
        
        this.us = usServicio;

        addWindowListener(new WindowAdapter() 
        {

            @Override
            public void windowClosed(WindowEvent wev)
            {
                if(validador_cierre)
                {
                    SteamML.abrir_InterfazInicio();
                }
            }
        });
        // --- Botones Aprobar/Rechazar ---
        btnNeuronal = new JButton("Abrir solicitudes");
        btnNeuronal.setBounds(225, 110, 250, 40);
        btnNeuronal.setForeground(Color.WHITE);
        btnNeuronal.setBackground(new Color(0, 100, 60));
    
        btnNeuronal.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me)
            {
                btnNeuronal.setBackground(new Color(25, 25, 112));
                btnNeuronal.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent mev)
            {
                btnNeuronal.setBackground(new Color(0, 100, 60));
                btnNeuronal.setForeground(Color.WHITE);
            }
        });
        btnNeuronal.addActionListener(this);
        add(btnNeuronal);
        
        btnCrearUser = new JButton("Crear nuevo usuario");
        btnCrearUser.setBounds(225, 230, 250, 40);
        btnCrearUser.setForeground(Color.WHITE);
        btnCrearUser.setBackground(new Color(0, 100, 60));
    
        btnCrearUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me)
            {
                btnCrearUser.setBackground(new Color(25, 25, 112));
                btnCrearUser.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent mev)
            {
                btnCrearUser.setBackground(new Color(0, 100, 60));
                btnCrearUser.setForeground(Color.WHITE);
            }
        });
        btnCrearUser.addActionListener(this);
        if(this.us.obtenerIDUsuario() == 1)
        {
            add(btnCrearUser);
        }

        btn_elimina = new JButton("Eliminar Usuario");
        btn_elimina.setBounds(225,290,250,40);
        btn_elimina.setBackground(new Color(0,100,60));
        btn_elimina.setForeground(Color.WHITE);

        btn_elimina.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent moen)
            {
                btn_elimina.setBackground(new Color(25,25,112));
            }

            @Override
            public void mouseExited(MouseEvent moex)
            {
                btn_elimina.setBackground(new Color(0,100,60));
            }

        });
        btn_elimina.addActionListener(this);
        //El botón de "eliminar" se agrega más abajo para no hacer dos operadores de control

        btn_modifica = new JButton("Modificar cuenta");
        btn_modifica.setBounds(225,350,250,40);
        btn_modifica.setBackground(new Color(0,100,60));
        btn_modifica.setForeground(Color.WHITE);
        btn_modifica.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent moen)
            {
                btn_modifica.setBackground(new Color(25,25,112));
            }

            @Override
            public void mouseExited(MouseEvent moex)
            {
                btn_modifica.setBackground(new Color(0,100,60));
            }

        });
        btn_modifica.addActionListener(this);
        if(this.us.obtenerIDUsuario() == 1)
        {
            add(btn_modifica);
            add(btn_elimina);
        }
            
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnNeuronal)
        {
            SteamML.abrir_InterfazRedNeuronal();
            validador_cierre = false;
            this.dispose();

        } else if (e.getSource() == btnCrearUser)
        {
            SteamML.abrir_InterfazRegistro(2);
            validador_cierre = false;
            this.dispose();

        } else if (e.getSource() == btn_elimina)
        {
            SteamML.abrir_InterfazVerificaContrasena(1, 1);
            validador_cierre = false;

            this.dispose();

        } else if (e.getSource() == btn_modifica)
        {
            SteamML.abrir_InterfazVerificaContrasena(1, 0);
            validador_cierre = false;

            this.dispose();
        }
    }
}