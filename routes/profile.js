import express from 'express';
import pool from '../modules/database.js';
import jwt from 'jsonwebtoken';
import { verifyToken } from '../modules/authentication.js';
import { validateFields } from '../modules/validation.js';

const router = express.Router();

// POST /profiles/complete - Update profile details after signup
router.post('/complete', verifyToken, async (req, res) => {
  // Validate input
  const { headlineRole, program, college, location, bio } = req.body;
  const validationError = validateFields({ headlineRole, program, college, location, bio });

  // If validation fails, return 400 with error message
  if (validationError) {
    return res.status(400).json({ error: validationError, success: false });
  }

  try {
    // SQL query to update the user's profile using the userId from the verified token
    await pool.execute(
      'UPDATE users SET headlineRole = ?, program = ?, college = ?, location = ?, bio = ?, updatedAt = CURRENT_TIMESTAMP WHERE userId = ?',
      [headlineRole, program, college, location, bio, req.tokenData.userId]
    );

    // Respond with success message
    res.status(200).json({ success: true, message: 'Profile updated successfully' });
  } catch (err) {
    res.status(500).json({ error: "Database error", success: false });
  }
});

// GET /profiles - Get Discover list (excludes current user if logged in)
router.get('/', async (req, res) => {
  try {
    // get authorization header to identify current user (if token is provided)
    const authHeader = req.headers.authorization;

    // Initialize currentUserId to null (guest) and update if valid token is provided
    let currentUserId = null;

    // Optional: If a token is provided, we identify the current user to exclude them
    if (authHeader && authHeader.startsWith("Bearer ")) {
      // If token is provided, verify it and extract userId to exclude from results
      try {
        const decoded = jwt.verify(authHeader.split(" ")[1], process.env.JWT_SECRET);
        currentUserId = decoded.userId;
      } catch (err) { /* token invalid, proceed as guest */ }
    }

    // SQL query to fetch all user profiles, excluding the current user if identified, ordered by creation date
    const query = currentUserId 
      ? 'SELECT userId, fullName, headlineRole, program, college, location, bio, profileImageUri FROM users WHERE userId != ? ORDER BY createdAt DESC'
      : 'SELECT userId, fullName, headlineRole, program, college, location, bio, profileImageUri FROM users ORDER BY createdAt DESC';
    
    // Use currentUserId as parameter if we are excluding the current user, otherwise no parameters
    const params = currentUserId ? [currentUserId] : [];
    const [rows] = await pool.execute(query, params);

    // Respond with success message and profiles data
    res.status(200).json({ success: true, profiles: rows });
  // Handle database errors
  } catch (err) {
    // For database errors, return a generic error message
    res.status(500).json({ error: "Database error", success: false });
  }
});

// GET /profiles/:userId - Get one user profile with their skills
router.get('/:userId', async (req, res) => {
  try {
    // SQL query to fetch user profile by userId, excluding sensitive info, and fetch their skills in a separate query
    const [userRows] = await pool.execute(
      'SELECT userId, fullName, headlineRole, program, college, location, bio, profileImageUri FROM users WHERE userId = ?',
      [req.params.userId]
    );

    // If user not found, return 404
    if (userRows.length === 0) {
      return res.status(404).json({ error: "User not found", success: false });
    }

    // SQL query to fetch skills for the specified user
    const [skillRows] = await pool.execute(
      'SELECT skillName, level, type FROM skills WHERE userId = ?',
      [req.params.userId]
    );

    // Combine user profile and skills into one response object
    const profile = {
      ...userRows[0],
      skills: skillRows
    };

    // Respond with success message and profile data
    res.status(200).json({ success: true, profile });
  // Handle database errors  
  } catch (err) {
    // For database errors, return a generic error message
    res.status(500).json({ error: "Database error", success: false });
  }
});

// PUT /profiles/:userId - Update own profile
router.put('/:userId', verifyToken, async (req, res) => {
  // Ensure users can only update their own profile
  if (parseInt(req.params.userId) !== req.tokenData.userId) {
    return res.status(403).json({ error: "Unauthorized to update this profile", success: false });
  }

  // Validate input
  const { fullName, headlineRole, program, college, location, bio, profileImageUri } = req.body;
  const validationError = validateFields({ fullName, headlineRole, program, college, location, bio });

  // If validation fails, return 400 with error message
  if (validationError) {
    return res.status(400).json({ error: validationError, success: false });
  }

  try {
    // SQL query to update user profile in database using req.tokenData.userId to ensure only own profile is updated
    const [result] = await pool.execute(
      'UPDATE users SET fullName = ?, headlineRole = ?, program = ?, college = ?, location = ?, bio = ?, profileImageUri = ?, updatedAt = CURRENT_TIMESTAMP WHERE userId = ?',
      [fullName, headlineRole, program, college, location, bio, profileImageUri, req.tokenData.userId]
    );
    // If no rows were affected, the profile was not found (should not happen since we check userId) or no changes were made
    if (result.affectedRows === 0) {
      return res.status(404).json({ error: "Profile not found or no changes made", success: false });
    }

    // Respond with success message    res.status(200).json({ success: true, message: 'Profile updated successfully' });
    res.status(200).json({ success: true, message: 'Profile updated successfully' });
  } catch (err) {
    res.status(500).json({ error: "Database error", success: false });
  }
});

export default router;
