const express = require('express');
const cors = require('cors');
require('dotenv').config();

const authRoutes = require('./routes/auth');
const profileRoutes = require('./routes/profile');
const postRoutes = require('./routes/post');
const requestRoutes = require('./routes/request');
const conversationRoutes = require('./routes/conversation');

const app = express();

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
