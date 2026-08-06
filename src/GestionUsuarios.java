import java.util.ArrayList;

/**
 * ============================================================
 * GestionUsuarios.java — Lógica de autenticación (versión 2)
 * ============================================================
 * Esta clase se encarga de verificar si un usuario puede
 * entrar al sistema y qué puede hacer según su rol.
 *
 * Novedad respecto a v1: los usuarios ahora tienen Rol.ADMIN,
 * Rol.VETERINARIO o Rol.APRENDIZ en lugar de "admin"/"usuario".
 * ============================================================
 */
public class GestionUsuarios {

    // Lista donde se guardan todos los usuarios del sistema
    private ArrayList<Usuario> listaUsuarios;

    // ── Constructor ───────────────────────────────────────────
    // Al crear GestionUsuarios, se cargan los usuarios predefinidos.
    // En un sistema real, estos vendrían de una base de datos.
    public GestionUsuarios() {
        listaUsuarios = new ArrayList<>();

        // Usuarios de prueba con sus roles asignados
        listaUsuarios.add(new Usuario("admin",    "1234",   Rol.ADMIN));
        listaUsuarios.add(new Usuario("vet01",    "pass1",  Rol.VETERINARIO));
        listaUsuarios.add(new Usuario("vet02",    "pass2",  Rol.VETERINARIO));
        listaUsuarios.add(new Usuario("aprendiz", "abc123", Rol.APRENDIZ));
    }

    /**
     * Busca si existe un usuario con ese nombre y contraseña.
     * Retorna el objeto Usuario si lo encuentra, o null si no existe.
     *
     * ¿Por qué retornamos el objeto completo en lugar de solo true/false?
     * Porque la ventana que llama a este método necesita saber el ROL
     * del usuario para saber qué ventana abrir después.
     */
    public Usuario autenticar(String user, String pass) {
        // Recorremos la lista comparando uno por uno
        for (Usuario u : listaUsuarios) {
            if (u.getNombreUsuario().equals(user) &&
                u.getContrasena().equals(pass)) {
                return u; // ¡Encontrado! Login correcto
            }
        }
        return null; // No existe ese usuario o la contraseña es incorrecta
    }
}
