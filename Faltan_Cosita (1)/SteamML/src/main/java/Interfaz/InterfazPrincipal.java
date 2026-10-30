package Interfaz;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class InterfazPrincipal extends JFrame {

    protected JMenuBar menuBarComun;
    protected JPopupMenu menuContextual;

    public InterfazPrincipal(String titulo, int ancho, int alto) {
        setTitle(titulo);
        setSize(ancho, alto);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        menuBarComun = new JMenuBar();

        // Ejemplo de un menú desplegable superior
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0)); // Sale de la JVM
        
        menuArchivo.add(itemSalir);
        menuBarComun.add(menuArchivo);

        setJMenuBar(menuBarComun);

        // Configuración del menú contextual
        menuContextual = new JPopupMenu();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                revisarPopup(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                revisarPopup(e);
            }
            private void revisarPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    menuContextual.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
}