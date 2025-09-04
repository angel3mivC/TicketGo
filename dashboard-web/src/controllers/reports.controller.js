import pool from "../helpers/mysql-config.js";

const reportByState = async (req, res) => {
    try {
        const [rows] = await pool.query(
            `SELECT e.nombre AS estado, COUNT(*) AS total
            FROM tickets t
            INNER JOIN estados e ON t.id_estado = e.id_estado
            GROUP BY e.nombre`
        );
        res.json(rows);
    } catch (error) {
        console.error("Error en reporte por estado:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const reportByPriority = async (req, res) => {
    try {
        const [rows] = await pool.query(
            `SELECT p.nivel AS prioridad, COUNT(*) AS total
            FROM tickets t
            INNER JOIN prioridades p ON t.id_prioridad = p.id_prioridad
            GROUP BY p.nivel`
        );
        res.json(rows);
    } catch (error) {
        console.error("Error en reporte por prioridad:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const reportByCategory = async (req, res) => {
    try {
        const [rows] = await pool.query(
            `SELECT c.nombre AS categoria, COUNT(*) AS total
            FROM tickets t
            INNER JOIN categorias c ON t.id_categoria = c.id_categoria
            GROUP BY c.nombre`
        );
        res.json(rows);
    } catch (error) {
        console.error("Error en reporte por categoría:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const reportByTechnician = async (req, res) => {
    try {
        const [rows] = await pool.query(
            `SELECT u.nombre AS tecnico, COUNT(*) AS total
            FROM tickets t
            INNER JOIN usuarios u ON t.asignado_a = u.id_usuario
            WHERE u.id_rol = 3
            GROUP BY u.nombre`
        );
        res.json(rows);
    } catch (error) {
        console.error("Error en reporte por técnico:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const reportByDateRange = async (req, res) => {
    try {
        const { inicio, fin } = req.query;

        if (!inicio || !fin) {
            return res.status(400).json({ message: "Debe proporcionar fecha inicio y fin (YYYY-MM-DD)" });
        }

        const [rows] = await pool.query(
            `SELECT DATE(fecha_creacion) AS fecha, COUNT(*) AS total
            FROM tickets
            WHERE DATE(fecha_creacion) BETWEEN ? AND ?
            GROUP BY DATE(fecha_creacion)
            ORDER BY fecha ASC`,
            [inicio, fin]
        );

        res.json(rows);
    } catch (error) {
        console.error("Error en reporte por rango de fechas:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

export {
    reportByState,
    reportByPriority,
    reportByCategory,
    reportByTechnician,
    reportByDateRange
};