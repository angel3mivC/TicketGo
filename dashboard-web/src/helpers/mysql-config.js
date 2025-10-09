import mysql from "mysql2/promise";
import dotenv from "dotenv";

dotenv.config();

const pool = mysql.createPool({
    host: process.env.DBHOST,
    user: process.env.DBUSER,
    password: process.env.DBPASS,
    database: process.env.DBNAME,
    port: process.env.DBPORT,
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