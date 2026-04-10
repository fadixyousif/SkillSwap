import express from 'express';
import pool from '../modules/database.js';
import { verifyToken } from '../modules/authentication.js';
import { validateFields } from '../modules/validation.js';

const router = express.Router();

// POST /posts/create - Create a public post
router.post('/create', verifyToken, async (req, res) => {
  // Validate input
  const { content, offeredSkill, neededSkill } = req.body;
  const validationError = validateFields({ content, offeredSkill, neededSkill });

  // If validation fails, return 400 with error message
  if (validationError) {
    return res.status(400).json({ error: validationError, success: false });
  }

  // Insert new post into database
  try {
    // Use req.tokenData.userId to associate post with the authenticated user
    const [result] = await pool.execute(
      'INSERT INTO posts (userId, content, offeredSkill, neededSkill) VALUES (?, ?, ?, ?)',
      [req.tokenData.userId, content, offeredSkill, neededSkill]
    );

    // Respond with success message and new post ID
    res.status(201).json({
      success: true,
      message: 'Post created successfully',
      postId: result.insertId
    });
  // Handle database errors
  } catch (err) {
    // For database errors, return a generic error message
    res.status(500).json({ error: "Database error", success: false });
  }
});

// GET /posts - Get posts feed
router.get('/', async (req, res) => {
  // Fetch all posts with author info, ordered by creation date
  try {
    // Join posts with users to get author details in one query
    const [rows] = await pool.execute(`
      SELECT p.*, u.fullName as authorName, u.headlineRole as authorRole, u.profileImageUri 
      FROM posts p 
      JOIN users u ON p.userId = u.userId 
      ORDER BY p.createdAt DESC
    `);
    
    // Respond with success message and posts data
    res.status(200).json({ success: true, posts: rows });
  // Handle database errors
  } catch (err) {
    // For database errors, return a generic error message
    res.status(500).json({ error: "Database error", success: false });
  }
});

// GET /posts/user/:userId - Get all posts by one user
router.get('/user/:userId', async (req, res) => {
  // Fetch posts for a specific user, ordered by creation date
  try {
    // Join posts with users to get author details in one query
    const [rows] = await pool.execute(
      'SELECT * FROM posts WHERE userId = ? ORDER BY createdAt DESC',
      [req.params.userId]
    );

    // Respond with success message and posts data
    res.status(200).json({ success: true, posts: rows });
  // Handle database errors
  } catch (err) {
    // For database errors, return a generic error message
    res.status(500).json({ error: "Database error", success: false });
  }
});

// DELETE /posts/:postId - Delete a post
router.delete('/:postId', verifyToken, async (req, res) => {
  // Delete a post if it belongs to the authenticated user
  try {
    // Use req.tokenData.userId to ensure users can only delete their own posts
    const [result] = await pool.execute(
      'DELETE FROM posts WHERE postId = ? AND userId = ?',
      [req.params.postId, req.tokenData.userId]
    );

    // If no rows were affected, the post was not found or did not belong to the user
    if (result.affectedRows === 0) {
      return res.status(404).json({ error: "Post not found or unauthorized", success: false });
    }

    // Respond with success message
    res.status(200).json({ success: true, message: "Post deleted successfully" });
  // Handle database errors
  } catch (err) {
    // For database errors, return a generic error message
    res.status(500).json({ error: "Database error", success: false });
  }
});

export default router;
