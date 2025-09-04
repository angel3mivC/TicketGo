import { Router } from "express";
import {
    reportByState,
    reportByPriority,
    reportByCategory,
    reportByTechnician,
    reportByDateRange,
} from "../controllers/reports.controller.js";
import { verifyToken, checkRole } from "../middlewares/auth.middleware.js";

const router = Router();

router.get("/state", verifyToken, checkRole([1]), reportByState);
router.get("/priority", verifyToken, checkRole([1]), reportByPriority);
router.get("/category", verifyToken, checkRole([1]), reportByCategory);
router.get("/technician", verifyToken, checkRole([1]), reportByTechnician);
router.get("/dates", verifyToken, checkRole([1]), reportByDateRange);

export default router;