import jwt from "jsonwebtoken";
import pool from "../helpers/mysql-config.js";
import dotenv from "dotenv";

dotenv.config();

const JWT_SECRET = process.env.JWT_SECRET || "supersecret";

const login = async (req, res) => {
    try {
        const { correo, contraseña } = req.body;
        if (!correo || !contraseña) {
            return res.status(400).json({ message: "Correo y contraseña son requeridos" });
        }

        const [rows] = await pool.query(
            "SELECT id_usuario, nombre, correo, contraseña, id_rol, estado FROM usuarios WHERE correo = ? AND estado = 'Activo'",
            [correo]
        );

        if (rows.length === 0) {
            return res.status(401).json({ message: "Usuario no encontrado o inactivo" });
        }

        const user = rows[0];
        const [passCheck] = await pool.query(
            "SELECT id_usuario FROM usuarios WHERE id_usuario = ? AND contraseña = SHA2(?, 256)",
            [user.id_usuario, contraseña]
        );

        if (passCheck.length === 0) {
            return res.status(401).json({ message: "Contraseña incorrecta" });
        }

        const token = jwt.sign(
            {
                id_usuario: user.id_usuario,
                nombre: user.nombre,
                correo: user.correo,
                id_rol: user.id_rol,
            },
            JWT_SECRET,
            { expiresIn: "8h" }
        );

        return res.json({
            message: "Login exitoso",
            token,
            user: {
                id: user.id_usuario,
                nombre: user.nombre,
                correo: user.correo,
                rol: user.id_rol,
            },
        });
    } catch (error) {
        console.error("Error en login:", error);
        res.status(500).json({ message: "Error interno en el servidor" });
    }
};

const logout = (req, res) => {
    return res.json({ message: "Sesión cerrada. Elimina el token en el cliente." });
};

export { login, logout };
