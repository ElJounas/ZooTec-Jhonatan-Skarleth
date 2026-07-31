import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * ============================================================
 * GestionEstadisticas.java — Estadísticas del inventario
 * ============================================================
 * CAPA: Control (nueva clase)
 *
 * ¿Por qué existe esta clase separada de GestionAnimales?
 *   Principio de responsabilidad única: cada clase debe hacer
 *   UNA sola cosa bien.
 *
 *   GestionAnimales  →  gestiona el inventario (agregar, editar, eliminar)
 *   GestionEstadisticas  →  calcula estadísticas sobre ese inventario
 *
 *   Si en el futuro cambiamos cómo se calculan las estadísticas,
 *   solo tocas este archivo. GestionAnimales no se toca.
 *   Eso es separar capas correctamente.
 *
 * Esta clase recibe la lista de animales ya cargada y
 * hace cálculos sobre ella. No guarda ni modifica datos.
 *
 * ¿Qué es Map<String, Integer>?
 *   Un "diccionario" que guarda pares clave → valor.
 *   Ejemplo: "Bovino" → 5, "Porcino" → 3
 *   Lo usamos para agrupar conteos y promedios por especie.
 *
 * ¿Qué es TreeMap?
 *   Un Map que ordena las claves alfabéticamente (A → Z).
 *   Así las especies siempre aparecen ordenadas en pantalla.
 * ============================================================
 */
public class GestionEstadisticas {

    // Lista sobre la que haremos los cálculos.
    // No la guardamos permanentemente: se pasa nueva cada vez
    // que se abre VentanaEstadisticas.
    private ArrayList<Animal> lista;

    // ── Constructor ───────────────────────────────────────────
    public GestionEstadisticas(ArrayList<Animal> lista) {
        this.lista = lista;
    }

    // ── Estadísticas generales ────────────────────────────────

    /** Total de animales en el inventario */
    public int totalAnimales() {
        return lista.size();
    }

    /**
     * Peso promedio de todos los animales.
     * Retorna 0 si el inventario está vacío para evitar
     * dividir entre cero (lo que causaría un error en Java).
     */
    public double promedioPesoGeneral() {
        if (lista.isEmpty()) return 0;
        double suma = 0;
        for (Animal a : lista) suma += a.getPeso();
        return suma / lista.size();
    }

    /**
     * Suma de la ración diaria de TODO el hato.
     * Útil para saber cuántos kg de alimento se necesitan al día.
     */
    public double racionTotalDiaria() {
        double total = 0;
        for (Animal a : lista) total += a.calcularRacion();
        return total;
    }

    /**
     * Retorna el animal con mayor peso.
     * Retorna null si no hay animales registrados.
     */
    public Animal animalMasPesado() {
        if (lista.isEmpty()) return null;
        Animal masPesado = lista.get(0);
        for (Animal a : lista) {
            if (a.getPeso() > masPesado.getPeso()) masPesado = a;
        }
        return masPesado;
    }

    // ── Estadísticas por especie ──────────────────────────────

    /**
     * Cuenta cuántos animales hay de cada especie.
     * Resultado ejemplo: {"Aviar"→2, "Bovino"→5, "Porcino"→3}
     *
     * getOrDefault(clave, valorPorDefecto):
     *   Si la clave ya existe → retorna su valor actual
     *   Si la clave NO existe → retorna el valor por defecto (0)
     */
    public Map<String, Integer> cantidadPorEspecie() {
        Map<String, Integer> mapa = new TreeMap<>();
        for (Animal a : lista) {
            String especie = a.getEspecie();
            mapa.put(especie, mapa.getOrDefault(especie, 0) + 1);
        }
        return mapa;
    }

    /**
     * Peso promedio agrupado por especie.
     * Resultado ejemplo: {"Bovino"→350.5, "Porcino"→95.2}
     */
    public Map<String, Double> promedioPesoPorEspecie() {
        Map<String, Double>  sumas   = new TreeMap<>();
        Map<String, Integer> conteos = new TreeMap<>();

        for (Animal a : lista) {
            String esp = a.getEspecie();
            sumas.put(esp,   sumas.getOrDefault(esp, 0.0) + a.getPeso());
            conteos.put(esp, conteos.getOrDefault(esp, 0) + 1);
        }

        Map<String, Double> promedios = new TreeMap<>();
        for (String esp : sumas.keySet()) {
            promedios.put(esp, sumas.get(esp) / conteos.get(esp));
        }
        return promedios;
    }

    /**
     * Ración total diaria agrupada por especie.
     * Útil para planificar el alimento por tipo de animal.
     */
    public Map<String, Double> racionTotalPorEspecie() {
        Map<String, Double> mapa = new TreeMap<>();
        for (Animal a : lista) {
            String esp = a.getEspecie();
            mapa.put(esp, mapa.getOrDefault(esp, 0.0) + a.calcularRacion());
        }
        return mapa;
    }
}
