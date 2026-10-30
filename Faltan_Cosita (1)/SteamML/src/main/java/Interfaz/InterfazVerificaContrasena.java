package Interfaz;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class InterfazVerificaContrasena extends JFrame implements ActionListener {

    JButton aceptar_btn, cancelar_btn;
    JLabel con_lbl;
    JPasswordField con_txt;

    boolean validador_cierre = true;

    int direccion;

    boolean origen;

    public InterfazVerificaContrasena(int pasador, int ubicacion)
    {
        this.direccion = ubicacion; //Es para definir si se usa para ir a Modificar
                                    //O para ir a eliminar.
                                    //1 = eliminar
                                    //0 = modificar

        if(pasador == 1)
        {
            this.origen = true;
        } else
        {
            this.origen = false;
        }

        setBounds(400,400,370,150);
        setDefaultCloseOperation(InterfazVerificaContrasena.DISPOSE_ON_CLOSE);
        setLayout(null);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent wev)
            {
                if((validador_cierre) && (pasador == 1))
                {
                    SteamML.abrir_InterfazAdmin();
                } else if ((validador_cierre) && (pasador == 0))
                {
                    SteamML.abrir_InterfazCliente();
                }
            }
        });

        con_lbl = new JLabel("Ingrese su contraseña:");
        con_lbl.setBounds(10,20,150,20);
        con_lbl.setForeground(new Color(25, 25, 112));

        con_txt = new JPasswordField("contrasena123");
        con_txt.setForeground(Color.LIGHT_GRAY);
        //con_txt.setEchoChar('°');
        con_txt.setBounds(150,20,200,20);
        con_txt.addFocusListener(new FocusListener() {
            
            @Override
            public void focusGained(FocusEvent fe)
            {
                if(!con_txt.getPassword().toString().equals("contrasena123"))
                {
                    con_txt.setText("");
                    con_txt.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent fev)
            {
                if(con_txt.getPassword().toString().isEmpty())
                {
                    con_txt.setText("contrasena123");
                    con_txt.setForeground(Color.LIGHT_GRAY);
                }
            }
        });

        Color verde_ap = new Color(0, 150, 80, 153); //Verde apagado: Mouse fuera del área del botón
        Color verde_pren = new Color(0, 200, 80, 255); //Verde prendido: Mouse dentro del área del botón

        Color rojo_ap = new Color(180, 40, 40, 153);
        Color rojo_pren = new Color(230, 50, 40, 255);

        aceptar_btn = new JButton("Aceptar");
        aceptar_btn.setBounds(50,60,100,30);
        aceptar_btn.setForeground(Color.WHITE);
        aceptar_btn.setBackground(verde_ap);
        aceptar_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                aceptar_btn.setBackground(verde_pren);
            }
            @Override
            public void mouseExited(MouseEvent mev) {
                aceptar_btn.setBackground(verde_ap);
            }
        });
        aceptar_btn.addActionListener(this);

        cancelar_btn = new JButton("Cancelar");
        cancelar_btn.setBounds(220,60,100,30);
        cancelar_btn.setForeground(Color.WHITE);
        cancelar_btn.setBackground(rojo_ap);
        cancelar_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                cancelar_btn.setBackground(rojo_pren);
            }
            @Override
            public void mouseExited(MouseEvent mev) {
                cancelar_btn.setBackground(rojo_ap);
            }
        });
        cancelar_btn.addActionListener(this);


        add(con_lbl);
        add(con_txt);
        add(aceptar_btn);
        add(cancelar_btn);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == aceptar_btn)
        {
            try
            {
                Usuario valida_us = Usuario.extractor(Usuario.id);
                        
                char[] contra_chars = con_txt.getPassword();

                String contra_ingre = String.valueOf(contra_chars);

                Integer resultado_buscado = Usuario.iniciar_sesion(valida_us.nombre, contra_ingre, valida_us.dni);

                if(!resultado_buscado.equals(-1))
                {
                    if(this.direccion == 1)
                    {
                        SteamML.abrir_EliminarUser(this.origen);
                    } else
                    {
                        SteamML.abrir_InterfazModificarUsuario(this.origen);
                    }
                    validador_cierre = false;

                    this.dispose();
                } else
                {
                    JOptionPane.showMessageDialog(this, "Contraseña ingresada inválida");
                }
            } catch (IOException ioex)
            {
                JOptionPane.showMessageDialog(this,ioex);
            } catch(NoSuchAlgorithmException nsue)
            {
                JOptionPane.showMessageDialog(this, nsue);
            } catch (InvalidKeySpecException ikse)
            {
                JOptionPane.showMessageDialog(this, ikse);
            } catch (NullPointerException npe)
            {
                JOptionPane.showMessageDialog(this, "ERROR: El cuadro de texto de la contraseña está vacío. Por favor, indique su contraseña para continuar.");
            } catch (SQLException sqle) {
                JOptionPane.showMessageDialog(this, sqle);
            } catch (Exception ex)
            {
                JOptionPane.showMessageDialog(this,ex);
            }
        } else
        {
            this.dispose();
        }
    }

}