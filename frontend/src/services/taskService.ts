import axios from 'axios';
import { Task } from '../types/Task';

const API_URL = 'http://localhost:8888/api/tasks';

export const taskService = {
    async getAllTasks(): Promise<Task[]> {
        const response = await axios.get<Task[]>(API_URL);
        return response.data;
    },

    async getTaskById(id: number): Promise<Task> {
        const response = await axios.get<Task>(`${API_URL}/${id}`);
        return response.data;
    },

    async createTask(task: Omit<Task, 'id'>): Promise<Task> {
        const response = await axios.post<Task>(API_URL, task);
        return response.data;
    },

    async updateTask(id: number, task: Task): Promise<Task> {
        const response = await axios.put<Task>(`${API_URL}/${id}`, task);
        return response.data;
    },

    async deleteTask(id: number): Promise<void> {
        await axios.delete(`${API_URL}/${id}`);
    }
}; 