import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * ============================================================
 * VentanaEstadisticas.java — Estadísticas del inventario
 * ============================================================
 * CAPA: Vista (nueva clase)
 *
 * Muestra un resumen analítico del inventario:
 *   - 4 tarjetas con métricas clave en la parte superior
 *   - Tabla con desglose por especie en la parte inferior
 *
 * Esta ventana NO calcula nada: recibe un objeto
 * GestionEstadisticas ya cargado con los datos y solo
 * se encarga de mostrarlos de forma visual.
 *
 * Eso es la separación de capas:
 *   VentanaEstadisticas  →  solo muestra (Vista)
 *   GestionEstadisticas  →  solo calcula (Control)
 *
 * ¿Qué es una "tarjeta"?
 *   Un JPanel con borde y fondo de color que destaca
 *   visualmente un único dato importante.
 * ============================================================
 */
public class VentanaEstadisticas extends JFrame {

    private GestionEstadisticas stats;

    // ── Constructor ───────────────────────────────────────────
    public VentanaEstadisticas(GestionEstadisticas stats) {
        this.stats = stats;
        configurarVentana();
        crearComponentes();
    }

    private void configurarVentana() {
        setTitle("Zootec — Estadísticas del inventario");
        setSize(640, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void crearComponentes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 15, 15, 15));

        // ── Encabezado ────────────────────────────────────────
        JLabel titulo = new JLabel("Estadísticas del inventario", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(new Color(0, 100, 0));
        JLabel subtitulo = new JLabel("Análisis general del hato pecuario", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 11));
        subtitulo.setForeground(Color.DARK_GRAY);
        JPanel encabezado = new JPanel(new BorderLayout(2, 2));
        encabezado.add(titulo,    BorderLayout.NORTH);
        encabezado.add(subtitulo, BorderLayout.SOUTH);
        panel.add(encabezado, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(10, 10));

        // ── Tarjetas de métricas clave ────────────────────────
        // 4 tarjetas en fila horizontal con los datos más importantes
        JPanel panelTarjetas = new JPanel(new GridLayout(1, 4, 10, 0));

        panelTarjetas.add(crearTarjeta(
            "Total de ejemplares",
            String.valueOf(stats.totalAnimales()),
            new Color(232, 245, 233)  // verde claro
        ));

        panelTarjetas.add(crearTarjeta(
            "Peso promedio general",
            String.format("%.1f kg", stats.promedioPesoGeneral()),
            new Color(227, 242, 253)  // azul claro
        ));

        panelTarjetas.add(crearTarjeta(
            "Ración total diaria",
            String.format("%.2f kg", stats.racionTotalDiaria()),
            new Color(255, 243, 224)  // naranja claro
        ));

        // Tarjeta del animal más pesado: muestra nombre y peso
        Animal masPesado = stats.animalMasPesado();
        String textoMasPesado = (masPesado != null)
            ? masPesado.getNombre() + "\n" + masPesado.getPeso() + " kg"
            : "Sin datos";
        panelTarjetas.add(crearTarjeta(
            "Ejemplar más pesado",
            textoMasPesado,
            new Color(243, 229, 245)  // morado claro
        ));

        centro.add(panelTarjetas, BorderLayout.NORTH);

        // ── Tabla de desglose por especie ─────────────────────
        Map<String, Integer> cantidades = stats.cantidadPorEspecie();
        Map<String, Double>  promedios  = stats.promedioPesoPorEspecie();
        Map<String, Double>  raciones   = stats.racionTotalPorEspecie();

        String[] columnas = {
            "Especie", "Cantidad", "Peso promedio (kg)", "Ración total/día (kg)"
        };

        // Construimos la matriz de datos para la tabla
        String[][] filas = new String[cantidades.size()][4];
        int i = 0;
        for (String especie : cantidades.keySet()) {
            filas[i][0] = especie;
            filas[i][1] = String.valueOf(cantidades.get(especie));
            filas[i][2] = String.format("%.1f", promedios.getOrDefault(especie, 0.0));
            filas[i][3] = String.format("%.2f", raciones.getOrDefault(especie, 0.0));
            i++;
        }

        // Tabla de solo lectura (anónima con isCellEditable sobreescrito)
        JTable tablaEspecies = new JTable(filas, columnas) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaEspecies.setRowHeight(24);
        tablaEspecies.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaEspecies.getTableHeader().setReorderingAllowed(false);
        tablaEspecies.getColumnModel().getColumn(0).setPreferredWidth(130);
        tablaEspecies.getColumnModel().getColumn(1).setPreferredWidth(70);
        tablaEspecies.getColumnModel().getColumn(2).setPreferredWidth(150);
        tablaEspecies.getColumnModel().getColumn(3).setPreferredWidth(150);

        JScrollPane scroll = new JScrollPane(tablaEspecies);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Desglose por especie"),
            BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        centro.add(scroll, BorderLayout.CENTER);
        panel.add(centro, BorderLayout.CENTER);

        // ── Botón cerrar ──────────────────────────────────────
        JButton btnCerrar = new JButton("Cerrar estadísticas");
        btnCerrar.addActionListener(e -> dispose());
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(btnCerrar);
        panel.add(panelBoton, BorderLayout.SOUTH);

        add(panel);
    }

    /**
     * Crea una tarjeta visual con un título pequeño y un valor grande.
     *
     * Si el valor tiene salto de línea (\n), lo divide en dos renglones.
     * Ejemplo: "Bessie\n380.5 kg" → dos líneas dentro de la tarjeta.
     *
     * @param titulo     Texto descriptivo (pequeño, gris)
     * @param valor      Dato a destacar (grande, negro)
     * @param colorFondo Color de fondo de la tarjeta
     */
    private JPanel crearTarjeta(String titulo, String valor, Color colorFondo) {
        JPanel tarjeta = new JPanel(new BorderLayout(4, 4));
        tarjeta.setBackground(colorFondo);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorFondo.darker(), 1),
            BorderFactory.createEmptyBorder(10, 8, 10, 8)));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 10));
        lblTitulo.setForeground(Color.DARK_GRAY);

        // Si el valor tiene \n, creamos una etiqueta por cada línea
        JPanel panelValor = new JPanel(new GridLayout(0, 1, 2, 2));
        panelValor.setOpaque(false);
        for (String linea : valor.split("\n")) {
            JLabel lbl = new JLabel(linea, SwingConstants.CENTER);
            lbl.setFont(new Font("Arial", Font.BOLD, 15));
            panelValor.add(lbl);
        }

        tarjeta.add(lblTitulo,  BorderLayout.NORTH);
        tarjeta.add(panelValor, BorderLayout.CENTER);
        return tarjeta;
    }
}
