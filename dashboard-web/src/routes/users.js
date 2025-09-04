import { Router } from "express";
import {
    getUsers,
    getUserById,
    createUser,
    updateUser,
    deleteUser,
} from "../controllers/users.controller.js";
import { verifyToken, checkRole } from "../middlewares/auth.middleware.js";

const router = Router();

router.get("/", verifyToken, checkRole([1]), getUsers);
router.get("/:id", verifyToken, checkRole([1]), getUserById);
router.post("/", verifyToken, checkRole([1]), createUser);
router.put("/:id", verifyToken, checkRole([1]), updateUser);
router.delete("/:id", verifyToken, checkRole([1]), deleteUser);

export default router;