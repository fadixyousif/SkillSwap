import express from 'express';
import pool from '../modules/database.js';
import { hashPassword, generateSalt, generateToken, verifyToken } from '../modules/authentication.js';

const router = express.Router();


// POST /auth/signup - Register a new user
router.post('/signup', async (req, res) => {
  // Validate input
  const { email, password, fullName } = req.body;

  // Basic validation
  if (!email || !password || !fullName) {
      return res.status(400).json({ error: "Missing required fields", success: false });
  }

  // Additional validation can be added here (e.g., email format, password strength)
  try {
    // Hash password with salt
    const salt = generateSalt();
    // Hash the password using PBKDF2 with the generated salt
    const hashedPassword = hashPassword(password, salt);
    

    // Store salt and hash together in the format: salt.hash
    const storedPassword = `${salt}.${hashedPassword}`;

    // Insert new user into database
    const [result] = await pool.execute(
        'INSERT INTO users (email, passwordHash, fullName) VALUES (?, ?, ?)',
        [email, storedPassword, fullName]
    );

    // get the inserted userId and generate JWT token for the new user
    const userId = result.insertId;
    const token = generateToken({ userId, email });

    // Respond with success message, userId, and token
    res.status(201).json({ 
        success: true, 
        message: 'User created successfully', 
        userId, 
        token 
    });
  // Handle database errors (e.g., duplicate email)
  } catch (err) {
    // Check for duplicate entry error code (MySQL specific)
    if (err.code === 'ER_DUP_ENTRY') {
        return res.status(409).json({ error: "Email already exists", success: false });
    }
    // For other database errors, return a generic error message
    res.status(500).json({ error: "Database error", success: false });
    console.error("Database error during signup:", err);
  }
});

// POST /auth/login - Authenticate user and return JWT token
router.post('/login', async (req, res) => {
  // Validate input
  const { email, password } = req.body;

  if (!email || !password) {
      return res.status(400).json({ error: "Missing email or password", success: false });
  }

  // Basic validation
  if (!email || !password) {
      return res.status(400).json({ error: "Missing email or password", success: false });
  }

  // Fetch user from database by email
  try {
    // Query the database for a user with the provided email
    const [rows] = await pool.execute('SELECT * FROM users WHERE email = ?', [email]);
    const user = rows[0];

    // If user is not found, return an error response
    if (!user) {
        return res.status(401).json({ error: "Invalid credentials", success: false });
    }

    // Extract salt and hash from stored password
    const [salt, savedHash] = user.passwordHash.split('.');
    const loginHash = hashPassword(password, salt);

    // Compare the hash of the provided password with the stored hash
    if (loginHash !== savedHash) {
        return res.status(401).json({ error: "Invalid credentials", success: false });
    }

    // Generate JWT token for authenticated user
    const token = generateToken(user);

    // Respond with success message, token, and user info (excluding password)
    res.status(200).json({ 
        success: true, 
        token, 
        user: { 
          userId: user.userId, 
          email: user.email, 
          fullName: user.fullName 
        } 
    });

  // Handle database errors
  } catch (err) {
    // For database errors, return a generic error message
    res.status(500).json({ error: "Database error", success: false });
  }
});

/**
 * GET /auth/me
 */
router.get('/me', verifyToken, async (req, res) => {
  try {
    // Query the database for the current user's profile using userId from the verified token
    const [rows] = await pool.execute(
        'SELECT userId, email, fullName, headlineRole, program, college, location, bio, profileImageUri FROM users WHERE userId = ?', 
        [req.tokenData.userId]
    );
    
    // If user is not found, return an error response
    if (rows.length === 0) {
        return res.status(404).json({ error: "User not found", success: false });
    }

    // Respond with user profile data (excluding password)
    res.status(200).json({ success: true, user: rows[0] });
  } catch (err) {
      res.status(500).json({ error: "Database error", success: false });
  }
});

export default router;
