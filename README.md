# Zootec — Sistema de Gestión de Inventario Pecuario
### CBA Mosquera — SENA | Programación de Software
 
---
 
## Descripción del proyecto
 
**Zootec** es un sistema de información para el área pecuaria del Centro de Biotecnología Agropecuaria (CBA) de la regional Mosquera del SENA. Permite registrar, consultar, editar y analizar el inventario de animales del centro de forma centralizada, reemplazando el control manual disperso en libretas, hojas de Excel y mensajes de WhatsApp.
 
---
 
## Problema que resuelve
 
El área pecuaria del CBA gestionaba su inventario de animales en libretas de campo y múltiples archivos de Excel sin conexión entre sí. Esto generaba:
 
- Pérdida de datos cuando las libretas se mojaban o extraviaban.
- Hasta 30 minutos para consolidar el historial de un solo animal.
- Imposibilidad de saber quién registró qué información ni cuándo.
- Ausencia de estadísticas automáticas para tomar decisiones de manejo.
**Objetivo del sistema:** proveer una aplicación de escritorio en Java que centralice el registro de animales, controle el acceso según el rol del usuario y genere estadísticas del inventario de forma automática.
 
---
 
## Alcance inicial
 
El sistema cubre los siguientes módulos en su versión 3 (versión actual):
 
| Módulo | Descripción |
|--------|-------------|
| Autenticación | Login con 3 roles: Administrador, Veterinario y Aprendiz |
| Registro de animales | Formulario con validaciones para nombre, especie, raza, peso y edad |
| Inventario | Tabla con ordenamiento por columna y filtros combinados (nombre + especie) |
| Estadísticas | Panel con métricas clave y desglose automático por especie |
| Persistencia | Base de datos SQLite local (archivo `zootec.db`) |
 
---
 
## Equipo
 
| Nombre / Iniciales | Usuario GitHub | Rol en el proyecto |
|--------------------|----------------|-------------------|
| Jhonatan Londoño| @ElJounas | Responsable del repositorio |
| Skarleth Aya | @daiannrodriguez-boceto | Responsable de la rama y commits |
| Jhonatan Londoño | @ElJounas | Revisor / Auditor de evidencia |
 
---
 
## Ruta tecnológica
 
El sistema fue construido en **Java con Swing** para la interfaz gráfica y **SQLite** para la persistencia de datos. La comunicación con la base de datos se realiza a través del patrón **DAO (Data Access Object)**.
 
| Capa | Tecnología | Propósito |
|------|-----------|-----------|
| Vista | Java Swing | Ventanas, formularios y tablas |
| Control | Java (clases de gestión) | Lógica de negocio y estadísticas |
| Datos | JDBC + SQLite | Persistencia del inventario |
| Modelo | Clases Java (POJO) | Representación de Animal y Usuario |
 
### Archivos principales del sistema
 
```
zootec/
├── Main.java                  ← Punto de entrada
├── Rol.java                   ← Enum de roles
├── Animal.java                ← Modelo de datos
├── Usuario.java               ← Modelo de usuario
├── ConexionBD.java            ← Conexión SQLite (Singleton)
├── AnimalDAO.java             ← Operaciones SQL (DAO)
├── GestionAnimales.java       ← Lógica del inventario
├── GestionUsuarios.java       ← Autenticación
├── GestionEstadisticas.java   ← Cálculo de estadísticas
├── ModeloTablaAnimales.java   ← Puente datos-JTable
├── VentanaLogin.java          ← Vista: inicio de sesión
├── VentanaFormulario.java     ← Vista: gestión de animales
├── VentanaInventario.java     ← Vista: consulta y filtros
└── VentanaEstadisticas.java   ← Vista: métricas
```
 
---
 
## Usuarios de prueba
 
| Usuario | Contraseña | Rol | Acceso |
|---------|-----------|-----|--------|
| `admin` | `1234` | Administrador | Todo |
| `vet01` | `pass1` | Veterinario | Registrar y editar |
| `aprendiz` | `abc123` | Aprendiz | Solo consulta |
 
---
 
## Cómo ejecutar el proyecto
 
**Requisitos:** Java 17 o superior y el archivo `sqlite-jdbc.jar` en la misma carpeta.
 
```bash
# Compilar
javac -cp "sqlite-jdbc.jar" *.java
 
# Ejecutar (Windows CMD)
java -cp .;sqlite-jdbc.jar Main
 
# Ejecutar (Mac / Linux)
java -cp .:sqlite-jdbc.jar Main
```
 
Al ejecutarse por primera vez, se crea automáticamente el archivo `zootec.db` con la tabla de animales.
 
---
 
## Acuerdo de trabajo del equipo
 
1. Todo cambio posterior al commit inicial se realiza en una rama, nunca directamente en `main`.
2. Los mensajes de commit siguen el formato `tipo: descripción breve`.
   - Ejemplos válidos: `feat: agrega filtro por especie`, `fix: corrige validación de peso negativo`, `docs: actualiza sección de roles en README`
   - Ejemplos inválidos: `cambio`, `listo`, `prueba`, `ajuste`
3. Ningún pull request se fusiona sin al menos una observación revisada y corregida.
4. No se publican contraseñas, tokens, documentos de identidad, teléfonos ni archivos institucionales restringidos.
---
 
## Cinco criterios de calidad para aceptar una contribución
 
Antes de fusionar cualquier pull request en `main`, la contribución debe cumplir:
 
1. **Compilación sin errores:** el código compila correctamente con `javac` sin advertencias críticas.
2. **Funcionalidad verificada:** la funcionalidad agregada o modificada fue probada manualmente antes de abrir el pull request.
3. **Mensaje de commit descriptivo:** cada commit explica qué cambió y por qué, no solo qué archivo se tocó.
4. **Sin información sensible:** ningún archivo del pull request contiene contraseñas, tokens o datos personales.
5. **Coherencia con la arquitectura:** los cambios respetan la separación de capas (Vista → Control → Datos → Modelo) definida en el diseño del sistema.
---
 
## Fuentes consultadas
 
- Documentación oficial de Java Swing: https://docs.oracle.com/javase/tutorial/uiswing/
- SQLite JDBC Driver (Xerial): https://github.com/xerial/sqlite-jdbc
- GitHub Hello World: https://docs.github.com/es/get-started/start-your-journey/hello-world
- Sitio oficial de Git: https://git-scm.com/about
- Glosario de GitHub: https://docs.github.com/es/get-started/learning-about-github/github-glossary
