# Conceptos Git y GitHub — Proyecto Zootec
## CBA Mosquera — SENA

---

## Comprobación conceptual

### 1. ¿Qué puede hacer Git aunque GitHub no exista?

Git puede registrar y controlar el historial de cambios de un proyecto completamente en local, es decir, en el computador sin necesidad de internet ni de ninguna plataforma externa. Por ejemplo, durante el desarrollo de Zootec pudimos guardar versiones de `Animal.java` o `GestionAnimales.java` en distintos momentos, volver a una versión anterior si algo fallaba, y ver exactamente qué líneas cambiaron entre una versión y otra, todo esto sin subir nada a GitHub. Git es el motor; GitHub es solo el lugar donde ese motor se comparte con otros.

---

### 2. ¿Por qué una rama reduce el riesgo de dañar main?

Porque una rama es una copia paralela del proyecto donde se puede trabajar libremente sin tocar la versión principal. En Zootec, cuando pasamos de la v2 a la v3 agregando SQLite, hicimos esos cambios en una rama separada. Si algo salía mal con `ConexionBD.java` o `AnimalDAO.java`, la rama `main` seguía intacta con la versión funcional. Solo cuando los cambios estuvieron probados y revisados se fusionaron con main. Sin rama, cualquier error en el código nuevo habría roto la versión que ya funcionaba.

---

### 3. ¿Qué diferencia existe entre guardar un archivo y crear un commit?

Guardar un archivo solo actualiza el archivo en el disco duro; si algo falla después, ese cambio puede perderse o sobrescribirse sin dejar rastro. Un commit, en cambio, es un registro permanente e identificable dentro del historial de Git: guarda qué cambió, quién lo cambió, cuándo y por qué. Por ejemplo, en Zootec guardar `GestionAnimales.java` después de agregar el método `editarAnimal()` no deja ningún registro útil; hacer un commit con el mensaje `feat: agrega método editarAnimal con búsqueda por ID` sí documenta exactamente qué se hizo y permite volver a ese punto si es necesario.

---

### 4. ¿Por qué un pull request no es lo mismo que un merge?

Un merge es la acción técnica de unir dos ramas. Un pull request es el proceso de revisión y aprobación que ocurre antes de que ese merge suceda. En Zootec, si un integrante trabajó en la rama `feature-sqlite` y quiere integrar sus cambios en main, primero abre un pull request para que otro integrante revise el código, haga observaciones y confirme que todo está correcto. Solo después de esa revisión y aprobación se ejecuta el merge. Un merge sin pull request sería integrar cambios sin que nadie más los haya visto, lo cual es exactamente el problema que tuvo AulaConecta con los archivos `index-final-caro.html`.

---

### 5. ¿Qué evidencia permite saber quién cambió algo y por qué?

El historial de commits de Git. Cada commit tiene un identificador único (hash), el nombre del autor, la fecha y hora, y el mensaje descriptivo. En Zootec, si alguien modifica `VentanaFormulario.java` para corregir una validación, el commit queda registrado con todos esos datos. Desde GitHub se puede ver el diff (las líneas exactas que cambiaron), quién las cambió y qué mensaje dejó. Eso hace que sea imposible decir "yo no fui" o "no sé por qué cambió eso", que era exactamente el problema del caso AulaConecta.

---

## Reto rápido de secuencia

Los siete pasos ordenados y el riesgo que evita cada uno:

| # | Paso | Riesgo que evita |
|---|------|-----------------|
| 1 | **Crear repositorio** | Evita que el proyecto viva solo en carpetas locales o mensajería, donde cualquiera puede sobrescribir el trabajo de otro sin dejar rastro. |
| 2 | **Crear rama** | Evita trabajar directamente en main, lo que podría romper la versión estable del proyecto si se introduce un error. |
| 3 | **Hacer commits** | Evita perder el historial de decisiones. Sin commits descriptivos, es imposible saber qué cambió, cuándo y por qué, como pasó con los archivos `index-final-2.html`. |
| 4 | **Abrir pull request** | Evita que cambios no revisados lleguen a main. Es el mecanismo formal para pedir que alguien más valide el trabajo antes de integrarlo. |
| 5 | **Revisar** | Evita que errores de lógica, seguridad o calidad pasen desapercibidos. Una segunda persona puede ver cosas que el autor no notó. |
| 6 | **Corregir observaciones** | Evita fusionar código con problemas conocidos. La corrección queda registrada como un commit nuevo, lo que mantiene la trazabilidad. |
| 7 | **Fusionar (merge)** | Solo ocurre después de que todo lo anterior fue completado correctamente, lo que garantiza que main siempre contiene una versión revisada y aprobada. |
