import express from "express";
import dotenv from "dotenv";
import fileUpload from "express-fileupload";
import cors from "cors";

import authRoutes from "./routes/auth.js";
import userRoutes from "./routes/users.js";
import ticketRoutes from "./routes/tickets.js";
import notificationRoutes from "./routes/notifications.js";
import reportRoutes from "./routes/reports.js";
import dashboardRoutes from "./routes/dashboard.js";
import catalogRoutes from "./routes/catalogs.js";

dotenv.config();

const app = express();
const PORT = process.env.PORT || 8080;

app.use(cors());
app.use(express.json());

app.use(
    fileUpload({
        limits: { fileSize: 100 * 1024 * 1024 }, // 100MB
        abortOnLimit: true,
    })
);

app.use("/auth", authRoutes);
app.use("/users", userRoutes);
app.use("/tickets", ticketRoutes);
app.use("/notifications", notificationRoutes);
app.use("/reports", reportRoutes);
app.use("/dashboard", dashboardRoutes);
app.use("/catalogs", catalogRoutes);

app.listen(PORT, () => {
    console.log(`Servidor corriendo en http://localhost:${PORT}`);
});