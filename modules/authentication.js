import jwt from "jsonwebtoken";
import crypto from "crypto";

export const verifyToken = (req, res, next) => { 
    // Get token from Authorization header
    const authHeader = req.headers.authorization;

    // Check if Authorization header is present and starts with "Bearer "
    if (!authHeader || !authHeader.startsWith("Bearer ")) {
        return res.status(403).json({ error: "Unauthorized", success: false });
    }

    // Extract token from "Bearer <token>"
    const token = authHeader.split(" ")[1];

    try { 
        // Use JWT_SECRET from .env
        const decoded = jwt.verify(token, process.env.JWT_SECRET); 

        // Attach decoded token data to request object for use in route handlers
        req.tokenData = decoded; 
        next();
    } catch (err) { 
        return res.status(401).json({ error: "Invalid token", success: false }); 
    } 
};

// Hash password using PBKDF2 with salt
export const hashPassword = (password, salt) => {
    return crypto.pbkdf2Sync(password, salt, 1000, 64, 'sha512').toString('hex');
};

// Generate random salt for password hashing
export const generateSalt = () => {
    return crypto.randomBytes(16).toString('hex');
};

// Generate JWT token for authenticated user based on userId and email
export const generateToken = (user) => {
    return jwt.sign(
        { userId: user.userId, email: user.email },
        process.env.JWT_SECRET,
        { expiresIn: '24h' }
    );
};