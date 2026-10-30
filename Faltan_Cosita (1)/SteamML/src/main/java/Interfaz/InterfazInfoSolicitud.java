package Interfaz;

import java.io.IOException;

import javax.swing.JFrame;
import java.awt.event.*;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.sql.SQLException;

import Servicio.UsuarioServicio;
import Servicio.SolicitudServicio;
import Modelo.Solicitud;

public class InterfazInfoSolicitud extends InterfazPrincipal implements ActionListener {

    Integer id;

    UsuarioServicio us;

    JLabel nom, porc_rnet, id_orden,//Describen al usuario y su solicitud
            //Nombre, porcentaje RedNeuronal.nnet, ID de orden

            gen_lbl, auto_lbl, prop_lbl, tel_trab_lbl, tel_lbl, eml_lbl, desemp_lbl, hij_lbl, fam_lbl, larg_lbl, 
            ing_lbl, edad_lbl, emple_lbl, ing_or_lbl, edu_lbl, est_civ_lbl, est_casa_lbl, ocup_lbl, monto_lbl,
            nom_lbl,porc_rnet_lbl,id_orden_lbl,

            //genero, auto, propiedad, telefono_trabajo, telefono, email, desempleo, hijos, familia, cuenta, ingresos, edad, empleo, origen_ingresos, educación, estado_civil, estado_casa, ocupación, monto

            gen_txt, auto_txt, prop_txt, tel_trab_txt, tel_txt, eml_txt, desemp_txt, hij_txt, fam_txt, larg_txt, ing_txt, edad_txt, emple_txt, ing_or_txt, edu_txt, est_civ_txt, est_casa_txt, ocup_txt, monto_txt;

            //FILA 0: Datos de solicitante
            //FILA 1: Títulos de campos
            //FILA 2: Valor de campos

    JButton aprobar, rechazar;

    String gen, au, prop, tel_t, tel, eml, desempleado;

    public InterfazInfoSolicitud(Integer id_solicitud, UsuarioServicio usIn) throws SQLException
    {
        super("Info solicitud", 700, 450);
        //LÓGICA BACKEND---------------------------------------------------------------------------

        this.id = id_solicitud;

        this.us = usIn;

        Solicitud info_sol = SolicitudServicio.expande_solicitud(id_solicitud.toString());
        
        if(info_sol.genero.equals("1")) 
        {
            this.gen = "Hombre";
        } else
        {
            this.gen = "Mujer";
        }
        if(info_sol.auto.equals("1"))
        {
            this.au = "Si";
        } else
        {
            this.au = "No";
        }

        if(info_sol.propiedad.equals("1"))
        {
            this.prop = "Si";
        } else
        {
            this.prop = "No";
        }

        if(info_sol.telefonoTrabajo.equals("1"))
        {
            this.tel_t = "Si";
        } else
        {
            this.tel_t = "No";
        }

        if(info_sol.telefono.equals("1"))
        {
            this.tel = "Si";
        } else
        {
            this.tel = "No";
        }

        if(info_sol.email.equals("1"))
        {
            this.eml = "Si";
        } else
        {
            this.eml = "No";
        }

        if(info_sol.desempleo.equals("1"))
        {
            this.desempleado = "Si";
        } else
        {
            this.desempleado = "No";
        }

        //FIN DE LÓGICA BACKEND--------------------------------------------------------------------

        //FRONTEND---------------------------------------------------------------------------------

        setBounds(400, 500, 600, 600);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(Color.LIGHT_GRAY);

        setDefaultCloseOperation(InterfazInfoSolicitud.DISPOSE_ON_CLOSE);

        Color verde_ap = new Color(0, 150, 80, 153); //Verde apagado: Mouse fuera del área del botón
        Color verde_pren = new Color(0, 200, 80, 255); //Verde prendido: Mouse dentro del área del botón

        Color rojo_ap = new Color(180, 40, 40, 153);
        Color rojo_pren = new Color(230, 50, 40, 255);

        Color azul_palabra = new Color(20,130,240);

        nom_lbl = new JLabel("Nombre: ");
        nom_lbl.setBounds(20,70,80,20);
        nom_lbl.setForeground(Color.BLACK);

        nom = new JLabel(info_us.nombre);
        nom.setBounds(110,70,200,20);
        nom.setForeground(azul_palabra);

        porc_rnet_lbl = new JLabel("PROBABILIDAD DE APROBACIÓN: ");
        porc_rnet_lbl.setBounds(300,20,220,20);
        porc_rnet_lbl.setForeground(Color.BLACK);

        porc_rnet = new JLabel(info_sol.porcentajeAprobacion.toString());
        porc_rnet.setBounds(540,20,50,20);
        porc_rnet.setForeground(azul_palabra);

        id_orden_lbl = new JLabel("ID de Orden:");
        id_orden_lbl.setBounds(20,20,100,20);
        id_orden_lbl.setForeground(Color.BLACK);

        id_orden = new JLabel(info_sol.idSolicitud.toString());
        id_orden.setBounds(120, 20, 80, 20);
        id_orden.setForeground(Color.BLACK);

        aprobar = new JButton("APROBAR");
        aprobar.setBounds(100, 540, 100, 20);
        aprobar.setBackground(verde_ap);
        aprobar.setForeground(Color.WHITE.darker());
        aprobar.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent meen)
            {
                aprobar.setBackground(verde_pren);
                aprobar.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent meex)
            {
                aprobar.setBackground(verde_ap);
                aprobar.setForeground(Color.WHITE.darker());
            }

        });
        aprobar.addActionListener(this);

        rechazar = new JButton("RECHAZAR");
        rechazar.setBounds(400, 540, 100, 20);
        rechazar.setBackground(rojo_ap);
        rechazar.setForeground(Color.WHITE.darker());
        rechazar.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent meen)
            {
                rechazar.setBackground(rojo_pren);
                rechazar.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent meex)
            {
                rechazar.setBackground(rojo_ap);
                rechazar.setForeground(Color.WHITE.darker());
            }

        });
        rechazar.addActionListener(this);

        gen_lbl = new JLabel("Género: ");
        gen_lbl.setBounds(20,100,50,20);
        gen_lbl.setForeground(Color.BLACK);

        gen_txt = new JLabel(this.gen);
        gen_txt.setBounds(80,100,30,20);
        gen_txt.setForeground(Color.BLACK);

        auto_lbl = new JLabel("Auto: ");
        auto_lbl.setBounds(20, 140, 50,20);
        auto_lbl.setForeground(Color.BLACK);

        auto_txt = new JLabel(this.au);
        auto_txt.setBounds(80, 140, 30, 20);
        auto_txt.setForeground(Color.BLACK);

        prop_lbl = new JLabel("Propiedad: ");
        prop_lbl.setBounds(20, 180, 70, 20);
        prop_lbl.setForeground(Color.BLACK);

        prop_txt = new JLabel(this.prop);
        prop_txt.setBounds(100, 180, 30, 20);
        prop_txt.setForeground(Color.BLACK);

        tel_trab_lbl = new JLabel("Teléfono laboral: ");
        tel_trab_lbl.setBounds(20, 220,70,20);
        tel_trab_lbl.setForeground(Color.BLACK);

        tel_trab_txt = new JLabel(this.tel_t);
        tel_trab_txt.setBounds(100,220,30,20);
        tel_trab_txt.setForeground(Color.BLACK);

        tel_lbl = new JLabel("Teléfono: ");
        tel_lbl.setBounds(20, 260, 50,20);
        tel_lbl.setForeground(Color.BLACK);

        tel_txt = new JLabel(this.tel);
        tel_txt.setBounds(80,260,30,20);
        tel_txt.setForeground(Color.BLACK);

        eml_lbl = new JLabel("Email: ");
        eml_lbl.setBounds(20,300,50,20);
        eml_lbl.setForeground(Color.BLACK);

        eml_txt = new JLabel(this.eml);
        eml_txt.setBounds(80,300,30,20);
        eml_txt.setForeground(Color.BLACK);

        desemp_lbl = new JLabel("Desempleado: ");
        desemp_lbl.setBounds(20,340,50,20);
        desemp_lbl.setForeground(Color.BLACK);

        desemp_txt = new JLabel(this.desempleado);
        desemp_txt.setBounds(80,340,30,20);
        desemp_txt.setForeground(Color.BLACK);

        hij_lbl = new JLabel("N° de Hijos: ");
        hij_lbl.setBounds(20,380,60,20);
        hij_lbl.setForeground(Color.BLACK);

        hij_txt = new JLabel(info_sol.hijos.toString());
        hij_txt.setBounds(90, 380, 40,20);
        hij_txt.setForeground(Color.BLACK);
        
        fam_lbl = new JLabel("Fam. a cargo: ");
        fam_lbl.setBounds(20, 420, 60,20);
        fam_lbl.setForeground(Color.BLACK);

        fam_txt = new JLabel(info_sol.familia.toString());
        fam_txt.setBounds(90, 420, 40,20);
        fam_txt.setForeground(Color.BLACK);
        
        larg_lbl = new JLabel("Tiempo de Cuenta (Años): ");
        larg_lbl.setBounds(20, 460, 140, 20);
        larg_lbl.setForeground(Color.BLACK);

        larg_txt = new JLabel(info_sol.longitudCuenta.toString());
        larg_txt.setBounds(170, 460, 30, 20);
        larg_txt.setForeground(Color.BLACK);
        
        ing_lbl = new JLabel("Ingresos anuales: ");
        ing_lbl.setBounds(350,100, 100, 20);
        ing_lbl.setForeground(Color.BLACK);

        ing_txt = new JLabel(info_sol.ingresosAnuales.toString());
        ing_txt.setBounds(460,100,70,20);
        ing_txt.setForeground(Color.BLACK);
        
        edad_lbl = new JLabel("Edad: ");
        edad_lbl.setBounds(350, 140, 50, 20);
        edad_lbl.setForeground(Color.BLACK);

        edad_txt = new JLabel(info_sol.edad.toString());
        edad_txt.setBounds(410, 140, 30, 20);
        edad_txt.setForeground(Color.BLACK);
        
        emple_lbl = new JLabel("Años empleado: ");
        emple_lbl.setBounds(350, 180, 100, 20);
        emple_lbl.setForeground(Color.BLACK);

        emple_txt = new JLabel(info_sol.empleo);
        emple_txt.setBounds(460, 180, 40, 20);
        emple_txt.setForeground(Color.BLACK);
        
        ing_or_lbl = new JLabel("Proveedor de ingresos: ");
        ing_or_lbl.setBounds(350, 220, 120, 20);
        ing_or_lbl.setForeground(Color.BLACK);

        ing_or_txt = new JLabel(info_sol.origenIngresos);
        ing_or_txt.setBounds(480, 220, 100, 20);
        ing_or_txt.setForeground(Color.BLACK);
        
        edu_lbl = new JLabel("Nivel Educativo: ");
        edu_lbl.setBounds(350, 260, 100, 20);
        edu_lbl.setForeground(Color.BLACK);

        edu_txt = new JLabel(info_sol.educacion);
        edu_txt.setBounds(460, 260, 100, 20);
        edu_txt.setForeground(Color.BLACK);
        
        est_civ_lbl = new JLabel("Estado Civil: ");
        est_civ_lbl.setBounds(350, 300, 100, 20);
        est_civ_lbl.setForeground(Color.BLACK);

        est_civ_txt = new JLabel(info_sol.estadoCivil);
        est_civ_txt.setBounds(460, 300, 100, 20);
        est_civ_txt.setForeground(Color.BLACK);
        
        est_casa_lbl = new JLabel("Estado de casa: ");
        est_casa_lbl.setBounds(350, 340, 100, 20);
        est_casa_lbl.setForeground(Color.BLACK);

        est_casa_txt = new JLabel(info_sol.estadoCasa);
        est_casa_txt.setBounds(460, 340, 100, 20);
        est_casa_txt.setForeground(Color.BLACK);
        
        ocup_lbl = new JLabel("Trabajo: ");
        ocup_lbl.setBounds(350, 380, 50, 20);
        ocup_lbl.setForeground(Color.BLACK);

        ocup_txt = new JLabel(info_sol.ocupacion);
        ocup_txt.setBounds(410, 380, 100, 20);
        ocup_txt.setForeground(Color.BLACK);
        
        monto_lbl = new JLabel("Monto solicitado: ");
        monto_lbl.setBounds(200, 500, 120, 20);
        monto_lbl.setForeground(Color.BLACK);
        
        monto_txt = new JLabel(info_sol.monto.toString());
        monto_txt.setBounds(330, 500, 150, 20);
        monto_txt.setForeground(new Color(0,200,0));

        add(gen_lbl);
        add(gen_txt);
        add(auto_lbl);
        add(auto_txt);
        add(prop_lbl);
        add(prop_txt);
        add(tel_trab_lbl);
        add(tel_trab_txt);
        add(tel_lbl);
        add(tel_txt);
        add(eml_lbl);
        add(eml_txt);
        add(desemp_lbl);
        add(desemp_txt);
        add(hij_lbl);
        add(hij_txt);
        add(fam_lbl);
        add(fam_txt);
        add(larg_lbl);
        add(larg_txt);
        add(ing_lbl);
        add(ing_txt);
        add(edad_lbl);
        add(edad_txt);
        add(emple_lbl);
        add(emple_txt);
        add(ing_or_lbl);
        add(ing_or_txt);
        add(edu_lbl);
        add(edu_txt);
        add(est_civ_lbl);
        add(est_civ_txt);
        add(est_casa_lbl);
        add(est_casa_txt);
        add(ocup_lbl);
        add(ocup_txt);
        add(monto_lbl);
        add(monto_txt);
        add(nom_lbl);
        add(nom);
        add(porc_rnet_lbl);
        add(porc_rnet);
        add(id_orden_lbl);
        add(id_orden);
        add(aprobar);
        add(rechazar);

    }

    public void actionPerformed(ActionEvent e)
    {

        if(e.getSource() == aprobar)
        {
            try
            {
                if(Solicitud.segregador(this.id, true)) //Se hace con una función de mismo nombre porque así está en las Vistas de Secuencia
                {
                    JOptionPane.showMessageDialog(this, "¡Estado de solicitud modificado con éxito!");
                    this.dispose();
                } else
                {
                    JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado al intentar modificar el estado de la solicitud");
                }
            } catch (IOException ioex)
            {
                JOptionPane.showMessageDialog(this, ioex);
            } catch (SQLException sqlex)
            {
                JOptionPane.showMessageDialog(this, sqlex);
            }
        } else
        {
            try
            {
                if(Solicitud.segregador(this.id, false))
                {
                    JOptionPane.showMessageDialog(this, "¡Estado de solicitud modificado con éxito!");
                    this.dispose();
                } else
                {
                    JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado al intentar modificar el estado de la solicitud");
                }
            } catch (IOException ioex)
            {
                JOptionPane.showMessageDialog(this, ioex);
            } catch (SQLException sqle_u)
            {
                JOptionPane.showMessageDialog(this,sqle_u);
            }
        }

    }
    
}
