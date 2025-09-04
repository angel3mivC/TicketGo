import pool from "../helpers/mysql-config.js";

const getUsers = async (req, res) => {
    try {
        const [rows] = await pool.query(
            "SELECT id_usuario, nombre, correo, id_rol, estado, fecha_creacion FROM usuarios"
        );
        res.json(rows);
    } catch (error) {
        console.error("Error al obtener usuarios:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};


const getUserById = async (req, res) => {
    try {
        const { id } = req.params;
        const [rows] = await pool.query(
            "SELECT id_usuario, nombre, correo, id_rol, estado, fecha_creacion FROM usuarios WHERE id_usuario = ?",
            [id]
        );

        if (rows.length === 0) {
            return res.status(404).json({ message: "Usuario no encontrado" });
        }

        res.json(rows[0]);
    } catch (error) {
        console.error("Error al obtener usuario:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const createUser = async (req, res) => {
    try {
        const { nombre, correo, contraseña, id_rol } = req.body;

        if (!nombre || !correo || !contraseña || !id_rol) {
            return res.status(400).json({ message: "Todos los campos son requeridos" });
        }

        const [exists] = await pool.query("SELECT id_usuario FROM usuarios WHERE correo = ?", [correo]);
        if (exists.length > 0) {
            return res.status(409).json({ message: "El correo ya está registrado" });
        }

        const [result] = await pool.query(
            "INSERT INTO usuarios (nombre, correo, contraseña, id_rol) VALUES (?, ?, ?, ?)",
            [nombre, correo, contraseña, id_rol]
        );

        res.status(201).json({
            message: "Usuario creado correctamente",
            user: {
                id: result.insertId,
                nombre,
                correo,
                id_rol,
            },
        });
    } catch (error) {
        console.error("Error al crear usuario:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const updateUser = async (req, res) => {
    try {
        const { id } = req.params;
        const { nombre, correo, contraseña, id_rol, estado } = req.body;

        const [rows] = await pool.query("SELECT * FROM usuarios WHERE id_usuario = ?", [id]);
        if (rows.length === 0) {
            return res.status(404).json({ message: "Usuario no encontrado" });
        }

        await pool.query(
            `UPDATE usuarios SET 
        nombre = COALESCE(?, nombre),
        correo = COALESCE(?, correo),
        contraseña = COALESCE(?, contraseña),
        id_rol = COALESCE(?, id_rol),
        estado = COALESCE(?, estado)
        WHERE id_usuario = ?`,
            [nombre, correo, contraseña, id_rol, estado, id]
        );

        res.json({ message: "Usuario actualizado correctamente" });
    } catch (error) {
        console.error("Error al actualizar usuario:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

const deleteUser = async (req, res) => {
    try {
        const { id } = req.params;

        const [rows] = await pool.query("SELECT * FROM usuarios WHERE id_usuario = ?", [id]);
        if (rows.length === 0) {
            return res.status(404).json({ message: "Usuario no encontrado" });
        }

        await pool.query("UPDATE usuarios SET estado = 'Inactivo' WHERE id_usuario = ?", [id]);

        res.json({ message: "Usuario eliminado correctamente (Inactivo)" });
    } catch (error) {
        console.error("Error al eliminar usuario:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

export { getUsers, getUserById, createUser, updateUser, deleteUser };