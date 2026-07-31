import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;

/**
 * ============================================================
 * VentanaFormulario.java — Gestión de ejemplares
 * ============================================================
 * CAPA: Vista
 *
 * Ventana para Administradores y Veterinarios.
 * Permite registrar, editar y (Admin) eliminar animales.
 *
 * Novedades respecto a v2:
 *   - Al seleccionar un animal para editar, se muestra su
 *     fecha de registro en una etiqueta de solo lectura.
 *     (La fecha no se puede cambiar: es un dato histórico.)
 *   - Nuevo botón "Ver estadísticas" que abre VentanaEstadisticas.
 *     La vista solo crea el objeto GestionEstadisticas y abre
 *     la ventana: no calcula nada ella misma. (Separación de capas.)
 * ============================================================
 */
public class VentanaFormulario extends JFrame {

    // ── Campos del formulario ─────────────────────────────────
    private JTextField txtNombre;
    private JTextField txtEspecie;
    private JTextField txtRaza;
    private JTextField txtPeso;
    private JTextField txtEdad;

    // Etiquetas de solo lectura que aparecen al editar
    private JLabel lblIdEdicion;
    private JLabel lblFechaEdicion; // NUEVO: muestra la fecha de registro

    // ── Tabla ─────────────────────────────────────────────────
    private JTable              tablaAnimales;
    private ModeloTablaAnimales modelo;
    private JLabel              lblTotal;

    // ── Botones ───────────────────────────────────────────────
    private JButton btnGuardar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnCancelar;
    private JButton btnEstadisticas; // NUEVO
    private JButton btnVerInventario;
    private JButton btnCerrarSesion;

    // ── Referencias ───────────────────────────────────────────
    private GestionAnimales logica;
    private JFrame          ventanaLogin;
    private Usuario         usuarioActivo;
    private int             idEnEdicion = -1; // -1 = no hay edición en curso

    // ── Constructor ───────────────────────────────────────────
    public VentanaFormulario(GestionAnimales logica,
                             JFrame ventanaLogin,
                             Usuario usuarioActivo) {
        this.logica        = logica;
        this.ventanaLogin  = ventanaLogin;
        this.usuarioActivo = usuarioActivo;

        configurarVentana();
        crearComponentes();
        configurarBotonesPorRol();
        actualizarTabla();
    }

    private void configurarVentana() {
        String rolTexto = (usuarioActivo.getRol() == Rol.ADMIN)
                          ? "Administrador" : "Veterinario";
        setTitle("Zootec — Gestión de ejemplares — "
                 + rolTexto + ": " + usuarioActivo.getNombreUsuario());
        setSize(760, 610);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { confirmarCierreSesion(); }
        });
    }

    private void crearComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        // ── Encabezado ────────────────────────────────────────
        JLabel titulo = new JLabel("Zootec", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(new Color(0, 100, 0));
        JLabel subtitulo = new JLabel(
            "Formulario de registro (Veterinarios / Ayudantes)",
            SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 11));
        JPanel encabezado = new JPanel(new BorderLayout(2, 2));
        encabezado.add(titulo,    BorderLayout.NORTH);
        encabezado.add(subtitulo, BorderLayout.SOUTH);
        panelPrincipal.add(encabezado, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(8, 8));

        // ── Sección de datos ──────────────────────────────────
        JPanel seccionDatos = new JPanel(new BorderLayout(5, 5));
        seccionDatos.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Datos del ejemplar"),
            BorderFactory.createEmptyBorder(5, 8, 8, 8)));

        JPanel camposPanel = new JPanel(new GridLayout(2, 6, 8, 6));

        // Fila 1: etiquetas
        camposPanel.add(new JLabel("Nombre:"));
        camposPanel.add(new JLabel("Especie:"));
        camposPanel.add(new JLabel("Raza:"));
        camposPanel.add(new JLabel("Peso (kg):"));
        camposPanel.add(new JLabel("Edad (años):"));
        camposPanel.add(new JLabel("Editando ID:"));

        // Fila 2: campos de texto + panel de info al editar
        txtNombre  = new JTextField(); camposPanel.add(txtNombre);
        txtEspecie = new JTextField(); camposPanel.add(txtEspecie);
        txtRaza    = new JTextField(); camposPanel.add(txtRaza);
        txtPeso    = new JTextField(); camposPanel.add(txtPeso);
        txtEdad    = new JTextField(); camposPanel.add(txtEdad);

        // Panel de info al editar: muestra el ID y la fecha de registro
        lblIdEdicion    = new JLabel("—");
        lblIdEdicion.setForeground(new Color(120, 80, 0));
        lblIdEdicion.setFont(new Font("Arial", Font.BOLD, 12));

        // NUEVO: fecha de registro (solo lectura, solo visible al editar)
        lblFechaEdicion = new JLabel("");
        lblFechaEdicion.setFont(new Font("Arial", Font.ITALIC, 10));
        lblFechaEdicion.setForeground(Color.DARK_GRAY);

        JPanel panelId = new JPanel(new BorderLayout(0, 1));
        panelId.add(lblIdEdicion,    BorderLayout.NORTH);
        panelId.add(lblFechaEdicion, BorderLayout.SOUTH);
        camposPanel.add(panelId);

        seccionDatos.add(camposPanel, BorderLayout.CENTER);

        // ── Botones ───────────────────────────────────────────
        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 4));

        btnGuardar       = crearBotonVerde("Guardar animal");
        btnEditar        = new JButton("Editar seleccionado");
        btnEliminar      = new JButton("Eliminar seleccionado");
        btnEliminar.setForeground(new Color(160, 0, 0));
        btnCancelar      = new JButton("Cancelar edición");
        btnEstadisticas  = new JButton("Ver estadísticas"); // NUEVO
        btnVerInventario = new JButton("Ver inventario");
        btnCerrarSesion  = new JButton("Cerrar sesión");

        filaBotones.add(btnGuardar);
        filaBotones.add(btnEditar);
        filaBotones.add(btnEliminar);
        filaBotones.add(btnCancelar);
        filaBotones.add(Box.createHorizontalStrut(10));
        filaBotones.add(btnEstadisticas);
        filaBotones.add(btnVerInventario);
        filaBotones.add(btnCerrarSesion);

        seccionDatos.add(filaBotones, BorderLayout.SOUTH);
        centro.add(seccionDatos, BorderLayout.NORTH);

        // ── Tabla ─────────────────────────────────────────────
        modelo        = new ModeloTablaAnimales(logica.getLista());
        tablaAnimales = new JTable(modelo);

        TableRowSorter<ModeloTablaAnimales> sorter = new TableRowSorter<>(modelo);
        tablaAnimales.setRowSorter(sorter);
        tablaAnimales.setRowHeight(22);
        tablaAnimales.getTableHeader().setReorderingAllowed(false);

        int[] anchos = {40, 100, 80, 80, 65, 60, 85, 90};
        for (int i = 0; i < anchos.length; i++) {
            tablaAnimales.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }

        JScrollPane scroll = new JScrollPane(tablaAnimales);
        scroll.setBorder(BorderFactory.createTitledBorder("Inventario actual"));

        lblTotal = new JLabel("Total: 0 ejemplares");
        lblTotal.setFont(new Font("Arial", Font.PLAIN, 11));

        JPanel panelTabla = new JPanel(new BorderLayout(3, 3));
        panelTabla.add(scroll,   BorderLayout.CENTER);
        panelTabla.add(lblTotal, BorderLayout.SOUTH);

        centro.add(panelTabla, BorderLayout.CENTER);
        panelPrincipal.add(centro, BorderLayout.CENTER);
        add(panelPrincipal);

        conectarBotones();
    }

    private void conectarBotones() {
        btnGuardar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (idEnEdicion == -1) guardarAnimalNuevo();
                else                   confirmarEdicion();
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                cargarAnimalParaEditar();
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                eliminarAnimalSeleccionado();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                cancelarEdicion();
            }
        });

        // NUEVO: la vista crea GestionEstadisticas y abre la ventana.
        // La vista NO calcula nada: solo delega al control.
        btnEstadisticas.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                GestionEstadisticas stats = new GestionEstadisticas(logica.getLista());
                new VentanaEstadisticas(stats).setVisible(true);
            }
        });

        btnVerInventario.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new VentanaInventario(
                    logica, VentanaFormulario.this, usuarioActivo
                ).setVisible(true);
            }
        });

        btnCerrarSesion.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                confirmarCierreSesion();
            }
        });
    }

    /** Muestra u oculta el botón Eliminar según el rol */
    private void configurarBotonesPorRol() {
        btnEliminar.setVisible(usuarioActivo.getRol() == Rol.ADMIN);
    }

    // ── Acciones ──────────────────────────────────────────────

    private void guardarAnimalNuevo() {
        if (!validarCampos()) return;

        logica.agregarAnimal(
            txtNombre.getText().trim(),
            txtEspecie.getText().trim(),
            txtRaza.getText().trim(),
            Double.parseDouble(txtPeso.getText().trim()),
            Integer.parseInt(txtEdad.getText().trim())
        );
        // La fecha se asigna automáticamente en GestionAnimales.
        // La vista no necesita saber ni enviar la fecha.

        JOptionPane.showMessageDialog(this,
            "Animal \"" + txtNombre.getText().trim() + "\" registrado exitosamente.",
            "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);

        limpiarCampos();
        actualizarTabla();
    }

    private void cargarAnimalParaEditar() {
        int filaSeleccionada = tablaAnimales.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un animal de la tabla primero.",
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int filaReal = tablaAnimales.convertRowIndexToModel(filaSeleccionada);
        int id       = (int) modelo.getValueAt(filaReal, 0);
        Animal animal = logica.buscarPorId(id);
        if (animal == null) return;

        // Cargamos los datos editables en el formulario
        txtNombre.setText(animal.getNombre());
        txtEspecie.setText(animal.getEspecie());
        txtRaza.setText(animal.getRaza());
        txtPeso.setText(String.valueOf(animal.getPeso()));
        txtEdad.setText(String.valueOf(animal.getEdad()));

        // Mostramos el ID y la fecha de registro (solo lectura)
        idEnEdicion = id;
        lblIdEdicion.setText(String.valueOf(id));

        // NUEVO: mostramos la fecha de registro original
        lblFechaEdicion.setText("Registrado: "
            + String.format("%02d/%02d/%d",
                animal.getFechaRegistro().getDayOfMonth(),
                animal.getFechaRegistro().getMonthValue(),
                animal.getFechaRegistro().getYear()));

        btnGuardar.setText("Guardar cambios");
        txtNombre.requestFocus();
    }

    private void confirmarEdicion() {
        if (!validarCampos()) return;

        logica.editarAnimal(
            idEnEdicion,
            txtNombre.getText().trim(),
            txtEspecie.getText().trim(),
            txtRaza.getText().trim(),
            Double.parseDouble(txtPeso.getText().trim()),
            Integer.parseInt(txtEdad.getText().trim())
        );

        JOptionPane.showMessageDialog(this, "Animal actualizado correctamente.",
            "Edición exitosa", JOptionPane.INFORMATION_MESSAGE);

        cancelarEdicion();
        actualizarTabla();
    }

    private void eliminarAnimalSeleccionado() {
        int filaSeleccionada = tablaAnimales.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un animal de la tabla primero.",
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int filaReal = tablaAnimales.convertRowIndexToModel(filaSeleccionada);
        int id       = (int) modelo.getValueAt(filaReal, 0);
        String nombre = (String) modelo.getValueAt(filaReal, 1);

        int resp = JOptionPane.showConfirmDialog(this,
            "¿Eliminar al animal \"" + nombre + "\" (ID: " + id + ")?\n"
            + "Esta acción no se puede deshacer.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (resp == JOptionPane.YES_OPTION) {
            logica.eliminarAnimal(id);
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Animal eliminado del inventario.",
                "Eliminado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cancelarEdicion() {
        idEnEdicion = -1;
        lblIdEdicion.setText("—");
        lblFechaEdicion.setText(""); // Borramos la fecha al cancelar
        btnGuardar.setText("Guardar animal");
        limpiarCampos();
        tablaAnimales.clearSelection();
    }

    // ── Validaciones ──────────────────────────────────────────

    private boolean validarCampos() {
        String nombre  = txtNombre.getText().trim();
        String especie = txtEspecie.getText().trim();
        String raza    = txtRaza.getText().trim();
        String pesoStr = txtPeso.getText().trim();
        String edadStr = txtEdad.getText().trim();

        if (nombre.isEmpty())  { mostrarError("El nombre es obligatorio.", txtNombre);   return false; }
        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            mostrarError("El nombre solo puede contener letras y espacios.", txtNombre);  return false; }
        if (especie.isEmpty()) { mostrarError("La especie es obligatoria.", txtEspecie); return false; }
        if (raza.isEmpty())    { mostrarError("La raza es obligatoria.", txtRaza);       return false; }
        if (pesoStr.isEmpty()) { mostrarError("El peso es obligatorio.", txtPeso);       return false; }
        if (edadStr.isEmpty()) { mostrarError("La edad es obligatoria.", txtEdad);       return false; }

        double peso;
        try { peso = Double.parseDouble(pesoStr); }
        catch (NumberFormatException ex) {
            mostrarError("El peso debe ser un número decimal.\nEjemplo: 250.5", txtPeso);
            return false;
        }
        if (peso <= 0)   { mostrarError("El peso debe ser mayor a 0.", txtPeso);                     return false; }
        if (peso > 2000) { mostrarError("El peso parece demasiado alto (máx. 2000 kg).", txtPeso);   return false; }

        int edad;
        try { edad = Integer.parseInt(edadStr); }
        catch (NumberFormatException ex) {
            mostrarError("La edad debe ser un número entero.\nEjemplo: 3", txtEdad);
            return false;
        }
        if (edad < 0)  { mostrarError("La edad no puede ser negativa.", txtEdad);                    return false; }
        if (edad > 30) { mostrarError("La edad parece demasiado alta (máx. 30 años).", txtEdad);     return false; }

        return true;
    }

    private void mostrarError(String mensaje, JTextField campo) {
        JOptionPane.showMessageDialog(this, mensaje,
            "Error de validación", JOptionPane.WARNING_MESSAGE);
        campo.requestFocus();
        campo.selectAll();
    }

    // ── Utilidades ────────────────────────────────────────────

    private void limpiarCampos() {
        txtNombre.setText(""); txtEspecie.setText(""); txtRaza.setText("");
        txtPeso.setText(""); txtEdad.setText("");
        txtNombre.requestFocus();
    }

    private void actualizarTabla() {
        modelo.actualizar(logica.getLista());
        int total = logica.getLista().size();
        lblTotal.setText("Total: " + total
            + " ejemplar" + (total == 1 ? "" : "es")
            + " registrado" + (total == 1 ? "" : "s"));
    }

    private JButton crearBotonVerde(String texto) {
        JButton btn = new JButton(texto);
        btn.setForeground(new Color(0, 140, 0));
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        return btn;
    }

    private void confirmarCierreSesion() {
        int r = JOptionPane.showConfirmDialog(this,
            "¿Cerrar sesión y salir?", "Confirmar cierre",
            JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            dispose();
            ventanaLogin.setVisible(true);
        }
    }
}
