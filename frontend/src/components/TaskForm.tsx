import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import {
    Box,
    Button,
    TextField,
    Typography,
    Paper,
    Container,
    Alert,
    Stack
} from '@mui/material';
import { Task } from '../types/Task';
import { taskService } from '../services/taskService';

interface TaskFormProps {
    mode: 'create' | 'edit';
}

export default function TaskForm({ mode }: TaskFormProps) {
    const navigate = useNavigate();
    const { id } = useParams<{ id: string }>();
    const [task, setTask] = useState<Partial<Task>>({
        title: '',
        description: '',
        completed: false,
        dueDate: ''
    });
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (mode === 'edit' && id) {
            loadTask(parseInt(id));
        }
    }, [mode, id]);

    const loadTask = async (taskId: number) => {
        try {
            const data = await taskService.getTaskById(taskId);
            setTask(data);
        } catch (err) {
            setError('Failed to load task');
            console.error(err);
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);
        setLoading(true);

        try {
            if (mode === 'create') {
                await taskService.createTask(task as Omit<Task, 'id'>);
            } else if (id) {
                await taskService.updateTask(parseInt(id), task as Task);
            }
            navigate('/');
        } catch (err) {
            setError('Failed to save task');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container maxWidth="sm">
            <Box sx={{ mt: 8 }}>
                <Paper elevation={3} sx={{ p: 4 }}>
                    <Typography variant="h4" component="h1" gutterBottom align="center">
                        {mode === 'create' ? 'Create Task' : 'Edit Task'}
                    </Typography>
                    
                    {error && (
                        <Alert severity="error" sx={{ mb: 2 }}>
                            {error}
                        </Alert>
                    )}

                    <form onSubmit={handleSubmit}>
                        <Stack spacing={2}>
                            <TextField
                                fullWidth
                                label="Title"
                                variant="outlined"
                                value={task.title}
                                onChange={(e) => setTask({ ...task, title: e.target.value })}
                                required
                            />
                            <TextField
                                fullWidth
                                label="Description"
                                variant="outlined"
                                multiline
                                rows={4}
                                value={task.description}
                                onChange={(e) => setTask({ ...task, description: e.target.value })}
                            />
                            <TextField
                                fullWidth
                                label="Due Date"
                                type="date"
                                variant="outlined"
                                InputLabelProps={{ shrink: true }}
                                value={task.dueDate || ''}
                                onChange={(e) => setTask({ ...task, dueDate: e.target.value })}
                            />
                            <Button
                                fullWidth
                                variant="contained"
                                color="primary"
                                type="submit"
                                disabled={loading}
                            >
                                {loading ? 'Saving...' : mode === 'create' ? 'Create Task' : 'Update Task'}
                            </Button>
                            <Button
                                fullWidth
                                variant="outlined"
                                onClick={() => navigate('/')}
                            >
                                Cancel
                            </Button>
                        </Stack>
                    </form>
                </Paper>
            </Box>
        </Container>
    );
} 