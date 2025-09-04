import { Router } from "express";
import {
    totalTickets,
    openTickets,
    closedTickets,
    myTickets,
    dashboardSummary,
} from "../controllers/dashboard.controller.js";
import { verifyToken, checkRole } from "../middlewares/auth.middleware.js";

const router = Router();

router.get("/summary", verifyToken, checkRole([1, 2]), dashboardSummary);

router.get("/total", verifyToken, checkRole([1, 2]), totalTickets);
router.get("/open", verifyToken, checkRole([1, 2]), openTickets);
router.get("/closed", verifyToken, checkRole([1, 2]), closedTickets);

router.get("/my-tickets", verifyToken, checkRole([3]), myTickets);

export default router;