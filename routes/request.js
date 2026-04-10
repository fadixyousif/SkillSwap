import express from 'express';
import pool from '../modules/database.js';
import { verifyToken } from '../modules/authentication.js';
import { validateFields } from '../modules/validation.js';

const router = express.Router();

// POST /requests/send - Send swap request
router.post('/send', verifyToken, async (req, res) => {
  // Validate input
  const { toUserId, requestedSkill, offeredSkill, message } = req.body;
  const validationError = validateFields({ toUserId, requestedSkill, offeredSkill });

  // If validation fails, return 400 with error message
  if (validationError) {
    return res.status(400).json({ error: validationError, success: false });
  }

  // Prevent users from sending requests to themselves
  if (toUserId === req.tokenData.userId) {
    return res.status(400).json({ error: "You cannot send a request to yourself", success: false });
  }

  try {
    // Query to check if there is already an active request (pending or accepted) between these two users in either direction
    const [existingRequests] = await pool.execute(
      'SELECT requestId, status FROM swap_requests WHERE ((fromUserId = ? AND toUserId = ?) OR (fromUserId = ? AND toUserId = ?)) AND status IN ("pending", "accepted")',
      [req.tokenData.userId, toUserId, toUserId, req.tokenData.userId]
    );

    // If an active request exists, return a 409 Conflict error with an appropriate message
    if (existingRequests.length > 0) {
      const status = existingRequests[0].status;
      const message = status === 'pending' 
        ? "A pending swap request already exists between you and this user"
        : "An active accepted swap already exists between you and this user";
      return res.status(409).json({ error: message, success: false });
    }

    // Insert new swap request into database
    const [result] = await pool.execute(
      'INSERT INTO swap_requests (fromUserId, toUserId, requestedSkill, offeredSkill, message) VALUES (?, ?, ?, ?, ?)',
      [req.tokenData.userId, toUserId, requestedSkill, offeredSkill, message || '']
    );

    // Check if the insert was successful
    if (result.affectedRows === 0) {
      return res.status(500).json({ error: "Failed to create swap request", success: false });
    }

    // Respond with success message and new request ID
    res.status(201).json({ 
      success: true, 
      message: 'Swap request sent successfully', 
      requestId: result.insertId 
    });
  // Handle database errors
  } catch (err) {
    res.status(500).json({ error: "Database error", success: false });
    console.error("Error sending swap request:", err);
  }
});

// GET /requests/incoming - List incoming requests
router.get('/incoming', verifyToken, async (req, res) => {
  try {
    // Fetch incoming swap requests for the authenticated user, including sender's name and profile image
    const [rows] = await pool.execute(`
      SELECT sr.*, u.fullName as fromUserName, u.profileImageUri 
      FROM swap_requests sr 
      JOIN users u ON sr.fromUserId = u.userId 
      WHERE sr.toUserId = ? 
      ORDER BY sr.createdAt DESC
    `, [req.tokenData.userId]);

    // If no requests found, return an empty array
    if (rows.length === 0) {
      return res.status(200).json({ success: true, requests: [] });
    }

    // Respond with success message and requests data
    res.status(200).json({ success: true, requests: rows });
  // Handle database errors
  } catch (err) {
    // For database errors, return a generic error message
    res.status(500).json({ error: "Database error", success: false });
  }
});

// GET /requests/outgoing - List outgoing requests
router.get('/outgoing', verifyToken, async (req, res) => {
  try {
    // sql query to fetch outgoing swap requests for the authenticated user, including receiver's name and profile image
    const [rows] = await pool.execute(`
      SELECT sr.*, u.fullName as toUserName, u.profileImageUri 
      FROM swap_requests sr 
      JOIN users u ON sr.toUserId = u.userId 
      WHERE sr.fromUserId = ? 
      ORDER BY sr.createdAt DESC
    `, [req.tokenData.userId]);

    // If no requests found, return an empty array
    if (rows.length === 0) {
      return res.status(200).json({ success: true, requests: [] });
    }

    // Respond with success message and requests data
    res.status(200).json({ success: true, requests: rows });
  // Handle database errors
  } catch (err) {
    // For database errors, return a generic error message
    res.status(500).json({ error: "Database error", success: false });
  }
});

// PATCH /requests/:requestId/accept - Accept request
router.patch('/:requestId/accept', verifyToken, async (req, res) => {
  try {
    // query to update the swap request status to "accepted" if the authenticated user is the receiver and the request is still pending
    const [result] = await pool.execute(
      'UPDATE swap_requests SET status = "accepted", updatedAt = CURRENT_TIMESTAMP WHERE requestId = ? AND toUserId = ? AND status = "pending"',
      [req.params.requestId, req.tokenData.userId]
    );

    // If no request found, return 404
    if (result.affectedRows === 0) {
      return res.status(404).json({ error: "Request not found, already handled, or unauthorized", success: false });
    }

    // Respond with success message
    res.status(200).json({ success: true, message: "Request accepted" });
  // Handle database errors
  } catch (err) {
    // For database errors, return a generic error message
    res.status(500).json({ error: "Database error", success: false });
  }
});

// PATCH /requests/:requestId/decline - Decline request
router.patch('/:requestId/decline', verifyToken, async (req, res) => {
  try {
    // query to update the swap request status to "declined" if the authenticated user is the receiver and the request is still pending
    const [result] = await pool.execute(
      'UPDATE swap_requests SET status = "declined", updatedAt = CURRENT_TIMESTAMP WHERE requestId = ? AND toUserId = ? AND status = "pending"',
      [req.params.requestId, req.tokenData.userId]
    );

    // If no request found, return 404
    if (result.affectedRows === 0) {
      return res.status(404).json({ error: "Request not found, already handled, or unauthorized", success: false });
    }

    // Respond with success message
    res.status(200).json({ success: true, message: "Request declined" });
  // Handle database errors
  } catch (err) {
    // For database errors, return a generic error message
    res.status(500).json({ error: "Database error", success: false });
  }
});

export default router;
