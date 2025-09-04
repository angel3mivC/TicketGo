import mysql from "mysql2/promise";
import dotenv from "dotenv";

dotenv.config();

const pool = mysql.createPool({
    host: process.env.DBHOST || "localhost",
    user: process.env.DBUSER || "root",
    password: process.env.DBPASSWORD || "root",
    database: process.env.DBNAME || "TicketGo",
    port: process.env.DBPORT || 3306,
    connectionLimit: 10,
});

export default pool;


try {
    const connection = await pool.getConnection();
    console.log('Database connected successfully');
    connection.release();
} catch (err) {
    console.error('Database connection failed:', err.message);
}

export { pool };