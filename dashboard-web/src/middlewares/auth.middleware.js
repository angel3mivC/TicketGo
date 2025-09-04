import jwt from "jsonwebtoken";
import dotenv from "dotenv";

dotenv.config();

const JWT_SECRET = process.env.JWT_SECRET || "supersecret";

const verifyToken = (req, res, next) => {
    const authHeader = req.headers["authorization"];

    if (!authHeader) {
        return res.status(401).json({ message: "Acceso denegado. No se envió token." });
    }

    const token = authHeader.split(" ")[1];

    if (!token) {
        return res.status(401).json({ message: "Token inválido." });
    }

    try {
        const decoded = jwt.verify(token, JWT_SECRET);
        req.user = decoded; // adjuntamos info del usuario al request
        next();
    } catch (error) {
        return res.status(403).json({ message: "Token no válido o expirado." });
    }
};

const checkRole = (rolesPermitidos = []) => {
    return (req, res, next) => {
        if (!req.user) {
            return res.status(401).json({ message: "Usuario no autenticado." });
        }
        if (!rolesPermitidos.includes(req.user.id_rol)) {
            return res.status(403).json({ message: "No tienes permisos para acceder a este recurso." });
        }
        next();
    };
};

export { verifyToken, checkRole };