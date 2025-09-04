import { Router } from "express";
import { getComments, createComment } from "../controllers/comments.controller.js";
import { verifyToken, checkRole } from "../middlewares/auth.middleware.js";

const router = Router({ mergeParams: true });

router.get("/", verifyToken, checkRole([1, 2, 3]), getComments);
router.post("/", verifyToken, checkRole([2, 3]), createComment);

export default router;