import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * ============================================================
 * AnimalDAO.java — Operaciones SQL sobre la tabla animales  [NUEVO]
 * ============================================================
 * CAPA: Datos
 *
 * DAO = Data Access Object (Objeto de Acceso a Datos).
 * Es un patrón de diseño que concentra TODO el código SQL
 * en una sola clase. Las demás clases no necesitan saber
 * nada de SQL: solo llaman métodos como insertar() o
 * obtenerTodos().
 *
 * Ventaja práctica: si mañana cambias SQLite por MySQL,
 * solo modificas este archivo. Nada más cambia.
 *
 * ¿Qué es PreparedStatement?
 *   La forma SEGURA de ejecutar SQL con datos variables.
 *   Los "?" son marcadores que se reemplazan con los valores
 *   reales de forma controlada. Ejemplo:
 *
 *   "INSERT INTO animales VALUES (?, ?, ?)"
 *    pstmt.setString(1, "Bessie");   ← primer ?
 *    pstmt.setString(2, "Bovino");   ← segundo ?
 *    pstmt.setDouble(3, 380.5);      ← tercer ?
 *
 * ¿Qué es ResultSet?
 *   El resultado de un SELECT. Funciona como un cursor:
 *   rs.next() avanza a la siguiente fila y retorna true
 *   si hay más filas, false si ya terminó.
 * ============================================================
 */
public class AnimalDAO {

    // La conexión compartida que viene de ConexionBD (Singleton)
    private Connection conexion;

    public AnimalDAO() {
        this.conexion = ConexionBD.obtenerConexion();
    }

    // ─────────────────────────────────────────────────────────
    // CREATE — Insertar un animal nuevo
    // ─────────────────────────────────────────────────────────

    /**
     * Guarda un nuevo animal en la base de datos.
     * El ID lo asigna SQLite automáticamente (AUTOINCREMENT).
     * La fecha se guarda como texto "2026-06-18" (formato ISO 8601).
     */
    public void insertar(String nombre, String especie, String raza,
                         double peso, int edad, LocalDate fecha) {

        String sql = "INSERT INTO animales (nombre, especie, raza, peso, edad, fecha) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, especie);
            pstmt.setString(3, raza);
            pstmt.setDouble(4, peso);
            pstmt.setInt(5, edad);
            pstmt.setString(6, fecha.toString()); // LocalDate → "2026-06-18"
            pstmt.executeUpdate();                // Ejecuta el INSERT en la BD
        } catch (SQLException e) {
            System.err.println("❌ Error al insertar animal: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────
    // UPDATE — Actualizar un animal existente
    // ─────────────────────────────────────────────────────────

    /**
     * Modifica los datos de un animal buscándolo por su ID.
     * La fecha de registro original NO se toca.
     *
     * La cláusula WHERE id=? asegura que solo se actualiza
     * el animal con ese ID específico, no todos.
     */
    public void actualizar(int id, String nombre, String especie,
                           String raza, double peso, int edad) {

        String sql = "UPDATE animales " +
                     "SET nombre=?, especie=?, raza=?, peso=?, edad=? " +
                     "WHERE id=?";

        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, especie);
            pstmt.setString(3, raza);
            pstmt.setDouble(4, peso);
            pstmt.setInt(5, edad);
            pstmt.setInt(6, id);   // condición WHERE
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar animal: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────
    // DELETE — Eliminar un animal
    // ─────────────────────────────────────────────────────────

    /**
     * Elimina permanentemente un animal de la BD por su ID.
     * Esta operación no se puede deshacer.
     */
    public void eliminar(int id) {
        String sql = "DELETE FROM animales WHERE id=?";

        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar animal: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────
    // READ — Consultar animales
    // ─────────────────────────────────────────────────────────

    /**
     * Obtiene TODOS los animales de la BD ordenados por ID.
     *
     * Flujo:
     *   1. Ejecutamos SELECT * FROM animales
     *   2. rs.next() avanza fila por fila
     *   3. Por cada fila creamos un objeto Animal
     *   4. Lo agregamos a la lista y la retornamos
     *
     * LocalDate.parse("2026-06-18") convierte el texto de la BD
     * de vuelta a un objeto LocalDate que Java puede usar.
     */
    public ArrayList<Animal> obtenerTodos() {
        ArrayList<Animal> lista = new ArrayList<>();
        String sql = "SELECT * FROM animales ORDER BY id";

        try (Statement  stmt = conexion.createStatement();
             ResultSet  rs   = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Animal a = new Animal(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("especie"),
                    rs.getString("raza"),
                    rs.getDouble("peso"),
                    rs.getInt("edad"),
                    LocalDate.parse(rs.getString("fecha")) // "2026-06-18" → LocalDate
                );
                lista.add(a);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener animales: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca y retorna UN animal específico por su ID.
     * Se usa al cargar datos en el formulario de edición.
     *
     * @return El Animal encontrado, o null si no existe ese ID
     */
    public Animal obtenerPorId(int id) {
        String sql = "SELECT * FROM animales WHERE id=?";

        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) { // Si encontró al menos una fila
                return new Animal(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("especie"),
                    rs.getString("raza"),
                    rs.getDouble("peso"),
                    rs.getInt("edad"),
                    LocalDate.parse(rs.getString("fecha"))
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar por ID: " + e.getMessage());
        }
        return null; // No se encontró
    }
}
