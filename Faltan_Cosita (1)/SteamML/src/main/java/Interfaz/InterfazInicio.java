package Interfaz;

//IMPORTS DE INTERFAZ
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.event.*;

import Principal.SteamML;

public class InterfazInicio extends InterfazPrincipal implements ActionListener {
    JLabel lb_bienvenida, lb_eleccion;
    JButton bt_Registrar, bt_user, bt_salir;

    public InterfazInicio(){

        super("Menú principal", 700, 450);

        setDefaultCloseOperation(InterfazInicio.EXIT_ON_CLOSE);
        
        getContentPane().setBackground(Color.WHITE);
            
        lb_bienvenida = new JLabel("¡HOLA! ¡BIENVENIDO AL REFRITO DE CRÉDITOS!");
        lb_bienvenida.setBounds(150,50,300,40);
        lb_bienvenida.setForeground(new Color(25, 25, 112));

        bt_salir = new JButton("Salir");
        bt_salir.setBounds(400, 230, 80, 30);
        bt_salir.setBackground(new Color(0, 100, 60));
        bt_salir.setForeground(Color.WHITE);
        bt_salir.addActionListener(this);
        
        bt_user = new JButton("Inicio sesion");
        bt_user.setBounds(175,100,150,50);
        bt_user.setBackground(new Color(0, 100, 60));
        bt_user.setForeground(Color.WHITE);
        
        bt_user.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me)
            {
                bt_user.setBackground(new Color(25, 25, 112));
                bt_user.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent mev)
            {
                bt_user.setBackground(new Color(0, 100, 60));
                bt_user.setForeground(Color.WHITE);}
        });
        bt_user.addActionListener(this);
        
        bt_Registrar = new JButton ("Registrarse");
        bt_Registrar.setBounds(175,165,150,50);
        bt_Registrar.setBorder(BorderFactory.createRaisedBevelBorder());
        bt_Registrar.setBackground(new Color(0, 100, 60));
        bt_Registrar.setForeground(Color.WHITE);
        
        // Evento del ratón limpio (sin la imagen adentro)
        bt_Registrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me)
            {
                bt_Registrar.setBackground(new Color(25, 25, 112));
                bt_Registrar.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent mev)
            {
                bt_Registrar.setBackground(new Color(0, 100, 60));
                bt_Registrar.setForeground(Color.WHITE);}
        });
        bt_Registrar.addActionListener(this);

        // Agregar los elementos a la ventana
        add(lb_bienvenida);
        // add(lb_eleccion); // La comento porque aún no le has hecho "new JLabel()"
        add(bt_user);
        add(bt_Registrar);
        add(miLabel); // Se agrega la imagen
        add(bt_salir);

        javax.swing.SwingUtilities.invokeLater(() -> {
            InterfazCarga ventanaCarga = new InterfazCarga();
            ventanaCarga.setVisible(true); // Esto pausará la ejecución aquí y mostrará el cartel flotando encima
        });

    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == bt_Registrar){
            SteamML.abrir_InterfazRegistro(1);
            this.dispose();
        } else if (e.getSource() == bt_user){
            SteamML.abrir_InterfazInicioSesion();
            this.dispose();
        }else if (e.getSource() == bt_salir){
            System.exit(0);
        }
    }
}