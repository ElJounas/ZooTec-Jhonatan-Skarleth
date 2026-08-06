import java.time.LocalDate;

/**
 * ============================================================
 * Animal.java 
 * ============================================================
 * CAPA: Modelo
 *
 * Solo guarda datos. No toma decisiones ni dibuja pantallas.
 *
 * Novedad respecto a v2:
 *   Se agrega "fechaRegistro" para saber cuándo ingresó
 *   cada animal al sistema.
 *
 * ¿Qué es LocalDate?
 *   Clase de Java que representa una fecha
 *   sin hora: año, mes y día.
 *   LocalDate.now()  →  la fecha de hoy, ej: 2026-06-18
 *   LocalDate.of(2025, 3, 10)  →  fecha específica
 * ============================================================
 */
public class Animal {

    // ── Atributos ─────────────────────────────────────────────
    private int       id;
    private String    nombre;
    private String    especie;
    private String    raza;
    private double    peso;
    private int       edad;
    private LocalDate fechaRegistro; // NUEVO: cuándo ingresó al sistema

    // ── Constructor ───────────────────────────────────────────
    public Animal(int id, String nombre, String especie, String raza, double peso, int edad, LocalDate fechaRegistro) {
        this.id            = id;
        this.nombre        = nombre;
        this.especie       = especie;
        this.raza          = raza;
        this.peso          = peso;
        this.edad          = edad;
        this.fechaRegistro = fechaRegistro;
    }

    // ── Método de negocio ─────────────────────────────────────
    /**
     * Calcula la ración diaria recomendada de alimento.
     * Fórmula: 2.5% del peso corporal.
     */
    public double calcularRacion() {
        return peso * 0.025;
    }

    // ── Getters ───────────────────────────────────────────────
    public int       getId()            { return id; }
    public String    getNombre()        { return nombre; }
    public String    getEspecie()       { return especie; }
    public String    getRaza()          { return raza; }
    public double    getPeso()          { return peso; }
    public int       getEdad()          { return edad; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }

    // ── Setters ───────────────────────────────────────────────
    // La fecha de registro NO tiene setter: una vez registrado
    // un animal, su fecha de ingreso no debe cambiar.
    public void setNombre(String nombre)   { this.nombre  = nombre; }
    public void setEspecie(String especie) { this.especie = especie; }
    public void setRaza(String raza)       { this.raza    = raza; }
    public void setPeso(double peso)       { this.peso    = peso; }
    public void setEdad(int edad)          { this.edad    = edad; }
}
