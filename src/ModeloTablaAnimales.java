import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * ============================================================
 * ModeloTablaAnimales.java — Puente entre datos y JTable
 * ============================================================
 * CAPA: Vista (componente auxiliar)
 *
 * Traduce una lista de objetos Animal en filas y columnas
 * que la JTable puede mostrar en pantalla.
 *
 * Novedad respecto a v2:
 *   Se agrega la columna "Fecha registro" (última columna).
 *   Muestra la fecha en formato "dd/MM/yyyy" (más legible
 *   que el formato ISO "yyyy-MM-dd" por defecto).
 * ============================================================
 */
public class ModeloTablaAnimales extends AbstractTableModel {

    private ArrayList<Animal> lista;

    // Nombres de las columnas — ahora incluye "Fecha registro"
    private static final String[] COLUMNAS = {
        "ID", "Nombre", "Especie", "Raza",
        "Peso (kg)", "Edad (años)", "Ración (kg/día)", "Fecha registro"
    };

    public ModeloTablaAnimales(ArrayList<Animal> lista) {
        this.lista = lista;
    }

    @Override
    public int getRowCount() { return lista.size(); }

    @Override
    public int getColumnCount() { return COLUMNAS.length; }

    @Override
    public String getColumnName(int col) { return COLUMNAS[col]; }

    /**
     * Devuelve el dato de la celda (fila, columna).
     * La columna 7 (Fecha) muestra la fecha en formato dd/MM/yyyy.
     */
    @Override
    public Object getValueAt(int row, int col) {
        Animal a = lista.get(row);
        switch (col) {
            case 0: return a.getId();
            case 1: return a.getNombre();
            case 2: return a.getEspecie();
            case 3: return a.getRaza();
            case 4: return String.format("%.1f", a.getPeso());
            case 5: return a.getEdad();
            case 6: return String.format("%.2f", a.calcularRacion());
            case 7:
                // Formateamos la fecha a "18/06/2026" en lugar de "2026-06-18"
                return String.format("%02d/%02d/%d",
                    a.getFechaRegistro().getDayOfMonth(),
                    a.getFechaRegistro().getMonthValue(),
                    a.getFechaRegistro().getYear());
            default: return "";
        }
    }

    /** Las celdas no son editables directamente en la tabla */
    @Override
    public boolean isCellEditable(int row, int col) { return false; }

    /**
     * Reemplaza la lista y le avisa a la JTable que se repinte.
     * Se llama después de cada agregar, editar o eliminar.
     */
    public void actualizar(ArrayList<Animal> nuevaLista) {
        this.lista = nuevaLista;
        fireTableDataChanged(); // Le dice a la JTable: "los datos cambiaron"
    }
}
