-- MariaDB Schema based on ERD and SkillSwap Backend Handoff
CREATE DATABASE IF NOT EXISTS `skillswap`;

USE `skillswap`;

-- 1. Create USER Table
CREATE TABLE `users` (
    `userId` INT AUTO_INCREMENT PRIMARY KEY,
    `email` VARCHAR(255) UNIQUE NOT NULL,
    `passwordHash` VARCHAR(255) NOT NULL,
    `fullName` VARCHAR(255) NOT NULL,
    `headlineRole` VARCHAR(255),
    `program` VARCHAR(255),
    `college` VARCHAR(255),
    `location` VARCHAR(255),
    `bio` TEXT,
    `profileImageUri` TEXT,
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. Create SKILL Table
CREATE TABLE `skills` (
    `skillId` INT AUTO_INCREMENT PRIMARY KEY,
    `userId` INT NOT NULL,
    `skillName` VARCHAR(255) NOT NULL,
    `level` VARCHAR(100),
    `type` ENUM('OFFER', 'NEED') NOT NULL,
    CONSTRAINT `fk_skill_user` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`) ON DELETE CASCADE
);

-- 3. Create SWAPREQUEST Table
CREATE TABLE `swap_requests` (
    `requestId` INT AUTO_INCREMENT PRIMARY KEY,
    `fromUserId` INT NOT NULL,
    `toUserId` INT NOT NULL,
    `requestedSkill` VARCHAR(255) NOT NULL,
    `offeredSkill` VARCHAR(255) NOT NULL,
    `message` TEXT,
    `status` ENUM('pending', 'accepted', 'declined') NOT NULL DEFAULT 'pending',
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_request_from_user` FOREIGN KEY (`fromUserId`) REFERENCES `users` (`userId`),
    CONSTRAINT `fk_request_to_user` FOREIGN KEY (`toUserId`) REFERENCES `users` (`userId`)
);

-- 4. Create MESSAGE Table
CREATE TABLE `messages` (
    `messageId` INT AUTO_INCREMENT PRIMARY KEY,
    `requestId` INT NOT NULL,
    `senderId` INT NOT NULL,
    `content` TEXT NOT NULL,
    `sentAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_message_request` FOREIGN KEY (`requestId`) REFERENCES `swap_requests` (`requestId`) ON DELETE CASCADE,
    CONSTRAINT `fk_message_sender` FOREIGN KEY (`senderId`) REFERENCES `users` (`userId`)
);

-- 5. Create POSTS Table
CREATE TABLE `posts` (
    `postId` INT AUTO_INCREMENT PRIMARY KEY,
    `userId` INT NOT NULL,
    `content` TEXT NOT NULL,
    `offeredSkill` VARCHAR(255),
    `neededSkill` VARCHAR(255),
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_post_user` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`) ON DELETE CASCADE
);

