-- Add role field to users table
ALTER TABLE users ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'USER';

-- Create default admin user with password: adminSecure123!
INSERT INTO users (username, email, password_hash, first_name, last_name, role, enabled)
VALUES (
    'admin', 
    'admin@taskman.com', 
    '$2a$12$sWSdI13BJ5ipPca/5cyUhuap7VUuOLfkUfPr7SZ1QAZKD.kEQnXki', -- bcrypt hash for 'adminSecure123!'
    'Admin', 
    'User', 
    'ADMIN',
    true
) ON CONFLICT (username) DO NOTHING;