import pool from "../helpers/mysql-config.js";

const getRoles = async (req, res) => {
    try {
        const [rows] = await pool.query(
            "SELECT id_rol, nombre FROM roles ORDER BY id_rol ASC"
        );
        res.json(rows);
    } catch (error) {
        console.error("Error al obtener roles:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const getEstados = async (req, res) => {
    try {
        const [rows] = await pool.query(
            "SELECT id_estado, nombre FROM estados ORDER BY id_estado ASC"
        );
        res.json(rows);
    } catch (error) {
        console.error("Error al obtener estados:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const getCategorias = async (req, res) => {
    try {
        const [rows] = await pool.query(
            "SELECT id_categoria, nombre FROM categorias ORDER BY id_categoria ASC"
        );
        res.json(rows);
    } catch (error) {
        console.error("Error al obtener categorías:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const getPrioridades = async (req, res) => {
    try {
        const [rows] = await pool.query(
            "SELECT id_prioridad, nivel FROM prioridades ORDER BY id_prioridad ASC"
        );
        res.json(rows);
    } catch (error) {
        console.error("Error al obtener prioridades:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

export { getRoles, getEstados, getCategorias, getPrioridades };