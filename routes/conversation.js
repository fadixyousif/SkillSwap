import express from 'express';
import pool from '../modules/database.js';
import { verifyToken } from '../modules/authentication.js';
import { validateFields } from '../modules/validation.js';

const router = express.Router();

// GET /conversations - List conversations for the authenticated user
router.get('/', verifyToken, async (req, res) => {
  try {
    // SQL query to fetch conversations for the authenticated user, including the last message and partner info
    const [rows] = await pool.execute(`
      SELECT 
        sr.requestId,
        sr.status as requestStatus,
        u.fullName as partnerName,
        u.profileImageUri as partnerImage,
        (SELECT content FROM messages WHERE requestId = sr.requestId ORDER BY sentAt DESC LIMIT 1) as lastMessage,
        (SELECT sentAt FROM messages WHERE requestId = sr.requestId ORDER BY sentAt DESC LIMIT 1) as lastMessageAt
      FROM swap_requests sr
      JOIN users u ON (sr.fromUserId = u.userId OR sr.toUserId = u.userId)
      WHERE (sr.fromUserId = ? OR sr.toUserId = ?) AND u.userId != ?
      ORDER BY lastMessageAt DESC
    `, [req.tokenData.userId, req.tokenData.userId, req.tokenData.userId]);

    // Respond with success message and conversations data
    res.status(200).json({ success: true, conversations: rows });
  // Handle database errors
  } catch (err) {
    // For database errors, return a generic error message
    res.status(500).json({ error: "Database error", success: false });
  }
});

// GET /conversations/:requestId/messages - Get message history
router.get('/:requestId/messages', verifyToken, async (req, res) => {
  try {
    // SQL query to fetch messages for the specified conversation, ensuring the authenticated user is a participant
    const [rows] = await pool.execute(
      'SELECT * FROM messages WHERE requestId = ? ORDER BY sentAt ASC',
      [req.params.requestId]
    );

    // If no messages found, it could be an invalid conversation or just no messages yet. We can return an empty array for the latter case.
    if (rows.length === 0) {
      return res.status(404).json({ error: "Conversation not found", success: false });
    }

    // Respond with success message and messages data
    res.status(200).json({ success: true, messages: rows });
  // Handle database errors
  } catch (err) {
    // For database errors, return a generic error message
    res.status(500).json({ error: "Database error", success: false });
  }
});

// POST /conversations/:requestId/messages - Send a message
router.post('/:requestId/messages', verifyToken, async (req, res) => {
  // Validate input
  const { content } = req.body;
  const validationError = validateFields({ content });

  // If validation fails, return a 400 error with the validation message
  if (validationError) {
    return res.status(400).json({ error: validationError, success: false });
  }

  try {
    // SQL query to fetch the swap request details to verify the conversation exists, the user is a participant, and the request is accepted
    const [requestRows] = await pool.execute(
      'SELECT fromUserId, toUserId, status FROM swap_requests WHERE requestId = ?',
      [req.params.requestId]
    );

    // If no request found, return 404
    if (requestRows.length === 0) {
      return res.status(404).json({ error: "Conversation not found", success: false });
    }

    // Extract relevant info from the swap request
    const { fromUserId, toUserId, status } = requestRows[0];

    // Check if the request is accepted before allowing messages
    if (status !== 'accepted') {
      return res.status(403).json({ error: "Messages can only be sent once the swap request is accepted", success: false });
    }

    // Ensure the authenticated user is either the sender or receiver of the swap request
    if (req.tokenData.userId !== fromUserId && req.tokenData.userId !== toUserId) {
      return res.status(403).json({ error: "Unauthorized", success: false });
    }

    // Insert the new message into the database
    const [result] = await pool.execute(
      'INSERT INTO messages (requestId, senderId, content) VALUES (?, ?, ?)',
      [req.params.requestId, req.tokenData.userId, content]
    );

    // Respond with success message and new message ID
    res.status(201).json({ 
      success: true, 
      messageId: result.insertId,
      sentAt: new Date()
    });
  // Handle database errors
  } catch (err) {
    // For database errors, return a generic error message
    res.status(500).json({ error: "Database error", success: false });
  }
});

export default router;
