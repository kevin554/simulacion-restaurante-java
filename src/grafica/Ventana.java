package grafica;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class Ventana extends JFrame {

    private Dibujo elPanel;

    public Ventana() {
        init();
    }
    
    private void init() {
        elPanel = new Dibujo();
        setTitle("Graphics");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(elPanel, BorderLayout.CENTER);
        pack();
        setVisible(true);
        Thread hilo = new Thread(elPanel);
        hilo.start();
    }
    
}