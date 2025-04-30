-- Create categories table
CREATE TABLE IF NOT EXISTS categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Add category_id to tasks table
ALTER TABLE tasks ADD COLUMN category_id INTEGER REFERENCES categories(id);

-- Insert default categories
INSERT INTO categories (name, description, display_order) VALUES
('Backlog', 'Tasks that are waiting to be worked on', 1),
('In Development', 'Tasks currently being worked on', 2),
('Testing', 'Tasks that are being tested', 3),
('Finished', 'Completed tasks', 4)
ON CONFLICT (name) DO NOTHING;