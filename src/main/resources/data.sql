-- Place this file in src/main/resources/data.sql

-- Insert sample hackathons
INSERT INTO hackathons (title, platform, link, mode, location, start_date, end_date, status, created_at, updated_at)
VALUES 
('HackMIT 2025', 'DEVPOST', 'https://hackmit.org', 'OFFLINE', 'Boston, MA', '2025-09-20 09:00:00', '2025-09-22 18:00:00', 'UPCOMING', NOW(), NOW()),
('MLH Local Hack Day', 'DEVPOST', 'https://localhackday.mlh.io', 'HYBRID', 'Global', '2025-12-01 10:00:00', '2025-12-01 22:00:00', 'UPCOMING', NOW(), NOW()),
('Smart India Hackathon', 'UNSTOP', 'https://www.sih.gov.in', 'OFFLINE', 'New Delhi, India', '2025-08-15 09:00:00', '2025-08-17 17:00:00', 'UPCOMING', NOW(), NOW()),
('ETHGlobal Hackathon', 'DEVFOLIO', 'https://ethglobal.com', 'ONLINE', 'Virtual', '2025-11-10 00:00:00', '2025-11-12 23:59:00', 'UPCOMING', NOW(), NOW());

-- Insert sample users (password is "password123" for all - BCrypt hash)
INSERT INTO users (id, name, email, password, bio, github_url, linkedin_url, role, verified, created_at, updated_at)
VALUES 
('11111111-1111-1111-1111-111111111111', 'John Doe', 'john@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J0dMX8U7n.kJkPXwLq6MjQPL.qqhLm', 'Full-stack developer with 3 years of experience', 'https://github.com/johndoe', 'https://linkedin.com/in/johndoe', 'USER', true, NOW(), NOW()),
('22222222-2222-2222-2222-222222222222', 'Jane Smith', 'jane@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J0dMX8U7n.kJkPXwLq6MjQPL.qqhLm', 'UI/UX Designer passionate about creating intuitive interfaces', 'https://github.com/janesmith', 'https://linkedin.com/in/janesmith', 'USER', true, NOW(), NOW()),
('33333333-3333-3333-3333-333333333333', 'Admin User', 'admin@buildbuddy.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J0dMX8U7n.kJkPXwLq6MjQPL.qqhLm', 'Platform administrator', null, null, 'ADMIN', true, NOW(), NOW());

-- Insert user skills
INSERT INTO user_skills (user_id, skill)
VALUES 
('11111111-1111-1111-1111-111111111111', 'React'),
('11111111-1111-1111-1111-111111111111', 'Node.js'),
('11111111-1111-1111-1111-111111111111', 'MongoDB'),
('22222222-2222-2222-2222-222222222222', 'Figma'),
('22222222-2222-2222-2222-222222222222', 'Adobe XD'),
('22222222-2222-2222-2222-222222222222', 'CSS');

-- Insert sample team posts
INSERT INTO team_posts (id, title, description, hackathon_name, experience_level, mode, member_limit, creator_id, created_at, updated_at)
VALUES 
(1, 'Looking for Frontend Developer', 'We are building a healthcare application and need a skilled frontend developer proficient in React. The project focuses on creating an intuitive patient management system.', 'Smart India Hackathon', 'INTERMEDIATE', 'OFFLINE', 4, '11111111-1111-1111-1111-111111111111', NOW(), NOW()),
(2, 'Need ML Engineer for AI Project', 'Working on an AI-powered recommendation system. Looking for someone with experience in Python, TensorFlow, and data science.', 'HackMIT 2025', 'ADVANCED', 'HYBRID', 3, '22222222-2222-2222-2222-222222222222', NOW(), NOW());

-- Insert team required skills
INSERT INTO team_required_skills (team_id, skill)
VALUES 
(1, 'React'),
(1, 'JavaScript'),
(1, 'CSS'),
(2, 'Python'),
(2, 'TensorFlow'),
(2, 'Machine Learning');