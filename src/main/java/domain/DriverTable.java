package domain;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class DriverTable extends JFrame {
    private Driver driver;
    private JTable tabla;

    public DriverTable(Driver driver) {
        super(driver.getUsername() + "’s rides ");
        this.setBounds(100, 100, 700, 200);
        this.driver = driver;

        DriverAdapter adapt = new DriverAdapter(driver);
        tabla = new JTable(adapt);

        JScrollPane scrollPane = new JScrollPane(tabla);
        tabla.setPreferredScrollableViewportSize(new Dimension(500, 70));

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().add(scrollPane, BorderLayout.CENTER);

        pack();

        // Agrega un WindowListener para cerrar recursos al cerrar la ventana
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                limpiarRecursos();
            }
        });
    }

    // Método para cerrar recursos asociados, como conexiones de base de datos
    private void limpiarRecursos() {
        System.exit(0);
    }
}
