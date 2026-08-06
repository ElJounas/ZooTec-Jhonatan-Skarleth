/**
 * ============================================================
 * Usuario.java — Modelo de usuario del sistema (versión 2)
 * ============================================================
 * Representa a una persona que puede iniciar sesión en Zootec.
 *
 * Novedad respecto a v1: el campo "rol" ahora es de tipo Rol
 * (nuestro enum), en lugar de un String. Esto es más seguro
 * porque Java no nos dejará asignar un valor inválido.
 * ============================================================
 */
public class Usuario {

    // ── Atributos ─────────────────────────────────────────────
    private String nombreUsuario; // Nombre con el que inicia sesión
    private String contrasena;    // Contraseña del usuario
    private Rol    rol;           // Rol: ADMIN, VETERINARIO o APRENDIZ

    // ── Constructor ───────────────────────────────────────────
    public Usuario(String nombreUsuario, String contrasena, Rol rol) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena    = contrasena;
        this.rol           = rol;
    }

    // ── Getters ───────────────────────────────────────────────
    public String getNombreUsuario() { return nombreUsuario; }
    public String getContrasena()    { return contrasena; }
    public Rol    getRol()           { return rol; }
}
