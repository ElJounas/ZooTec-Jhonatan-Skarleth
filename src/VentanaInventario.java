import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.List;

/**
 * ============================================================
 * VentanaInventario.java — Consulta del inventario
 * ============================================================
 * CAPA: Vista
 *
 * Muestra el inventario en una JTable con filtros de búsqueda.
 * Accesible para todos los roles.
 *
 * Novedades respecto a v2:
 *   - Filtro por NOMBRE (texto libre) y por ESPECIE (dropdown)
 *     que se pueden combinar: nombre + especie al mismo tiempo.
 *   - El JComboBox de especies se llena dinámicamente con las
 *     especies que realmente existen en el inventario, no
 *     con una lista hardcodeada.
 *   - Botón "Ver estadísticas" disponible desde aquí también.
 *   - La tabla ahora muestra la fecha de registro.
 *
 * ¿Cómo funciona el filtro combinado?
 *   RowFilter.andFilter() recibe una lista de filtros y
 *   solo muestra filas que cumplan TODOS al mismo tiempo.
 *   Ejemplo: nombre contiene "bes" AND especie = "Bovino"
 * ============================================================
 */
public class VentanaInventario extends JFrame {

    private JTable              tablaAnimales;
    private ModeloTablaAnimales modelo;
    private JTextField          txtBuscarNombre;
    private JComboBox<String>   cmbEspecie;
    private JLabel              lblTotal;

    private JButton btnFiltrar;
    private JButton btnLimpiar;
    private JButton btnActualizar;
    private JButton btnEstadisticas;
    private JButton btnCerrar;

    private GestionAnimales logica;
    private JFrame          ventanaAnterior;
    private Usuario         usuarioActivo;

    // El sorter permite ordenar y filtrar la tabla sin cambiar el modelo
    private TableRowSorter<ModeloTablaAnimales> sorter;

    // ── Constructor ───────────────────────────────────────────
    public VentanaInventario(GestionAnimales logica,
                             JFrame ventanaAnterior,
                             Usuario usuarioActivo) {
        this.logica          = logica;
        this.ventanaAnterior = ventanaAnterior;
        this.usuarioActivo   = usuarioActivo;

        configurarVentana();
        crearComponentes();
        actualizarTabla();
        poblarComboEspecies();
    }

    private void configurarVentana() {
        setTitle("Zootec — Consulta de inventario — "
                 + usuarioActivo.getNombreUsuario());
        setSize(780, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void crearComponentes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        // ── Encabezado ────────────────────────────────────────
        JLabel titulo = new JLabel("Zootec", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(new Color(0, 100, 0));

        String rolTexto;
        switch (usuarioActivo.getRol()) {
            case ADMIN:       rolTexto = "Administrador";    break;
            case VETERINARIO: rolTexto = "Veterinario";      break;
            default:          rolTexto = "Aprendiz/Usuario"; break;
        }
        JLabel subtitulo = new JLabel(
            "Panel de consulta — " + rolTexto + " (Solo lectura)",
            SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 11));

        JPanel encabezado = new JPanel(new BorderLayout(2, 2));
        encabezado.add(titulo,    BorderLayout.NORTH);
        encabezado.add(subtitulo, BorderLayout.SOUTH);
        panel.add(encabezado, BorderLayout.NORTH);

        // ── Panel de filtros ──────────────────────────────────
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros de búsqueda"));

        // Filtro 1: búsqueda por nombre (texto libre)
        panelFiltros.add(new JLabel("Nombre:"));
        txtBuscarNombre = new JTextField(14);
        panelFiltros.add(txtBuscarNombre);

        // Filtro 2: filtro por especie con JComboBox (dropdown).
        // Se llena dinámicamente con las especies del inventario.
        panelFiltros.add(new JLabel("Especie:"));
        cmbEspecie = new JComboBox<>();
        cmbEspecie.addItem("Todas"); // Opción por defecto: sin filtro por especie
        cmbEspecie.setPreferredSize(new Dimension(120, 26));
        panelFiltros.add(cmbEspecie);

        btnFiltrar = crearBotonVerde("Aplicar filtro");
        btnLimpiar = new JButton("Limpiar");
        panelFiltros.add(btnFiltrar);
        panelFiltros.add(btnLimpiar);

        panelFiltros.add(Box.createHorizontalStrut(12));

        btnActualizar   = crearBotonVerde("Actualizar lista");
        btnEstadisticas = new JButton("Ver estadísticas");
        panelFiltros.add(btnActualizar);
        panelFiltros.add(btnEstadisticas);

        // ── Tabla ─────────────────────────────────────────────
        modelo        = new ModeloTablaAnimales(logica.getLista());
        tablaAnimales = new JTable(modelo);

        sorter = new TableRowSorter<>(modelo);
        tablaAnimales.setRowSorter(sorter);
        tablaAnimales.setRowHeight(22);
        tablaAnimales.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaAnimales.getTableHeader().setReorderingAllowed(false);

        int[] anchos = {40, 100, 80, 80, 65, 60, 85, 90};
        for (int i = 0; i < anchos.length; i++) {
            tablaAnimales.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }

        JScrollPane scroll = new JScrollPane(tablaAnimales);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Inventario pecuario"),
            BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        lblTotal = new JLabel("Total: 0 ejemplares");
        lblTotal.setFont(new Font("Arial", Font.PLAIN, 11));

        JPanel panelTabla = new JPanel(new BorderLayout(3, 3));
        panelTabla.add(scroll,   BorderLayout.CENTER);
        panelTabla.add(lblTotal, BorderLayout.SOUTH);

        // ── Layout central ────────────────────────────────────
        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.add(panelFiltros, BorderLayout.NORTH);
        centro.add(panelTabla,   BorderLayout.CENTER);
        panel.add(centro, BorderLayout.CENTER);

        // ── Botón cerrar ──────────────────────────────────────
        btnCerrar = new JButton("Cerrar reporte");
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(btnCerrar);
        panel.add(panelBoton, BorderLayout.SOUTH);

        add(panel);
        conectarBotones();
    }

    private void conectarBotones() {

        // Aplica los filtros combinados al hacer clic o presionar Enter
        ActionListener accionFiltrar = new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { aplicarFiltros(); }
        };
        btnFiltrar.addActionListener(accionFiltrar);
        txtBuscarNombre.addActionListener(accionFiltrar);

        // Limpia todos los filtros y muestra el inventario completo
        btnLimpiar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                txtBuscarNombre.setText("");
                cmbEspecie.setSelectedIndex(0); // Vuelve a "Todas"
                sorter.setRowFilter(null);       // Quita cualquier filtro activo
                actualizarContador();
            }
        });

        // Actualiza los datos y el dropdown de especies
        btnActualizar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                actualizarTabla();
                poblarComboEspecies();
                sorter.setRowFilter(null);
                txtBuscarNombre.setText("");
                cmbEspecie.setSelectedIndex(0);
            }
        });

        // Abre la ventana de estadísticas (la vista delega al control)
        btnEstadisticas.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                GestionEstadisticas stats = new GestionEstadisticas(logica.getLista());
                new VentanaEstadisticas(stats).setVisible(true);
            }
        });

        btnCerrar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                dispose();
                if (ventanaAnterior != null) ventanaAnterior.setVisible(true);
            }
        });
    }

    /**
     * Aplica filtros combinados: nombre AND especie.
     *
     * RowFilter.regexFilter("(?i)texto", columna):
     *   Filtra por texto en una columna específica.
     *   "(?i)" ignora mayúsculas/minúsculas.
     *
     * "^" y "$" en el filtro de especie indican inicio y fin,
     * así "Bovino" no coincide con "Bovino lechero" por error.
     *
     * RowFilter.andFilter(lista):
     *   Solo pasan las filas que cumplen TODOS los filtros de la lista.
     */
    private void aplicarFiltros() {
        String nombre  = txtBuscarNombre.getText().trim();
        String especie = (String) cmbEspecie.getSelectedItem();

        // Lista de filtros activos que iremos llenando
        List<RowFilter<ModeloTablaAnimales, Object>> filtros = new ArrayList<>();

        // Filtro por nombre (columna 1) — solo si escribieron algo
        if (!nombre.isEmpty()) {
            filtros.add(RowFilter.regexFilter("(?i)" + nombre, 1));
        }

        // Filtro por especie (columna 2) — solo si eligieron algo distinto de "Todas"
        if (especie != null && !especie.equals("Todas")) {
            // "^especie$" = coincidencia exacta de la especie completa
            filtros.add(RowFilter.regexFilter("(?i)^" + especie + "$", 2));
        }

        // Aplicamos los filtros
        if (filtros.isEmpty()) {
            sorter.setRowFilter(null); // Sin filtros: muestra todo
        } else {
            sorter.setRowFilter(RowFilter.andFilter(filtros));
        }

        actualizarContador();
    }

    /**
     * Llena el JComboBox con las especies únicas del inventario actual.
     *
     * TreeSet: colección que elimina duplicados automáticamente
     * y ordena los elementos alfabéticamente.
     * Así si hay 3 bovinos, "Bovino" solo aparece una vez en el combo.
     */
    private void poblarComboEspecies() {
        String seleccionActual = (String) cmbEspecie.getSelectedItem();

        cmbEspecie.removeAllItems();
        cmbEspecie.addItem("Todas");

        Set<String> especies = new TreeSet<>();
        for (Animal a : logica.getLista()) {
            especies.add(a.getEspecie());
        }
        for (String esp : especies) {
            cmbEspecie.addItem(esp);
        }

        // Restauramos la selección si todavía existe en el nuevo combo
        if (seleccionActual != null) {
            cmbEspecie.setSelectedItem(seleccionActual);
        }
    }

    /** Refresca la tabla con los datos actuales */
    public void actualizarTabla() {
        modelo.actualizar(logica.getLista());
        actualizarContador();
    }

    /** Actualiza la etiqueta de total mostrando visibles vs total */
    private void actualizarContador() {
        int visibles = tablaAnimales.getRowCount();
        int total    = logica.getLista().size();

        if (visibles == total) {
            lblTotal.setText("Total: " + total
                + " ejemplar" + (total == 1 ? "" : "es") + " en el inventario");
        } else {
            lblTotal.setText("Mostrando " + visibles + " de " + total + " ejemplares");
        }
    }

    private JButton crearBotonVerde(String texto) {
        JButton btn = new JButton(texto);
        btn.setForeground(new Color(0, 140, 0));
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        return btn;
    }
}
