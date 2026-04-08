const express = require('express');
const router = express.Router();

// POST /posts/create - Create a public post
router.post('/create', (req, res) => {
  res.status(501).json({ message: 'Not implemented yet' });
});

// GET /posts - Get posts feed
router.get('/', (req, res) => {
  res.status(501).json({ message: 'Not implemented yet' });
});

// GET /posts/user/:userId - Get all posts by one user
router.get('/user/:userId', (req, res) => {
  res.status(501).json({ message: 'Not implemented yet' });
});

module.exports = router;
