# Flujo de trabajo Git — Proyecto Zootec
## CBA Mosquera — SENA

---

## Secuencia de trabajo del equipo

El flujo que seguimos para desarrollar Zootec, explicado con el riesgo que evita cada paso:

### Paso 1 — Crear el repositorio
El responsable del repositorio crea `zootec-pecuario-equipo-XX` en GitHub con visibilidad pública, descripción clara y README inicial en main.

**Riesgo que evita:** que el proyecto quede disperso en carpetas locales o chats de WhatsApp, sin un punto de referencia único para el equipo.

### Paso 2 — Crear la rama de trabajo
Antes de tocar cualquier archivo, se crea la rama `feature-presentacion` a partir de main.

**Riesgo que evita:** introducir cambios incompletos o con errores directamente en main, lo que rompería la versión estable del sistema.

### Paso 3 — Hacer commits descriptivos
Cada cambio coherente se registra con un mensaje que explica qué se hizo y por qué. Ejemplo aplicado a Zootec: `feat: agrega clase AnimalDAO con métodos INSERT y SELECT`.

**Riesgo que evita:** perder el contexto de cada decisión técnica. Sin mensajes claros, nadie sabe por qué `GestionAnimales.java` cambió de un ArrayList a una llamada al DAO.

### Paso 4 — Abrir el pull request
Cuando los cambios en la rama están listos, se abre un pull request desde `feature-presentacion` hacia `main` con título, resumen y lista de verificaciones.

**Riesgo que evita:** integrar cambios sin que nadie los haya visto. El pull request es la barrera formal entre "está listo para mí" y "está listo para el proyecto".

### Paso 5 — Revisar
El revisor examina el diff línea por línea, no solo el resultado final, y deja al menos una observación específica y argumentada.

**Riesgo que evita:** que errores de lógica, seguridad o calidad lleguen a main sin ser detectados.

### Paso 6 — Corregir observaciones
El responsable de la rama aplica los cambios solicitados y crea un nuevo commit que responde directamente a la observación.

**Riesgo que evita:** fusionar código con problemas conocidos. El commit de corrección queda en el historial como evidencia de que la observación fue atendida.

### Paso 7 — Fusionar (merge) y eliminar la rama
El revisor aprueba, el equipo fusiona el pull request en main y elimina la rama `feature-presentacion`.

**Riesgo que evita:** que main acumule ramas obsoletas y que el historial se confunda con trabajo que ya fue integrado.

---

## Registro de observaciones de comandos Git

*(Completado durante la práctica en Learn Git Branching)*

| Comando | Observación de lo que ocurrió |
|---------|-------------------------------|
| `git commit` | Apareció un nodo nuevo en el grafo, conectado al commit anterior. La rama activa (HEAD) avanzó automáticamente para apuntar al nuevo nodo, dejando atrás el commit anterior como su padre. |
| `git branch nombre` | Se creó una nueva rama representada como una etiqueta ubicada sobre el mismo commit donde estaba HEAD en ese momento. No movió HEAD ni creó commits nuevos; solo añadió un puntero paralelo. |
| `git checkout nombre` | HEAD se desplazó de la rama anterior a la rama indicada. A partir de ese momento, los commits nuevos empezaron a construirse sobre esa rama, dejando la anterior en su última posición sin cambios. |
| `git merge nombre` | El historial de la rama indicada quedó integrado en la rama activa. Apareció un commit de merge con dos padres, uno de cada rama, uniendo las dos líneas de trabajo en un solo punto. |

---

## Acuerdo de trabajo del equipo Zootec

- Todo cambio posterior al commit inicial se realiza en una rama, nunca directamente en main.
- Los mensajes de commit siguen el formato `tipo: descripción breve` (ejemplo: `docs: agrega sección de roles en README`).
- Ningún pull request se fusiona sin al menos una observación revisada y corregida.
- No se publican en el repositorio contraseñas, tokens, números de documento ni archivos institucionales restringidos.
- Para identificar al equipo se usan usuarios de GitHub y nombres de pila únicamente.
