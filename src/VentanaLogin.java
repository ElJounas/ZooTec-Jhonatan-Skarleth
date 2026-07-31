import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * ============================================================
 * VentanaLogin.java — Inicio de sesión (versión 3)
 * ============================================================
 * CAPA: Vista
 *
 * Primera pantalla del sistema. Verifica credenciales y
 * redirige al usuario a la ventana correcta según su rol.
 *
 * Cambio respecto a v2b:
 *   Al cerrar el programa desde esta ventana, se llama a
 *   ConexionBD.cerrar() antes de salir. Esto garantiza que
 *   el archivo zootec.db quede en un estado limpio y no
 *   bloqueado para la próxima ejecución.
 * ============================================================
 */
public class VentanaLogin extends JFrame {

    private JTextField     txtUsuario;
    private JPasswordField txtContrasena;
    private JButton        btnEntrar;

    private GestionUsuarios gestionUsuarios;
    private GestionAnimales gestionAnimales;

    public VentanaLogin() {
        gestionUsuarios = new GestionUsuarios();
        gestionAnimales = new GestionAnimales();

        configurarVentana();
        crearComponentes();
    }

    private void configurarVentana() {
        setTitle("Zootec — Iniciar sesión — CBA Mosquera");
        setSize(360, 230);
        setLocationRelativeTo(null);
        setResizable(false);

        // DO_NOTHING_ON_CLOSE nos permite controlar el cierre nosotros mismos
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Interceptamos el clic en la X para cerrar la BD antes de salir
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // NUEVO: cerramos la conexión a SQLite antes de terminar.
                // Si no hacemos esto, el archivo zootec.db puede quedar
                // bloqueado o con escrituras pendientes sin guardar.
                ConexionBD.cerrar();
                System.exit(0);
            }
        });
    }

    private void crearComponentes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        // ── Encabezado ────────────────────────────────────────
        JLabel titulo = new JLabel("Zootec", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(new Color(0, 100, 0));

        JLabel subtitulo = new JLabel("Acceso seguro", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel encabezado = new JPanel(new BorderLayout(3, 3));
        encabezado.add(titulo,    BorderLayout.NORTH);
        encabezado.add(subtitulo, BorderLayout.SOUTH);
        panel.add(encabezado, BorderLayout.NORTH);

        // ── Formulario ────────────────────────────────────────
        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));

        form.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        form.add(txtUsuario);

        form.add(new JLabel("Contraseña:"));
        txtContrasena = new JPasswordField();
        form.add(txtContrasena);

        btnEntrar = new JButton("Iniciar sesión");
        btnEntrar.setForeground(new Color(0, 140, 0));
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 13));
        form.add(new JLabel());
        form.add(btnEntrar);

        panel.add(form, BorderLayout.CENTER);
        add(panel);

        // ── Eventos ───────────────────────────────────────────
        btnEntrar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { autenticarUsuario(); }
        });
        txtContrasena.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { autenticarUsuario(); }
        });
    }

    /**
     * Verifica credenciales y abre la ventana correspondiente al rol.
     *
     * - ADMIN o VETERINARIO → VentanaFormulario (pueden gestionar animales)
     * - APRENDIZ            → VentanaInventario (solo lectura)
     */
    private void autenticarUsuario() {
        String user = txtUsuario.getText().trim();
        String pass = new String(txtContrasena.getPassword());

        Usuario logueado = gestionUsuarios.autenticar(user, pass);

        if (logueado == null) {
            JOptionPane.showMessageDialog(this,
                "Usuario o contraseña incorrectos.\nVerifique e intente de nuevo.",
                "Error de acceso", JOptionPane.ERROR_MESSAGE);
            // Solo limpiamos la contraseña, no el usuario
            txtContrasena.setText("");
            txtContrasena.requestFocus();
        } else {
            this.setVisible(false);

            if (logueado.getRol() == Rol.ADMIN || logueado.getRol() == Rol.VETERINARIO) {
                new VentanaFormulario(gestionAnimales, this, logueado).setVisible(true);
            } else {
                new VentanaInventario(gestionAnimales, this, logueado).setVisible(true);
            }
        }
    }
}
