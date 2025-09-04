import pool from "../helpers/mysql-config.js";

const getFiles = async (req, res) => {
    try {
        const { id } = req.params;

        const [rows] = await pool.query(
            `SELECT a.id_adjunto, a.nombre_archivo, a.tipo_archivo, a.url_archivo, a.fecha, u.nombre AS subido_por
            FROM adjuntos a
            INNER JOIN usuarios u ON a.id_usuario = u.id_usuario
            WHERE a.id_ticket = ?
            ORDER BY a.fecha ASC`,
            [id]
        );

        res.json(rows);
    } catch (error) {
        console.error("Error al obtener archivos:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const uploadFile = async (req, res) => {
    try {
        const { id } = req.params; // id del ticket
        const { nombre_archivo, tipo_archivo, url_archivo } = req.body;
        const id_usuario = req.user.id_usuario;

        if (!nombre_archivo || !tipo_archivo || !url_archivo) {
            return res.status(400).json({ message: "Datos de archivo incompletos" });
        }

        const [ticket] = await pool.query("SELECT id_ticket FROM tickets WHERE id_ticket = ?", [id]);
        if (ticket.length === 0) {
            return res.status(404).json({ message: "Ticket no encontrado" });
        }

        const [result] = await pool.query(
            "INSERT INTO adjuntos (id_ticket, id_usuario, nombre_archivo, tipo_archivo, url_archivo) VALUES (?, ?, ?, ?, ?)",
            [id, id_usuario, nombre_archivo, tipo_archivo, url_archivo]
        );

        res.status(201).json({
            message: "Archivo agregado correctamente",
            id_adjunto: result.insertId,
        });
    } catch (error) {
        console.error("Error al subir archivo:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const deleteFile = async (req, res) => {
    try {
        const { id } = req.params;

        const [rows] = await pool.query("SELECT * FROM adjuntos WHERE id_adjunto = ?", [id]);
        if (rows.length === 0) {
            return res.status(404).json({ message: "Archivo no encontrado" });
        }

        await pool.query("DELETE FROM adjuntos WHERE id_adjunto = ?", [id]);

        res.json({ message: "Archivo eliminado correctamente" });
    } catch (error) {
        console.error("Error al eliminar archivo:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

export { getFiles, uploadFile, deleteFile };