import pool from "../helpers/mysql-config.js";
import fs from "fs";

export const getFiles = async (req, res) => {
    try {
        const { id } = req.params;

        const [rows] = await pool.query(
            `SELECT id_adjunto, nombre_archivo, tipo_archivo, nombre_original, tipo_mime, fecha
            FROM adjuntos
            WHERE id_ticket = ?
            ORDER BY fecha ASC`,
            [id]
        );

        res.json(rows);
    } catch (error) {
        console.error("Error al obtener archivos:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

export const downloadFile = async (req, res) => {
    try {
        const { id } = req.params;

        const [rows] = await pool.query(
            "SELECT nombre_original, tipo_mime, archivo FROM adjuntos WHERE id_adjunto = ?",
            [id]
        );

        if (rows.length === 0) {
            return res.status(404).json({ message: "Archivo no encontrado" });
        }

        const file = rows[0];
        res.setHeader("Content-Type", file.tipo_mime);
        res.setHeader("Content-Disposition", `attachment; filename="${file.nombre_original}"`);
        res.send(file.archivo);
    } catch (error) {
        console.error("Error al descargar archivo:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

export const uploadFile = async (req, res) => {
    try {
        const { id } = req.params;
        const id_usuario = req.user.id_usuario;

        if (!req.files || !req.files.file) {
            return res.status(400).json({ message: "No se envió ningún archivo" });
        }

        const uploadedFile = req.files.file;
        const buffer = uploadedFile.data;
        const tipo_mime = uploadedFile.mimetype;
        const nombre_original = uploadedFile.name;
        const nombre_archivo = req.body.nombre_archivo || nombre_original;
        const tipo_archivo = tipo_mime.split("/")[0];

        const [ticket] = await pool.query("SELECT id_ticket FROM tickets WHERE id_ticket = ?", [id]);
        if (ticket.length === 0) {
            return res.status(404).json({ message: "Ticket no encontrado" });
        }

        const [result] = await pool.query(
            `INSERT INTO adjuntos (id_ticket, id_usuario, nombre_archivo, tipo_archivo, archivo, nombre_original, tipo_mime)
            VALUES (?, ?, ?, ?, ?, ?, ?)`,
            [id, id_usuario, nombre_archivo, tipo_archivo, buffer, nombre_original, tipo_mime]
        );

        res.status(201).json({
            message: "Archivo guardado correctamente en la base de datos",
            id_adjunto: result.insertId,
        });
    } catch (error) {
        console.error("Error al subir archivo:", error);
        res.status(500).json({ message: "Error interno del servidor" });
    }
};

export const deleteFile = async (req, res) => {
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