import express from 'express';
const router = express.Router();

// POST /profiles/create - Create profile after signup/onboarding
router.post('/create', (req, res) => {
  res.status(501).json({ message: 'Not implemented yet' });
});

// GET /profiles - Get Discover list
router.get('/', (req, res) => {
  res.status(501).json({ message: 'Not implemented yet' });
});

// GET /profiles/:userId - Get one user profile
router.get('/:userId', (req, res) => {
  res.status(501).json({ message: 'Not implemented yet' });
});

// PUT /profiles/:userId - Update own profile
router.put('/:userId', (req, res) => {
  res.status(501).json({ message: 'Not implemented yet' });
});

export default router;
