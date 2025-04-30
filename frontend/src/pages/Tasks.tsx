import { useEffect, useState } from 'react';
import { 
    Box, 
    Typography, 
    List, 
    ListItem, 
    ListItemText, 
    ListItemSecondaryAction, 
    IconButton,
    Checkbox,
    Button,
    Paper,
    Stack,
    Chip
} from '@mui/material';
import { Delete as DeleteIcon, Edit as EditIcon, Add as AddIcon } from '@mui/icons-material';
import { Task } from '../types/Task';
import { taskService } from '../services/taskService';
import { useNavigate } from 'react-router-dom';

export default function Tasks() {
    const navigate = useNavigate();
    const [tasks, setTasks] = useState<Task[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        loadTasks();
    }, []);

    const loadTasks = async () => {
        try {
            const data = await taskService.getAllTasks();
            setTasks(data);
            setError(null);
        } catch (err) {
            setError('Failed to load tasks');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleToggleComplete = async (task: Task) => {
        try {
            const updatedTask = await taskService.updateTask(task.id!, {
                ...task,
                completed: !task.completed
            });
            setTasks(tasks.map(t => t.id === updatedTask.id ? updatedTask : t));
        } catch (err) {
            console.error('Failed to update task:', err);
        }
    };

    const handleDelete = async (id: number) => {
        try {
            await taskService.deleteTask(id);
            setTasks(tasks.filter(task => task.id !== id));
        } catch (err) {
            console.error('Failed to delete task:', err);
        }
    };

    const handleEdit = (task: Task) => {
        navigate(`/tasks/${task.id}/edit`);
    };

    if (loading) return <Typography>Loading...</Typography>;
    if (error) return <Typography color="error">{error}</Typography>;

    return (
        <Box sx={{ maxWidth: 800, mx: 'auto', p: 3 }}>
            <Stack direction="row" justifyContent="space-between" alignItems="center" mb={3}>
                <Typography variant="h4" component="h1">
                    My Tasks
                </Typography>
                <Button
                    variant="contained"
                    startIcon={<AddIcon />}
                    onClick={() => navigate('/tasks/new')}
                >
                    Create Task
                </Button>
            </Stack>
            
            <Paper elevation={2}>
                <List>
                    {tasks.map((task) => (
                        <ListItem
                            key={task.id}
                            divider
                            secondaryAction={
                                <ListItemSecondaryAction>
                                    <IconButton 
                                        edge="end" 
                                        aria-label="edit"
                                        onClick={() => handleEdit(task)}
                                    >
                                        <EditIcon />
                                    </IconButton>
                                    <IconButton 
                                        edge="end" 
                                        aria-label="delete"
                                        onClick={() => handleDelete(task.id!)}
                                    >
                                        <DeleteIcon />
                                    </IconButton>
                                </ListItemSecondaryAction>
                            }
                        >
                            <Checkbox
                                edge="start"
                                checked={task.completed}
                                onChange={() => handleToggleComplete(task)}
                            />
                            <ListItemText
                                primary={
                                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                                        <Typography 
                                            variant="body1" 
                                            sx={{ 
                                                textDecoration: task.completed ? 'line-through' : 'none',
                                                color: task.completed ? 'text.secondary' : 'text.primary'
                                            }}
                                        >
                                            {task.title}
                                        </Typography>
                                        {task.dueDate && (
                                            <Chip 
                                                size="small" 
                                                label={`Due: ${new Date(task.dueDate).toLocaleDateString()}`}
                                                color={new Date(task.dueDate) < new Date() ? 'error' : 'default'}
                                            />
                                        )}
                                    </Box>
                                }
                                secondary={
                                    task.description && (
                                        <Typography 
                                            variant="body2" 
                                            color="text.secondary"
                                            sx={{ 
                                                textDecoration: task.completed ? 'line-through' : 'none'
                                            }}
                                        >
                                            {task.description}
                                        </Typography>
                                    )
                                }
                            />
                        </ListItem>
                    ))}
                    {tasks.length === 0 && (
                        <ListItem>
                            <ListItemText 
                                primary="No tasks found" 
                                secondary="Create a new task to get started"
                            />
                        </ListItem>
                    )}
                </List>
            </Paper>
        </Box>
    );
} 