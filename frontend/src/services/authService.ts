import axios from 'axios';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:3000/api';

export interface User {
    id: string;
    username: string;
    email: string;
    role: 'admin' | 'user';
}

export interface LoginCredentials {
    username: string;
    password: string;
}

export interface RegisterData {
    username: string;
    email: string;
    password: string;
}

export interface LoginRequest {
    email: string;
    password: string;
}

export const authService = {
    async login(credentials: LoginCredentials): Promise<User> {
        const response = await axios.post(`${API_URL}/auth/login`, credentials);
        const { token, user } = response.data;
        localStorage.setItem('token', token);
        return user;
    },

    async register(data: RegisterData): Promise<User> {
        const response = await axios.post(`${API_URL}/auth/register`, data);
        return response.data;
    },

    async logout(): Promise<void> {
        await axios.post(`${API_URL}/auth/logout`);
    },

    async getCurrentUser(): Promise<User | null> {
        try {
            const response = await axios.get(`${API_URL}/auth/me`);
            return response.data;
        } catch (error) {
            return null;
        }
    },

    getToken(): string | null {
        return localStorage.getItem('token');
    },

    isAuthenticated(): boolean {
        return !!localStorage.getItem('token');
    }
};

export const login = async (credentials: LoginRequest): Promise<User> => {
    const response = await axios.post(`${API_URL}/auth/login`, credentials);
    return response.data;
};

export const logout = async (): Promise<void> => {
    await axios.post(`${API_URL}/auth/logout`);
};

export const getCurrentUser = async (): Promise<User | null> => {
    try {
        const response = await axios.get(`${API_URL}/auth/me`);
        return response.data;
    } catch (error) {
        return null;
    }
}; 