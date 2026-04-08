const express = require('express');
const router = express.Router();

// POST /auth/signup - Create user account
router.post('/signup', (req, res) => {
  res.status(501).json({ message: 'Not implemented yet' });
});

// POST /auth/login - Authenticate user
router.post('/login', (req, res) => {
  res.status(501).json({ message: 'Not implemented yet' });
});

// GET /auth/me - Get current signed-in user
router.get('/me', (req, res) => {
  res.status(501).json({ message: 'Not implemented yet' });
});

module.exports = router;
