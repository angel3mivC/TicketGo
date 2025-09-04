import { Router } from "express";
import {
    getTickets,
    getTicketById,
    createTicket,
    updateTicket,
    deleteTicket,
    assignTicket,
    changeState,
    changePriority,
    changeCategory,
} from "../controllers/tickets.controller.js";
import { verifyToken, checkRole } from "../middlewares/auth.middleware.js";

import commentRoutes from "./comments.routes.js";
import fileRoutes from "./files.routes.js";

const router = Router();

router.get("/", verifyToken, checkRole([1, 2]), getTickets);
router.get("/:id", verifyToken, checkRole([1, 2, 3]), getTicketById);
router.post("/", verifyToken, checkRole([2]), createTicket);
router.put("/:id", verifyToken, checkRole([1, 2]), updateTicket);
router.delete("/:id", verifyToken, checkRole([1]), deleteTicket);
router.put("/:id/assign", verifyToken, checkRole([1, 2]), assignTicket);
router.put("/:id/state", verifyToken, checkRole([1, 2, 3]), changeState);
router.put("/:id/priority", verifyToken, checkRole([1]), changePriority);
router.put("/:id/category", verifyToken, checkRole([1]), changeCategory);

router.use("/:id/comments", commentRoutes);
router.use("/:id/files", fileRoutes);

export default router;