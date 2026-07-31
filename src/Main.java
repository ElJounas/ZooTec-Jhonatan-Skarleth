import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * ============================================================
 * Main.java — 
 * ============================================================
 * Esta clase solo tiene un propósito: arrancar el programa.
 * Crea la ventana de login y la hace visible.
 *
 * SwingUtilities.invokeLater() garantiza que toda la interfaz
 * gráfica se construya en el hilo correcto de Java (el Event
 * Dispatch Thread). Es una buena práctica obligatoria en Swing.
 * ============================================================
 */
public class Main {

    public static void main(String[] args) {

        // Intentamos usar el estilo visual del sistema operativo.
        // En Windows se ve más "nativo" que el estilo por defecto de Java.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, Java usa su propio estilo. No es un error crítico.
            System.out.println("Usando el Look & Feel por defecto de Java.");
        }

        // Lanzamos la interfaz gráfica en el hilo correcto
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VentanaLogin login = new VentanaLogin();
                login.setVisible(true);
            }
        });
    }
}
