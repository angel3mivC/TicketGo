import { Router } from "express";
import {
    getRoles,
    getEstados,
    getCategorias,
    getPrioridades,
} from "../controllers/catalogs.controller.js";
import { verifyToken, checkRole } from "../middlewares/auth.middleware.js";

const router = Router();

router.get("/roles", verifyToken, checkRole([1, 2, 3]), getRoles);
router.get("/estados", verifyToken, checkRole([1, 2, 3]), getEstados);
router.get("/categorias", verifyToken, checkRole([1, 2, 3]), getCategorias);
router.get("/prioridades", verifyToken, checkRole([1, 2, 3]), getPrioridades);

export default router;