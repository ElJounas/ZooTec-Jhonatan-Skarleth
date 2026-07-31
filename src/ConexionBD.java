import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * ============================================================
 * ConexionBD.java — Conexión a la base de datos SQLite  [NUEVO]
 * ============================================================
 * CAPA: Datos
 *
 * ¿Qué es SQLite?
 *   Una base de datos que vive en un solo archivo (.db) en el
 *   computador. No necesita servidor ni instalación especial.
 *   Al ejecutar el programa por primera vez aparece el archivo
 *   "zootec.db" en la carpeta del proyecto. Ahí se guardan
 *   permanentemente todos los animales registrados.
 *
 * ¿Qué es el patrón Singleton?
 *   Garantiza que solo exista UNA conexión a la BD durante
 *   todo el programa. Todas las clases que necesiten hablar
 *   con la BD usan ESTA misma conexión.
 *   ¿Por qué una sola? Abrir y cerrar conexiones cuesta recursos.
 *   Es más eficiente abrirla una vez y compartirla.
 *
 * ¿Qué es JDBC?
 *   Java Database Connectivity. Es la forma estándar de Java
 *   para comunicarse con bases de datos. Necesitamos el archivo
 *   sqlite-jdbc.jar para que JDBC sepa hablar con SQLite.
 * ============================================================
 */
public class ConexionBD {

    // Nombre del archivo donde SQLite guardará los datos.
    // Se crea automáticamente en la carpeta del proyecto.
    private static final String ARCHIVO_BD = "zootec.db";

    // URL que le dice a JDBC: "usa SQLite y este archivo"
    private static final String URL = "jdbc:sqlite:" + ARCHIVO_BD;

    // La única conexión que existirá en todo el programa.
    // "static" = pertenece a la clase, no a un objeto.
    // Empieza en null porque aún no se ha abierto.
    private static Connection conexion = null;

    // Constructor privado: nadie puede hacer "new ConexionBD()"
    // Solo se accede por el método obtenerConexion()
    private ConexionBD() {}

    /**
     * Retorna la conexión a la base de datos.
     * Si todavía no existe, la crea (patrón Singleton).
     *
     * Primera llamada:  conexion == null → la crea y guarda
     * Siguientes veces: conexion ya existe → la devuelve directo
     */
    public static Connection obtenerConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {

                // Le decimos a Java qué tipo de base de datos vamos a usar
                Class.forName("org.sqlite.JDBC");

                // Abrimos la conexión. Si zootec.db no existe, SQLite lo crea solo.
                conexion = DriverManager.getConnection(URL);

                System.out.println("✅ Base de datos conectada: " + ARCHIVO_BD);

                // Creamos la tabla animales si es la primera vez que se ejecuta
                crearTablas();
            }
        } catch (Exception e) {
            System.err.println("❌ Error al conectar a la BD: " + e.getMessage());
        }
        return conexion;
    }

    /**
     * Crea la tabla 'animales' en la base de datos.
     *
     * "IF NOT EXISTS" es clave: si la tabla ya fue creada en
     * una ejecución anterior del programa, no da error, la ignora.
     *
     * Tipos de datos en SQLite:
     *   INTEGER  → número entero      (id, edad)
     *   TEXT     → texto              (nombre, especie, raza, fecha)
     *   REAL     → número decimal     (peso)
     *
     * PRIMARY KEY AUTOINCREMENT:
     *   SQLite asigna el ID automáticamente, empezando en 1
     *   y subiendo de 1 en 1 con cada nuevo registro.
     *   Nosotros nunca tenemos que calcular el ID manualmente.
     */
    private static void crearTablas() {
        String sql =
            "CREATE TABLE IF NOT EXISTS animales (" +
            "  id      INTEGER  PRIMARY KEY AUTOINCREMENT, " +
            "  nombre  TEXT     NOT NULL, " +
            "  especie TEXT     NOT NULL, " +
            "  raza    TEXT     NOT NULL, " +
            "  peso    REAL     NOT NULL, " +
            "  edad    INTEGER  NOT NULL, " +
            "  fecha   TEXT     NOT NULL"  + // Guardamos la fecha como "2026-06-18"
            ")";

        // try-with-resources: cierra el Statement automáticamente al terminar,
        // aunque haya un error. Evita fugas de recursos.
        try (Statement stmt = conexion.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Tabla 'animales' lista.");
        } catch (Exception e) {
            System.err.println("❌ Error al crear la tabla: " + e.getMessage());
        }
    }

    /**
     * Cierra la conexión de forma segura.
     * Se llama cuando el programa termina para liberar el archivo .db.
     */
    public static void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("✅ Conexión cerrada correctamente.");
            }
        } catch (Exception e) {
            System.err.println("❌ Error al cerrar la BD: " + e.getMessage());
        }
    }
}
