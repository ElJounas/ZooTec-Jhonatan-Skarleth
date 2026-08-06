import java.time.LocalDate;
import java.util.ArrayList;

/**
 * ============================================================
 * GestionAnimales.java — Lógica del inventario (versión 3)
 * ============================================================
 * CAPA: Control
 *
 * Es el intermediario entre las Ventanas y la base de datos:
 *
 *   VentanaFormulario
 *         │  llama métodos simples como agregarAnimal()
 *         ▼
 *   GestionAnimales   ← lógica de negocio (esta clase)
 *         │  delega el SQL a
 *         ▼
 *       AnimalDAO     ← código SQL
 *         │  escribe y lee en
 *         ▼
 *       SQLite        ← archivo zootec.db en disco
 *
 * Cambio respecto a v2b:
 *   Antes usaba un ArrayList en memoria (datos se perdían
 *   al cerrar el programa). Ahora usa AnimalDAO que lee y
 *   escribe en SQLite, por lo que los datos persisten entre
 *   sesiones.
 *
 *   La interfaz pública (los métodos que llaman las Ventanas)
 *   es EXACTAMENTE igual a la de v2b. Las Ventanas no se
 *   enteran de que ahora hay una base de datos detrás.
 *   Eso es lo que busca la separación de capas.
 * ============================================================
 */
public class GestionAnimales {

    // El DAO se encarga de toda la comunicación con SQLite
    private AnimalDAO dao;

    // ── Constructor ───────────────────────────────────────────
    public GestionAnimales() {
        this.dao = new AnimalDAO();
    }

    // ── Operaciones del inventario ────────────────────────────

    /**
     * Registra un nuevo animal en la base de datos.
     *
     * La fecha de registro se asigna AQUÍ con LocalDate.now().
     * La ventana no la envía: esa decisión pertenece a la
     * lógica de negocio, no a la vista.
     */
    public void agregarAnimal(String nombre, String especie,
                              String raza, double peso, int edad) {
        dao.insertar(nombre, especie, raza, peso, edad, LocalDate.now());
    }

    /**
     * Actualiza los datos de un animal existente.
     * Se identifica por su ID. La fecha original no cambia.
     */
    public void editarAnimal(int id, String nombre, String especie,
                             String raza, double peso, int edad) {
        dao.actualizar(id, nombre, especie, raza, peso, edad);
    }

    /**
     * Elimina un animal de la base de datos permanentemente.
     * Solo disponible para Administradores (controlado en la Vista).
     */
    public void eliminarAnimal(int id) {
        dao.eliminar(id);
    }

    /**
     * Retorna todos los animales del inventario.
     * Cada llamada consulta la BD para tener datos frescos.
     * Se usa para mostrar la JTable y calcular estadísticas.
     */
    public ArrayList<Animal> getLista() {
        return dao.obtenerTodos();
    }

    /**
     * Busca un animal específico por su ID.
     * Se usa al cargar datos en el formulario de edición.
     *
     * @return El Animal encontrado, o null si no existe
     */
    public Animal buscarPorId(int id) {
        return dao.obtenerPorId(id);
    }
}
