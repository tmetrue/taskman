-- First, set category_id to NULL for all tasks to avoid foreign key constraint issues
UPDATE tasks SET category_id = NULL;

-- Delete existing categories
DELETE FROM categories;

-- Reset the ID sequence to ensure consistent ordering
ALTER SEQUENCE categories_id_seq RESTART WITH 1;

-- Insert the required categories with proper display order
INSERT INTO categories (name, description, display_order) VALUES
('Backlog', 'Tasks waiting to be picked up for development', 1),
('In Development', 'Tasks currently being worked on', 2),
('Test', 'Tasks ready for testing', 3),
('Ready for Production', 'Tasks that passed testing and are ready to be deployed', 4),
('Done', 'Tasks that are completed and deployed to production', 5);

-- Set all tasks to Backlog category by default
UPDATE tasks SET category_id = 1 WHERE category_id IS NULL;