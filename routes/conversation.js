const express = require('express');
const router = express.Router();

// GET /conversations - Get chat list for signed-in user
router.get('/', (req, res) => {
  res.status(501).json({ message: 'Not implemented yet' });
});

// GET /conversations/:conversationId/messages - Get message history
router.get('/:conversationId/messages', (req, res) => {
  res.status(501).json({ message: 'Not implemented yet' });
});

// POST /conversations/:conversationId/messages - Send a message
router.post('/:conversationId/messages', (req, res) => {
  res.status(501).json({ message: 'Not implemented yet' });
});

// POST /conversations - Create or fetch conversation between users
router.post('/', (req, res) => {
  res.status(501).json({ message: 'Not implemented yet' });
});

module.exports = router;
