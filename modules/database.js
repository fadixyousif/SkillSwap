// import required modules
import mysql from 'mysql2/promise';
import dotenv from 'dotenv';
dotenv.config();

// MariaDB/MySQL connection configuration
const pool = mysql.createPool({
  host: process.env.DB_HOST || 'localhost',
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_DATABASE,
  port: parseInt(process.env.DB_PORT, 10) || 3306,
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
});

// Test connection
pool.getConnection()
  .then(connection => {
    console.log('Connected to MariaDB successfully');
    connection.release();
  })
  .catch(err => {
    console.error('Database connection error:', err);
  });

export default pool;