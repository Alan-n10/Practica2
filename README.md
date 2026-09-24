# TareasApp — Ejercicio de la fotografía (Serie 2)

Cliente Java Swing para el backend descrito en la fotografía:
- Modelo `Tarea`: id, titulo, descripcion, estado, responsable, fecha_inicio, fecha_fin, fecha_limite.
- Endpoints: `GET /tareas`, `POST /tareas`, `PUT /tareas/:id`, `DELETE /tareas/:id`.

## Antes de usarla
Cambia `BASE_URL` en `TareaService.java` por la URL real de tu backend en Render
(la que crearás en la parte 1 del examen, o la que te den ya desplegada).

## Cómo abrirla en NetBeans
File → Open Project → carpeta `tareas-app` (proyecto Maven).

## Si el JSON no carga los datos correctamente
Revisa cómo Sequelize está serializando las fechas: si el JSON real usa
`fechaInicio` (camelCase) en vez de `fecha_inicio` (snake_case), quita las
anotaciones `@JsonProperty("fecha_inicio")` etc. de `Tarea.java` (Jackson ya
mapea el nombre tal cual).
