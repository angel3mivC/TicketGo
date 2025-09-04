import pool from "../helpers/mysql-config.js";

const getTickets = async (req, res) => {
    try {
        const { estado, prioridad, tecnico, categoria, fecha_inicio, fecha_fin } = req.query;

        let query = `
        SELECT t.id_ticket, t.titulo, t.descripcion, t.fecha_creacion, t.fecha_actualizacion,
        u1.nombre AS creado_por, u2.nombre AS asignado_a,
        c.nombre AS categoria, p.nivel AS prioridad, e.nombre AS estado
        FROM tickets t
        LEFT JOIN usuarios u1 ON t.creado_por = u1.id_usuario
        LEFT JOIN usuarios u2 ON t.asignado_a = u2.id_usuario
        LEFT JOIN categorias c ON t.id_categoria = c.id_categoria
        LEFT JOIN prioridades p ON t.id_prioridad = p.id_prioridad
        LEFT JOIN estados e ON t.id_estado = e.id_estado
        WHERE 1=1
    `;

        const params = [];

        if (estado) {
            query += " AND e.nombre = ?";
            params.push(estado);
        }
        if (prioridad) {
            query += " AND p.nivel = ?";
            params.push(prioridad);
        }
        if (tecnico) {
            query += " AND t.asignado_a = ?";
            params.push(tecnico);
        }
        if (categoria) {
            query += " AND c.nombre = ?";
            params.push(categoria);
        }
        if (fecha_inicio && fecha_fin) {
            query += " AND DATE(t.fecha_creacion) BETWEEN ? AND ?";
            params.push(fecha_inicio, fecha_fin);
        }

        const [rows] = await pool.query(query, params);
        res.json(rows);
    } catch (error) {
        console.error("Error al obtener tickets:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const getTicketById = async (req, res) => {
    try {
        const { id } = req.params;

        const [rows] = await pool.query(
            `
        SELECT t.id_ticket, t.titulo, t.descripcion, t.fecha_creacion, t.fecha_actualizacion,
        u1.nombre AS creado_por, u2.nombre AS asignado_a,
        c.nombre AS categoria, p.nivel AS prioridad, e.nombre AS estado
        FROM tickets t
        LEFT JOIN usuarios u1 ON t.creado_por = u1.id_usuario
        LEFT JOIN usuarios u2 ON t.asignado_a = u2.id_usuario
        LEFT JOIN categorias c ON t.id_categoria = c.id_categoria
        LEFT JOIN prioridades p ON t.id_prioridad = p.id_prioridad
        LEFT JOIN estados e ON t.id_estado = e.id_estado
        WHERE t.id_ticket = ?
        `,
            [id]
        );

        if (rows.length === 0) {
            return res.status(404).json({ message: "Ticket no encontrado" });
        }

        res.json(rows[0]);
    } catch (error) {
        console.error("Error al obtener ticket:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const createTicket = async (req, res) => {
    try {
        const { titulo, descripcion, id_categoria, id_prioridad } = req.body;
        const creado_por = req.user.id_usuario;

        if (!titulo || !descripcion) {
            return res.status(400).json({ message: "Título y descripción son requeridos" });
        }

        const [result] = await pool.query(
            "INSERT INTO tickets (titulo, descripcion, id_categoria, id_prioridad, creado_por) VALUES (?, ?, ?, ?, ?)",
            [titulo, descripcion, id_categoria || null, id_prioridad || null, creado_por]
        );

        res.status(201).json({
            message: "Ticket creado correctamente",
            ticket_id: result.insertId,
        });
    } catch (error) {
        console.error("Error al crear ticket:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const updateTicket = async (req, res) => {
    try {
        const { id } = req.params;
        const { titulo, descripcion, id_categoria, id_prioridad } = req.body;

        const [rows] = await pool.query("SELECT * FROM tickets WHERE id_ticket = ?", [id]);
        if (rows.length === 0) {
            return res.status(404).json({ message: "Ticket no encontrado" });
        }

        await pool.query(
            `UPDATE tickets SET 
        titulo = COALESCE(?, titulo),
        descripcion = COALESCE(?, descripcion),
        id_categoria = COALESCE(?, id_categoria),
        id_prioridad = COALESCE(?, id_prioridad)
        WHERE id_ticket = ?`,
            [titulo, descripcion, id_categoria, id_prioridad, id]
        );

        res.json({ message: "Ticket actualizado correctamente" });
    } catch (error) {
        console.error("Error al actualizar ticket:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const deleteTicket = async (req, res) => {
    try {
        const { id } = req.params;

        const [rows] = await pool.query("SELECT * FROM tickets WHERE id_ticket = ?", [id]);
        if (rows.length === 0) {
            return res.status(404).json({ message: "Ticket no encontrado" });
        }

        await pool.query("DELETE FROM tickets WHERE id_ticket = ?", [id]);

        res.json({ message: "Ticket eliminado correctamente" });
    } catch (error) {
        console.error("Error al eliminar ticket:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const assignTicket = async (req, res) => {
    try {
        const { id } = req.params;
        const { tecnico_id } = req.body;

        if (!tecnico_id) {
            return res.status(400).json({ message: "ID de técnico requerido" });
        }

        const [rows] = await pool.query("SELECT * FROM tickets WHERE id_ticket = ?", [id]);
        if (rows.length === 0) {
            return res.status(404).json({ message: "Ticket no encontrado" });
        }

        await pool.query("UPDATE tickets SET asignado_a = ? WHERE id_ticket = ?", [tecnico_id, id]);

        res.json({ message: "Ticket asignado correctamente" });
    } catch (error) {
        console.error("Error al asignar ticket:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const changeState = async (req, res) => {
    try {
        const { id } = req.params;
        const { estado_id } = req.body;

        if (!estado_id) {
            return res.status(400).json({ message: "ID de estado requerido" });
        }

        const [rows] = await pool.query("SELECT * FROM tickets WHERE id_ticket = ?", [id]);
        if (rows.length === 0) {
            return res.status(404).json({ message: "Ticket no encontrado" });
        }

        await pool.query("UPDATE tickets SET id_estado = ? WHERE id_ticket = ?", [estado_id, id]);

        res.json({ message: "Estado del ticket actualizado" });
    } catch (error) {
        console.error("Error al cambiar estado:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const changePriority = async (req, res) => {
    try {
        const { id } = req.params;
        const { prioridad_id } = req.body;

        if (!prioridad_id) {
            return res.status(400).json({ message: "ID de prioridad requerido" });
        }

        await pool.query("UPDATE tickets SET id_prioridad = ? WHERE id_ticket = ?", [prioridad_id, id]);

        res.json({ message: "Prioridad actualizada" });
    } catch (error) {
        console.error("Error al cambiar prioridad:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const changeCategory = async (req, res) => {
    try {
        const { id } = req.params;
        const { categoria_id } = req.body;

        if (!categoria_id) {
            return res.status(400).json({ message: "ID de categoría requerido" });
        }

        await pool.query("UPDATE tickets SET id_categoria = ? WHERE id_ticket = ?", [categoria_id, id]);

        res.json({ message: "Categoría actualizada" });
    } catch (error) {
        console.error("Error al cambiar categoría:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

export {
    getTickets,
    getTicketById,
    createTicket,
    updateTicket,
    deleteTicket,
    assignTicket,
    changeState,
    changePriority,
    changeCategory
};