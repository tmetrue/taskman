import axios from 'axios';
import { AuthRequest, UserResponse } from '../types/Auth';

const API_URL = 'http://localhost:8888';

export const authService = {
    async login(username: string, password: string): Promise<string> {
        const response = await axios.post(`${API_URL}/api/auth/login`, { username, password });
        const token = response.headers['authorization'];
        if (token) {
            localStorage.setItem('token', token);
            axios.defaults.headers.common['Authorization'] = token;
        }
        return token;
    },

    async register(userData: { username: string; password: string; email: string }): Promise<UserResponse> {
        const response = await axios.post<UserResponse>(`${API_URL}/api/auth/register`, userData);
        return response.data;
    },

    logout(): void {
        localStorage.removeItem('token');
        delete axios.defaults.headers.common['Authorization'];
    },

    getToken(): string | null {
        return localStorage.getItem('token');
    },

    isAuthenticated(): boolean {
        return !!this.getToken();
    }
}; 