USE TicketGo;

INSERT INTO usuarios (nombre, correo, contraseña, id_rol, estado) VALUES
('Admin Uno', 'admin1@example.com', 'admin123', 1, 'Activo'),
('Mesa Uno', 'mesa1@example.com', 'mesa123', 2, 'Activo'),
('Tecnico Uno', 'tecnico1@example.com', 'tecnico123', 3, 'Activo');

INSERT INTO usuarios (nombre, correo, contraseña, id_rol, estado) VALUES
('Admin Dos', 'admin2@example.com', 'admin123', 1, 'Activo'),
('Mesa Dos', 'mesa2@example.com', 'mesa123', 2, 'Activo'),
('Tecnico Dos', 'tecnico2@example.com', 'tecnico123', 3, 'Activo');

INSERT INTO categorias (nombre) VALUES ('En proceso'), ('Garantia'), ('Daño inducido');
INSERT INTO prioridades (nivel) VALUES ('Alta'), ('Media'), ('Baja');
INSERT INTO estados (nombre) VALUES ('Abierto'), ('En Progreso'), ('Cerrado');

INSERT INTO tickets (titulo, descripcion, id_categoria, id_prioridad, id_estado, creado_por, asignado_a) VALUES
('Falla en impresora', 'La impresora no responde al enviar documentos', 1, 1, 1, 2, 3),
('Error en sistema', 'El sistema da error al iniciar sesión', 2, 2, 1, 2, NULL),
('Problema de red', 'No hay conexión en la oficina 3', 3, 1, 2, 2, 3);

INSERT INTO tickets (titulo, descripcion, id_categoria, id_prioridad, id_estado, creado_por, asignado_a) VALUES
('Falla en Outlook', 'El correo no sincroniza los mensajes nuevos', 3, 2, 1, 4, 6),
('Error en intranet', 'La página interna de reportes muestra error 500', 2, 1, 2, 5, 6),
('Conexión Wi-Fi intermitente', 'La red Wi-Fi se desconecta cada 5 minutos', 1, 3, 1, 5, 3);

INSERT INTO comentarios (id_ticket, id_usuario, comentario) VALUES
(1, 1, 'Se reporta el problema de impresión'),
(1, 2, 'Estoy revisando la impresora');

INSERT INTO comentarios (id_ticket, id_usuario, comentario) VALUES
(4, 4, 'Se ha detectado un problema en la configuración del cliente de correo.'),
(5, 6, 'Investigando logs del servidor de intranet.'),
(6, 3, 'Se recomienda reiniciar el router y verificar el canal Wi-Fi.');

INSERT INTO notificaciones (id_usuario, id_ticket, mensaje, leido) VALUES
(3, 1, 'Se te ha asignado el ticket #1', FALSE),
(2, 2, 'Ticket #2 creado exitosamente', FALSE),
(3, 3, 'Se actualizó el estado del ticket #3 a En Progreso', FALSE);

INSERT INTO notificaciones (id_usuario, id_ticket, mensaje, leido) VALUES
(6, 4, 'Ticket #4 asignado para revisión de correo electrónico.', FALSE),
(5, 5, 'Se ha creado el ticket #5 sobre error en intranet.', FALSE),
(3, 6, 'Nuevo ticket #6 sobre conexión Wi-Fi intermitente.', FALSE);

INSERT INTO historial_tickets (id_ticket, id_usuario, accion, detalle) VALUES
(4, 4, 'Creación de Ticket', 'Ticket sobre Outlook creado por Mesa Dos.'),
(5, 5, 'Creación de Ticket', 'Ticket de intranet generado por Mesa Dos.'),
(6, 5, 'Creación de Ticket', 'Reporte sobre conexión Wi-Fi intermitente.');

SELECT * FROM usuarios;
SELECT * FROM tickets;
SELECT * FROM comentarios;
SELECT * FROM adjuntos;