/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.Interfaz;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;

import javax.swing.text.MaskFormatter;
import javax.swing.JFormattedTextField;
import java.text.ParseException;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import javax.swing.JOptionPane;

import java.awt.Color;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author fran
 */
public class InterfazSolicitudesUsuario extends InterfazPrincipal implements ActionListener {
    
    boolean validador_cierre = true;
    JComboBox<String> cbBuscar, cbEstado;
    JFormattedTextField txtFecha;
    JButton btnFiltrar, btnNoFiltrar;
    DefaultTableModel modelo;
    JTable tabla;

    public InterfazSolicitudesUsuario() {
        
         super("Solicitud", 700, 450);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent wev) {
                if(validador_cierre) {
                    SteamML.abrir_InterfazCliente();
                }
            }
        });

        setTitle("Listado de Usuario");
        setBounds(300, 150, 800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       
        setLayout(new BorderLayout()); 
        
        // Panel superior para los controles de filtrado (idéntico al estilo de logs)
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(null);
        panelSuperior.setPreferredSize(new java.awt.Dimension(800, 80));

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setBounds(20, 25, 50, 25);
        panelSuperior.add(lblBuscar);

        cbBuscar = new JComboBox<>(new String[]{"Antes de:", "Después de:"});
        cbBuscar.setBounds(70, 25, 100, 25);
        panelSuperior.add(cbBuscar);

        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setBounds(185, 25, 45, 25);
        panelSuperior.add(lblFecha);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(355, 25, 50, 25);
        panelSuperior.add(lblEstado);

        cbEstado = new JComboBox<>(new String[]{"Aprobado", "Rechazado", "Pendiente"});
        cbEstado.setBounds(410, 25, 110, 25);
        panelSuperior.add(cbEstado);

        btnNoFiltrar = new JButton("No Filtrar");
        btnNoFiltrar.setForeground(Color.WHITE);
        btnNoFiltrar.setBackground(new Color(0,100,60));
        btnNoFiltrar.setBounds(650, 15, 110, 25);
        btnNoFiltrar.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent moen)
            {
                btnNoFiltrar.setBackground(new Color(25,25,112));
                btnNoFiltrar.setForeground(Color.WHITE.darker());
            }

            @Override
            public void mouseExited(MouseEvent moex)
            {
                btnNoFiltrar.setBackground(new Color(0,100,60));
                btnNoFiltrar.setForeground(Color.WHITE);
            }

        });
        btnNoFiltrar.addActionListener(this);
        panelSuperior.add(btnNoFiltrar);

        btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setForeground(Color.WHITE);
        btnFiltrar.setBackground(new Color(0,100,60));
        btnFiltrar.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseExited(MouseEvent moev)
            {
                btnFiltrar.setBackground(new Color(0,100,60));
                btnFiltrar.setForeground(Color.WHITE);
            }

            @Override
            public void mouseEntered(MouseEvent moen)
            {
                btnFiltrar.setBackground(new Color(25,25,112));
                btnFiltrar.setForeground(Color.WHITE.darker());
            }

        });
        btnFiltrar.setBounds(650, 45, 110, 25);
        btnFiltrar.addActionListener(this);
        panelSuperior.add(btnFiltrar);

        try
        {
            MaskFormatter mascara_fecha = new MaskFormatter("##-##-####");
            mascara_fecha.setPlaceholderCharacter('_');

            txtFecha = new JFormattedTextField(mascara_fecha);
            txtFecha.setColumns(10);
            txtFecha.setBounds(230, 25, 110, 25);
            panelSuperior.add(txtFecha);
        } catch (ParseException pe)
        {
            JOptionPane.showMessageDialog(this,pe.getMessage());
        }

        add(panelSuperior, BorderLayout.NORTH);
        
        // Configuración de la tabla
        String[] columnas = {"Id", "Fecha", "Monto", "Porcentaje de aprobación según la Red Neuronal", "Estado", "ID de responsable"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);

        JScrollPane scrollPane = new JScrollPane(tabla);

        // Carga inicial de datos
        try {
            ArrayList<Solicitud> registro = new ArrayList<>();

            LocalDate dia_actual = LocalDate.now();
            LocalDate dia_sig = dia_actual.plusDays(1);

            registro = Solicitud.mostrar_creditos(null, dia_sig.toString(), Usuario.id, false);

            for(Solicitud fila : registro) {
                modelo.addRow(new Object[] {fila.idSolicitud, fila.fecha, fila.monto, fila.porcentajeAprobacion, fila.estado, fila.idUsuarioResponsable});
            }

        } catch (IllegalArgumentException ilae) {
            JOptionPane.showMessageDialog(this, ilae.getMessage());
        } catch (IOException ioex) {
            JOptionPane.showMessageDialog(this, ioex);
        } catch (SQLException sqle)
        {
            JOptionPane.showMessageDialog(this, sqle);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex);
        }

        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() == btnFiltrar) {
            
            boolean elec_men_mas; //"elec" refiere a electo o elegido 
            int posicion_combo_men_mas = cbBuscar.getSelectedIndex();

            if(posicion_combo_men_mas == 0)
            {
                elec_men_mas = false;
            } else
            {
                elec_men_mas = true;
            }

            String elec_estado = cbEstado.getSelectedItem().toString();

            String fecha_ingresada = txtFecha.getText();
            if(fecha_ingresada.length() < 10)
            {
                fecha_ingresada = LocalDate.now().plusDays(1).toString();
            }

            ArrayList<Solicitud> registro_soli = new ArrayList<>();

            try
            {
                registro_soli = Solicitud.mostrar_creditos(elec_estado,fecha_ingresada,Usuario.id,elec_men_mas);

                for(Solicitud fila : registro_soli)
                {
                    modelo.addRow(new Object[] {fila.idSolicitud, fila.fecha, fila.monto, fila.porcentajeAprobacion, fila.estado, fila.idUsuarioResponsable});
                }
            } catch (SQLException sqle)
            {
                JOptionPane.showMessageDialog(this, sqle);
            } catch (Exception ex)
            {
                JOptionPane.showMessageDialog(this, ex);
            }
            
        } else if (e.getSource() == btnNoFiltrar)
        {
            ArrayList<Solicitud> registro_soli = new ArrayList<>();

            LocalDate dia_decidido = LocalDate.now().plusDays(1);

            try
            {
                registro_soli = Solicitud.mostrar_creditos(null, dia_decidido.toString(),Usuario.id,false);

                for(Solicitud fila : registro_soli)
                {
                    modelo.addRow(new Object[] {fila.idSolicitud, fila.fecha, fila.monto, fila.porcentajeAprobacion, fila.estado, fila.idUsuarioResponsable});
                }
            } catch (IllegalArgumentException ilae)
            {
                JOptionPane.showMessageDialog(this, ilae);
            } catch (SQLException sqle)
            {
                JOptionPane.showMessageDialog(this, sqle);
            } catch (Exception ex)
            {
                JOptionPane.showMessageDialog(this,ex.getMessage());
            }
        }
    }
}