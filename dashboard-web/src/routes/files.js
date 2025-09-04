import { Router } from "express";
import { getFiles, uploadFile, deleteFile } from "../controllers/files.controller.js";
import { verifyToken, checkRole } from "../middlewares/auth.middleware.js";

const router = Router({ mergeParams: true });

router.get("/", verifyToken, checkRole([1, 2, 3]), getFiles);
router.post("/", verifyToken, checkRole([2, 3]), uploadFile);
router.delete("/:id", verifyToken, checkRole([1]), deleteFile);

export default router;