package Interfaz;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import java.awt.Color;

public class InterfazCarga extends JDialog {

    private JProgressBar barraProgreso;

    public InterfazCarga() {
        setBounds(400,300,400, 150);
        setModal(true); // Bloquea la ventana 
        setUndecorated(true); // Le quita los bordes de la ventana para que se vea más moderno
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel lblMensaje = new JLabel("Cargando solicitudes...");
        lblMensaje.setBounds(50, 25, 300, 25);
        lblMensaje.setForeground(new Color(25, 25, 112));
        add(lblMensaje);

        barraProgreso = new JProgressBar();
        barraProgreso.setBounds(50, 70, 300, 25);
        barraProgreso.setIndeterminate(true); 
        add(barraProgreso);
    }
}
