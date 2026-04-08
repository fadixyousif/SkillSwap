const express = require('express');
const router = express.Router();

// POST /requests/send - Send swap request
router.post('/send', (req, res) => {
  res.status(501).json({ message: 'Not implemented yet' });
});

// GET /requests/incoming - List incoming requests
router.get('/incoming', (req, res) => {
  res.status(501).json({ message: 'Not implemented yet' });
});

// GET /requests/outgoing - List outgoing requests
router.get('/outgoing', (req, res) => {
  res.status(501).json({ message: 'Not implemented yet' });
});

// PATCH /requests/:requestId/accept - Accept request
router.patch('/:requestId/accept', (req, res) => {
  res.status(501).json({ message: 'Not implemented yet' });
});

// PATCH /requests/:requestId/decline - Decline request
router.patch('/:requestId/decline', (req, res) => {
  res.status(501).json({ message: 'Not implemented yet' });
});

module.exports = router;
