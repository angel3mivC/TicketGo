import pool from "../helpers/mysql-config.js";

const getComments = async (req, res) => {
    try {
        const { id } = req.params;

        const [rows] = await pool.query(
            `SELECT c.id_comentario, c.comentario, c.fecha, u.nombre AS autor
        FROM comentarios c
        INNER JOIN usuarios u ON c.id_usuario = u.id_usuario
        WHERE c.id_ticket = ?
        ORDER BY c.fecha ASC`,
            [id]
        );

        res.json(rows);
    } catch (error) {
        console.error("Error al obtener comentarios:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const createComment = async (req, res) => {
    try {
        const { id } = req.params;
        const { comentario } = req.body;
        const id_usuario = req.user.id_usuario;

        if (!comentario) {
            return res.status(400).json({ message: "Comentario requerido" });
        }

        const [ticket] = await pool.query("SELECT id_ticket FROM tickets WHERE id_ticket = ?", [id]);
        if (ticket.length === 0) {
            return res.status(404).json({ message: "Ticket no encontrado" });
        }

        const [result] = await pool.query(
            "INSERT INTO comentarios (id_ticket, id_usuario, comentario) VALUES (?, ?, ?)",
            [id, id_usuario, comentario]
        );

        res.status(201).json({
            message: "Comentario agregado correctamente",
            id_comentario: result.insertId,
        });
    } catch (error) {
        console.error("Error al crear comentario:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

export { getComments, createComment };