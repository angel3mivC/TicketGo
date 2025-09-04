import pool from "../helpers/mysql-config.js";

const totalTickets = async (req, res) => {
    try {
        const [rows] = await pool.query("SELECT COUNT(*) AS total FROM tickets");
        res.json(rows[0]);
    } catch (error) {
        console.error("Error en totalTickets:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const openTickets = async (req, res) => {
    try {
        const [rows] = await pool.query(
            `SELECT COUNT(*) AS abiertos
            FROM tickets t
            INNER JOIN estados e ON t.id_estado = e.id_estado
            WHERE e.nombre != 'Cerrado'`
        );
        res.json(rows[0]);
    } catch (error) {
        console.error("Error en openTickets:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const closedTickets = async (req, res) => {
    try {
        const [rows] = await pool.query(
            `SELECT COUNT(*) AS cerrados
            FROM tickets t
            INNER JOIN estados e ON t.id_estado = e.id_estado
            WHERE e.nombre = 'Cerrado'`
        );
        res.json(rows[0]);
    } catch (error) {
        console.error("Error en closedTickets:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const myTickets = async (req, res) => {
    try {
        const id_usuario = req.user.id_usuario;

        const [rows] = await pool.query(
            `SELECT COUNT(*) AS mis_tickets
            FROM tickets
            WHERE asignado_a = ?`,
            [id_usuario]
        );

        res.json(rows[0]);
    } catch (error) {
        console.error("Error en myTickets:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const dashboardSummary = async (req, res) => {
    try {
        const [[{ total }]] = await pool.query("SELECT COUNT(*) AS total FROM tickets");
        const [[{ abiertos }]] = await pool.query(
            `SELECT COUNT(*) AS abiertos
            FROM tickets t
            INNER JOIN estados e ON t.id_estado = e.id_estado
            WHERE e.nombre != 'Cerrado'`
        );
        const [[{ cerrados }]] = await pool.query(
            `SELECT COUNT(*) AS cerrados
            FROM tickets t
            INNER JOIN estados e ON t.id_estado = e.id_estado
            WHERE e.nombre = 'Cerrado'`
        );

        res.json({
            total,
            abiertos,
            cerrados,
        });
    } catch (error) {
        console.error("Error en dashboardSummary:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

export {
    totalTickets,
    openTickets,
    closedTickets,
    myTickets,
    dashboardSummary
};