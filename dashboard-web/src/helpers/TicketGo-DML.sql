USE TicketGo;

INSERT INTO usuarios (nombre, correo, contraseña, id_rol, estado) VALUES
('Admin Uno', 'admin1@example.com', 'admin123', 1, 'Activo'),
('Mesa Uno', 'mesa1@example.com', 'mesa123', 2, 'Activo'),
('Tecnico Uno', 'tecnico1@example.com', 'tecnico123', 3, 'Activo');

INSERT INTO categorias (nombre) VALUES
('Hardware'),
('Software'),
('Redes');

INSERT INTO prioridades (nivel) VALUES
('Alta'),
('Media'),
('Baja');

INSERT INTO estados (nombre) VALUES
('Abierto'),
('En Progreso'),
('Cerrado');

INSERT INTO tickets (titulo, descripcion, id_categoria, id_prioridad, id_estado, creado_por, asignado_a)
VALUES
('Falla en impresora', 'La impresora no responde al enviar documentos', 1, 1, 1, 2, 3),
('Error en sistema', 'El sistema da error al iniciar sesión', 2, 2, 1, 2, NULL),
('Problema de red', 'No hay conexión en la oficina 3', 3, 1, 2, 2, 3);

INSERT INTO comentarios (id_ticket, id_usuario, comentario) VALUES
(1, 2, 'Se reporta el problema de impresión'),
(1, 3, 'Estoy revisando la impresora'),
(1, 2, 'El usuario indica error al iniciar sesión');

INSERT INTO adjuntos (id_ticket, id_usuario, nombre_archivo, tipo_archivo, url_archivo) VALUES
(1, 2, 'captura1.png', 'image/png', 'http://localhost/files/captura1.png'),
(1, 3, 'log.txt', 'text/plain', 'http://localhost/files/log.txt'),
(2, 2, 'error_sesion.png', 'image/png', 'http://localhost/files/error_sesion.png');

INSERT INTO notificaciones (id_usuario, id_ticket, mensaje, leido) VALUES
(3, 1, 'Se te ha asignado el ticket #1', FALSE),
(2, 2, 'Ticket #2 creado exitosamente', FALSE),
(3, 3, 'Se actualizó el estado del ticket #3 a En Progreso', FALSE);