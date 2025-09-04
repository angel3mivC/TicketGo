CREATE DATABASE TicketGo;
USE TicketGo;

CREATE TABLE roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO roles (nombre) VALUES 
('Administrador'), 
('Mesa de Trabajo'), 
('Técnico');

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    contraseña VARCHAR(255) NOT NULL,
    id_rol INT NOT NULL,
    estado ENUM('Activo', 'Inactivo') DEFAULT 'Activo',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

CREATE TABLE categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE prioridades (
    id_prioridad INT AUTO_INCREMENT PRIMARY KEY,
    nivel ENUM('Baja','Media','Alta') NOT NULL
);

CREATE TABLE estados (
    id_estado INT AUTO_INCREMENT PRIMARY KEY,
    nombre ENUM('Abierto','En Progreso','Resuelto','Cerrado','Reabierto') NOT NULL
);

CREATE TABLE tickets (
    id_ticket INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    descripcion TEXT NOT NULL,
    id_categoria INT,
    id_prioridad INT,
    id_estado INT DEFAULT 1,
    creado_por INT NOT NULL,
    asignado_a INT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria),
    FOREIGN KEY (id_prioridad) REFERENCES prioridades(id_prioridad),
    FOREIGN KEY (id_estado) REFERENCES estados(id_estado),
    FOREIGN KEY (creado_por) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (asignado_a) REFERENCES usuarios(id_usuario)
);

CREATE TABLE historial_tickets (
    id_historial INT AUTO_INCREMENT PRIMARY KEY,
    id_ticket INT NOT NULL,
    id_usuario INT NOT NULL,
    accion VARCHAR(255) NOT NULL,
    detalle TEXT,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_ticket) REFERENCES tickets(id_ticket),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

CREATE TABLE comentarios (
    id_comentario INT AUTO_INCREMENT PRIMARY KEY,
    id_ticket INT NOT NULL,
    id_usuario INT NOT NULL,
    comentario TEXT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_ticket) REFERENCES tickets(id_ticket),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

CREATE TABLE notificaciones (
    id_notificacion INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_ticket INT NOT NULL,
    mensaje TEXT NOT NULL,
    leido BOOLEAN DEFAULT FALSE,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_ticket) REFERENCES tickets(id_ticket)
);

CREATE TABLE adjuntos (
    id_adjunto INT AUTO_INCREMENT PRIMARY KEY,
    id_ticket INT NOT NULL,
    id_usuario INT NOT NULL,
    nombre_archivo VARCHAR(255) NOT NULL,
    tipo_archivo VARCHAR(50) NOT NULL,
    url_archivo VARCHAR(500) NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_ticket) REFERENCES tickets(id_ticket),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

DELIMITER //
CREATE TRIGGER before_insert_usuario
BEFORE INSERT ON usuarios
FOR EACH ROW
BEGIN
    SET NEW.contraseña = SHA2(NEW.contraseña, 256);
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER before_update_usuario
BEFORE UPDATE ON usuarios
FOR EACH ROW
BEGIN
    IF NEW.contraseña <> OLD.contraseña THEN
        SET NEW.contraseña = SHA2(NEW.contraseña, 256);
    END IF;
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER after_insert_ticket
AFTER INSERT ON tickets
FOR EACH ROW
BEGIN
    INSERT INTO historial_tickets(id_ticket, id_usuario, accion, detalle)
    VALUES (NEW.id_ticket, NEW.creado_por, 'Creación de Ticket', CONCAT('Ticket creado con título: ', NEW.titulo));
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER after_update_estado_ticket
AFTER UPDATE ON tickets
FOR EACH ROW
BEGIN
    IF NEW.id_estado <> OLD.id_estado THEN
        INSERT INTO historial_tickets(id_ticket, id_usuario, accion, detalle)
        VALUES (NEW.id_ticket, NEW.asignado_a, 'Cambio de Estado', CONCAT('Estado cambiado de ', OLD.id_estado, ' a ', NEW.id_estado));

        INSERT INTO notificaciones(id_usuario, id_ticket, mensaje)
        VALUES (NEW.creado_por, NEW.id_ticket, CONCAT('El estado del ticket #', NEW.id_ticket, ' cambió a ', NEW.id_estado));
    END IF;
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER after_update_asignacion_ticket
AFTER UPDATE ON tickets
FOR EACH ROW
BEGIN
    IF NEW.asignado_a <> OLD.asignado_a THEN
        INSERT INTO historial_tickets(id_ticket, id_usuario, accion, detalle)
        VALUES (NEW.id_ticket, NEW.asignado_a, 'Reasignación', CONCAT('Ticket reasignado de usuario ', OLD.asignado_a, ' a usuario ', NEW.asignado_a));

        INSERT INTO notificaciones(id_usuario, id_ticket, mensaje)
        VALUES (NEW.asignado_a, NEW.id_ticket, CONCAT('Se te asignó el ticket #', NEW.id_ticket));
    END IF;
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER after_insert_comentario
AFTER INSERT ON comentarios
FOR EACH ROW
BEGIN
    INSERT INTO notificaciones(id_usuario, id_ticket, mensaje)
    SELECT asignado_a, NEW.id_ticket, CONCAT('Nuevo comentario en el ticket #', NEW.id_ticket, ': ', NEW.comentario)
    FROM tickets WHERE id_ticket = NEW.id_ticket;
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER after_insert_adjunto
AFTER INSERT ON adjuntos
FOR EACH ROW
BEGIN
    INSERT INTO notificaciones(id_usuario, id_ticket, mensaje)
    VALUES (NEW.id_usuario, NEW.id_ticket, CONCAT('Se adjuntó un nuevo archivo: ', NEW.nombre_archivo));
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER after_insert_ticket
AFTER INSERT ON tickets
FOR EACH ROW
BEGIN
    INSERT INTO historial_tickets(id_ticket, id_usuario, accion, detalle)
    VALUES (NEW.id_ticket, NEW.creado_por, 'Creación de Ticket', CONCAT('Ticket creado con título: ', NEW.titulo));
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER after_update_estado_ticket
AFTER UPDATE ON tickets
FOR EACH ROW
BEGIN
    IF NEW.id_estado <> OLD.id_estado THEN
        INSERT INTO historial_tickets(id_ticket, id_usuario, accion, detalle)
        VALUES (NEW.id_ticket, NEW.asignado_a, 'Cambio de Estado', CONCAT('Estado cambiado de ', OLD.id_estado, ' a ', NEW.id_estado));

        INSERT INTO notificaciones(id_usuario, id_ticket, mensaje)
        VALUES (NEW.creado_por, NEW.id_ticket, CONCAT('El estado del ticket #', NEW.id_ticket, ' cambió a ', NEW.id_estado));
    END IF;
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER after_update_asignacion_ticket
AFTER UPDATE ON tickets
FOR EACH ROW
BEGIN
    IF NEW.asignado_a <> OLD.asignado_a THEN
        INSERT INTO historial_tickets(id_ticket, id_usuario, accion, detalle)
        VALUES (NEW.id_ticket, NEW.asignado_a, 'Reasignación', CONCAT('Ticket reasignado de usuario ', OLD.asignado_a, ' a usuario ', NEW.asignado_a));

        INSERT INTO notificaciones(id_usuario, id_ticket, mensaje)
        VALUES (NEW.asignado_a, NEW.id_ticket, CONCAT('Se te asignó el ticket #', NEW.id_ticket));
    END IF;
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER after_insert_comentario
AFTER INSERT ON comentarios
FOR EACH ROW
BEGIN
    INSERT INTO notificaciones(id_usuario, id_ticket, mensaje)
    SELECT asignado_a, NEW.id_ticket, CONCAT('Nuevo comentario en el ticket #', NEW.id_ticket, ': ', NEW.comentario)
    FROM tickets WHERE id_ticket = NEW.id_ticket;
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER after_insert_adjunto
AFTER INSERT ON adjuntos
FOR EACH ROW
BEGIN
    INSERT INTO notificaciones(id_usuario, id_ticket, mensaje)
    VALUES (NEW.id_usuario, NEW.id_ticket, CONCAT('Se adjuntó un nuevo archivo: ', NEW.nombre_archivo));
END;
//
DELIMITER ;

ALTER TABLE usuarios ADD ultima_sesion TIMESTAMP NULL;

DELIMITER //
CREATE TRIGGER update_ultima_sesion
AFTER UPDATE ON usuarios
FOR EACH ROW
BEGIN
    IF NEW.estado = 'Activo' AND OLD.estado = 'Activo' THEN
        SET NEW.ultima_sesion = CURRENT_TIMESTAMP;
    END IF;
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER validar_adjunto
BEFORE INSERT ON adjuntos
FOR EACH ROW
BEGIN
    IF NOT (NEW.tipo_archivo IN ('jpg','png','pdf','docx')) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Tipo de archivo no permitido';
    END IF;
END;
//
DELIMITER ;

CREATE TABLE tickets_alertas (
    id_alerta INT AUTO_INCREMENT PRIMARY KEY,
    id_ticket INT NOT NULL,
    mensaje TEXT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_ticket) REFERENCES tickets(id_ticket)
);

DELIMITER //
CREATE TRIGGER after_update_ticket_inactividad
AFTER UPDATE ON tickets
FOR EACH ROW
BEGIN
    IF TIMESTAMPDIFF(DAY, OLD.fecha_actualizacion, NEW.fecha_actualizacion) > 7 THEN
        INSERT INTO tickets_alertas(id_ticket, mensaje)
        VALUES (NEW.id_ticket, CONCAT('El ticket #', NEW.id_ticket, ' lleva más de 7 días sin movimiento.'));
    END IF;
END;
//
DELIMITER ;