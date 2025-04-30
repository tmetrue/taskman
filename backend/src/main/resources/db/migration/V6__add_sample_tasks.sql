-- Add sample tasks to the database
-- All tasks will be assigned to the Backlog category (category_id = 1)

INSERT INTO tasks (title, description, completed, category_id) VALUES
('Add users', 'Create functionality to add and manage users in the system', FALSE, 1),
('Add admin page', 'Create an admin dashboard for system management', FALSE, 1),
('Add login page', 'Implement a user-friendly login interface', FALSE, 1),
('Add some statistics', 'Implement data visualization and statistics tracking', FALSE, 1),
('Use drag and drop', 'Implement drag and drop functionality for task management', FALSE, 1);