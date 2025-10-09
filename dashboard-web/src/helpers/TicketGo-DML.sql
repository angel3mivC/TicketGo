USE TicketGo;

INSERT INTO usuarios (nombre, correo, contraseña, id_rol, estado) VALUES
('Admin Uno', 'admin1@example.com', 'admin123', 1, 'Activo'),
('Mesa Uno', 'mesa1@example.com', 'mesa123', 2, 'Activo'),
('Tecnico Uno', 'tecnico1@example.com', 'tecnico123', 3, 'Activo');

INSERT INTO categorias (nombre) VALUES ('Hardware'), ('Software'), ('Redes');
INSERT INTO prioridades (nivel) VALUES ('Alta'), ('Media'), ('Baja');
INSERT INTO estados (nombre) VALUES ('Abierto'), ('En Progreso'), ('Cerrado');

INSERT INTO tickets (titulo, descripcion, id_categoria, id_prioridad, id_estado, creado_por, asignado_a) VALUES
('Falla en impresora', 'La impresora no responde al enviar documentos', 1, 1, 1, 2, 3),
('Error en sistema', 'El sistema da error al iniciar sesión', 2, 2, 1, 2, NULL),
('Problema de red', 'No hay conexión en la oficina 3', 3, 1, 2, 2, 3);

INSERT INTO comentarios (id_ticket, id_usuario, comentario) VALUES
(1, 1, 'Se reporta el problema de impresión'),
(1, 2, 'Estoy revisando la impresora');

INSERT INTO adjuntos (id_ticket, id_usuario, nombre_archivo, tipo_archivo, archivo, nombre_original, tipo_mime) VALUES
(1, 2, 'Captura de error', 'image', NULL, 'captura1.png', 'image/png'),
(1, 3, 'Registro de logs', 'text', NULL, 'log.txt', 'text/plain'),
(2, 2, 'Error en inicio de sesión', 'image', NULL, 'error_sesion.png', 'image/png');

INSERT INTO notificaciones (id_usuario, id_ticket, mensaje, leido) VALUES
(3, 1, 'Se te ha asignado el ticket #1', FALSE),
(2, 2, 'Ticket #2 creado exitosamente', FALSE),
(3, 3, 'Se actualizó el estado del ticket #3 a En Progreso', FALSE);

SELECT * FROM usuarios;
SELECT * FROM tickets;
SELECT * FROM comentarios;
SELECT * FROM adjuntos;