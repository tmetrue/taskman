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
    Paper
} from '@mui/material';
import { Delete as DeleteIcon, Edit as EditIcon } from '@mui/icons-material';
import { Task } from '../types/Task';
import { taskService } from '../services/taskService';

export default function Tasks() {
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

    if (loading) return <Typography>Loading...</Typography>;
    if (error) return <Typography color="error">{error}</Typography>;

    return (
        <Box sx={{ maxWidth: 800, mx: 'auto', p: 3 }}>
            <Typography variant="h4" component="h1" gutterBottom>
                Tasks
            </Typography>
            
            <Paper elevation={2}>
                <List>
                    {tasks.map((task) => (
                        <ListItem
                            key={task.id}
                            divider
                            secondaryAction={
                                <ListItemSecondaryAction>
                                    <IconButton edge="end" aria-label="edit">
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
                                primary={task.title}
                                secondary={
                                    <>
                                        {task.description && (
                                            <Typography component="span" variant="body2" color="text.secondary">
                                                {task.description}
                                            </Typography>
                                        )}
                                        {task.dueDate && (
                                            <Typography component="span" variant="body2" color="text.secondary" display="block">
                                                Due: {new Date(task.dueDate).toLocaleDateString()}
                                            </Typography>
                                        )}
                                    </>
                                }
                            />
                        </ListItem>
                    ))}
                </List>
            </Paper>
        </Box>
    );
} 