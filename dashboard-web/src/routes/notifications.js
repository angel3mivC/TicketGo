import { Router } from "express";
import { getNotifications, markAsRead } from "../controllers/notifications.controller.js";
import { verifyToken, checkRole } from "../middlewares/auth.middleware.js";

const router = Router();

router.get("/", verifyToken, checkRole([1, 2, 3]), getNotifications);
router.put("/:id/read", verifyToken, checkRole([1, 2, 3]), markAsRead);

export default router;