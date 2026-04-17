import express from 'express';
import pool from '../modules/database.js';
import jwt from 'jsonwebtoken';
import { verifyToken } from '../modules/authentication.js';
import { validateFields } from '../modules/validation.js';

const router = express.Router();

// POST /profiles/complete - profile details after registration
router.post('/complete', verifyToken, async (req, res) => {
  // Validate input fields
  const { headlineRole, program, college, location, bio } = req.body;
  const validationError = validateFields({ headlineRole, program, college, location, bio });
  if (validationError) return res.status(400).json({ error: validationError, success: false });

  try {
    // Query to update user profile details
    await pool.execute(
      'UPDATE users SET headlineRole = ?, program = ?, college = ?, location = ?, bio = ?, updatedAt = CURRENT_TIMESTAMP WHERE userId = ?',
      [headlineRole, program, college, location, bio, req.tokenData.userId]
    );

    // Return success message
    res.status(200).json({ success: true, message: 'Profile updated successfully' });
  // catch any unexpected errors and log them for debugging
  } catch (err) {
    // Log the error for debugging
    console.error("POST /profiles/complete error:", err);
    res.status(500).json({ error: "Database error", success: false });
  }
});

// POST /profiles/skills - Save skills for logged in user
router.post('/skills', verifyToken, async (req, res) => {
  // Save skills for the authenticated user
  const { skills } = req.body;

  if (!skills || !Array.isArray(skills) || skills.length === 0) {
    return res.status(400).json({ error: "Skills array is required", success: false });
  }
  try {
    // Validate skill objects
    for (const skill of skills) {
      if (!skill || typeof skill.skillName !== 'string' || skill.skillName.trim() === '') {
        return res.status(400).json({ error: "Each skill must have a non-empty skillName", success: false });
      }
    }

    // Use a transaction: delete existing skills and insert new ones atomically
    const connection = await pool.getConnection();
    try {
      // Start transaction
      await connection.beginTransaction();
      // Delete existing skills for the user
      await connection.execute('DELETE FROM skills WHERE userId = ?', [req.tokenData.userId]);

      // Insert new skills
      const insertQuery = 'INSERT INTO skills (userId, skillName, level, type) VALUES (?, ?, ?, ?)';
      
      // Loop through skills and insert each one, handling optional fields
      for (const skill of skills) {
        // Trim skillName and validate level and type
        const skillName = skill.skillName.trim();
        const level = (skill.level === undefined || skill.level === null) ? null : Number(skill.level);
        const type = (typeof skill.type === 'string' && skill.type.trim() !== '') ? skill.type.trim() : null;
        // Insert skill into database
        await connection.execute(insertQuery, [req.tokenData.userId, skillName, level, type]);
      }

      // Commit transaction
      await connection.commit();
      // Return success message
      res.status(200).json({ success: true, message: 'Skills saved successfully' });
    // Rollback transaction on error
    } catch (err) {
      // Rollback transaction on error
      await connection.rollback();
      // Log the error for debugging
      console.error("Skills save error:", err);
      res.status(500).json({ error: "Database error", success: false });
    } finally {
      // Release the connection back to the pool
      connection.release();
    }
  // catch any unexpected errors and log them for debugging
  } catch (err) {
    // Log the error for debugging
    console.error("Skills save outer error:", err);
    res.status(500).json({ error: "Database error", success: false });
  }
});

// GET /profiles — returns all users WITH their skills included
router.get('/', async (req, res) => {
  try {
    // Check for JWT to determine if we should exclude the current user's profile
    const authHeader = req.headers.authorization;
    let currentUserId = null;
    if (authHeader && authHeader.startsWith("Bearer ")) {
      try {
        const decoded = jwt.verify(authHeader.split(" ")[1], process.env.JWT_SECRET);
        currentUserId = decoded.userId;
      } catch (err) { /* proceed as guest */ }
    }

    // Fetch all users, excluding current user if authenticated
    const query = currentUserId
      ? 'SELECT userId, fullName, headlineRole, program, college, location, bio, profileImageUri FROM users WHERE userId != ? ORDER BY createdAt DESC'
      : 'SELECT userId, fullName, headlineRole, program, college, location, bio, profileImageUri FROM users ORDER BY createdAt DESC';
    const params = currentUserId ? [currentUserId] : [];
    const [rows] = await pool.execute(query, params);

    // Fetch skills for all users in one query
    const [allSkills] = await pool.execute('SELECT skillId, userId, skillName, level, type FROM skills');

    // Attach skills to each profile
    const profiles = rows.map(user => ({
      ...user,
      skills: allSkills.filter(s => s.userId == user.userId)
    }));

    // Return profiles with skills
    res.status(200).json({ success: true, profiles });

  // catch any unexpected errors and log them for debugging
  } catch (err) {
    // Log the error for debugging
    console.error("GET /profiles error:", err);
    res.status(500).json({ error: "Database error", success: false });
  }
});

// GET /profiles/:userId
router.get('/:userId', async (req, res) => {
  try {
    // Query user profile details
    const [userRows] = await pool.execute(
      'SELECT userId, fullName, headlineRole, program, college, location, bio, profileImageUri FROM users WHERE userId = ?',
      [req.params.userId]
    );

    // If no user found, return 404
    if (userRows.length === 0) return res.status(404).json({ error: "User not found", success: false });

    // Query skills for the user
    const [skillRows] = await pool.execute(
      'SELECT skillId, skillName, level, type FROM skills WHERE userId = ?',
      [req.params.userId]
    );

    // Return profile with skills
    res.status(200).json({ success: true, profile: { ...userRows[0], skills: skillRows } });
  // catch any unexpected errors and log them for debugging
  } catch (err) {
    // Log the error for debugging
    console.error("GET /profiles/:userId error:", err);
    res.status(500).json({ error: "Database error", success: false });
  }
});

// PUT /profiles/:userId
router.put('/:userId', verifyToken, async (req, res) => {
  // Ensure the authenticated user is updating their own profile
  if (parseInt(req.params.userId) !== req.tokenData.userId) {
    return res.status(403).json({ error: "Unauthorized to update this profile", success: false });
  }

  // Validate input fields
  const { fullName, headlineRole, program, college, location, bio, profileImageUri } = req.body;
  const validationError = validateFields({ fullName, headlineRole, program, college, location, bio });
  if (validationError) return res.status(400).json({ error: validationError, success: false });

  try {
    // Query to update user profile details
    const [result] = await pool.execute(
      'UPDATE users SET fullName = ?, headlineRole = ?, program = ?, college = ?, location = ?, bio = ?, profileImageUri = ?, updatedAt = CURRENT_TIMESTAMP WHERE userId = ?',
      [fullName, headlineRole, program, college, location, bio, profileImageUri, req.tokenData.userId]
    );

    // If no rows were affected, the profile was not found
    if (result.affectedRows === 0) return res.status(404).json({ error: "Profile not found", success: false });

    // Return success message
    res.status(200).json({ success: true, message: 'Profile updated successfully' });
  // catch any unexpected errors and log them for debugging
  } catch (err) {
    // Log the error for debugging
    console.error("PUT /profiles/:userId error:", err);
    res.status(500).json({ error: "Database error", success: false });
  }
});

export default router;