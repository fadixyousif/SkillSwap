// index.js - Main entry point for the Express server
import express from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
dotenv.config();

// Import route handlers
import authRoutes from './routes/auth.js';
import profileRoutes from './routes/profile.js';
import postRoutes from './routes/post.js';
import requestRoutes from './routes/request.js';
import conversationRoutes from './routes/conversation.js';

// Create Express app
const app = express();

// enable CORS and JSON parsing
app.use(cors());
app.use(express.json());

// Routes
app.use('/auth', authRoutes);
app.use('/profiles', profileRoutes);
app.use('/posts', postRoutes);
app.use('/requests', requestRoutes);
app.use('/conversations', conversationRoutes);

const PORT = process.env.PORT || 5000;
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
