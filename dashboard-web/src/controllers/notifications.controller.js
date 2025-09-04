import pool from "../helpers/mysql-config.js";

const getNotifications = async (req, res) => {
    try {
        const id_usuario = req.user.id_usuario;

        const [rows] = await pool.query(
            `SELECT id_notificacion, id_ticket, mensaje, leido, fecha
            FROM notificaciones
            WHERE id_usuario = ?
            ORDER BY fecha DESC`,
            [id_usuario]
        );

        res.json(rows);
    } catch (error) {
        console.error("Error al obtener notificaciones:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const markAsRead = async (req, res) => {
    try {
        const { id } = req.params;
        const id_usuario = req.user.id_usuario;

        const [rows] = await pool.query(
            "SELECT * FROM notificaciones WHERE id_notificacion = ? AND id_usuario = ?",
            [id, id_usuario]
        );

        if (rows.length === 0) {
            return res.status(404).json({ message: "Notificación no encontrada" });
        }

        await pool.query("UPDATE notificaciones SET leido = TRUE WHERE id_notificacion = ?", [id]);

        res.json({ message: "Notificación marcada como leída" });
    } catch (error) {
        console.error("Error al actualizar notificación:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

export { getNotifications, markAsRead };