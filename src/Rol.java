/**
 * ============================================================
 * Rol.java — Enumerado de roles del sistema
 * ============================================================
 * Un "enum" es una lista fija de opciones. En lugar de usar
 * Strings sueltos como "admin" o "usuario" (que son fáciles
 * de escribir mal), usamos este enum para que Java nos avise
 * si nos equivocamos al escribir un rol.
 *
 * Ejemplo de uso:
 *   Rol miRol = Rol.ADMIN;
 *   if (miRol == Rol.APRENDIZ) { ... }
 * ============================================================
 */
public enum Rol {
    ADMIN,        // Puede registrar, editar Y eliminar animales
    VETERINARIO,  // Puede registrar y editar, pero NO eliminar
    APRENDIZ      // Solo puede consultar el inventario (solo lectura)
}
