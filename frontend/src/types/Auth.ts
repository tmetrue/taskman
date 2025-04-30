export interface AuthRequest {
    username: string;
    password: string;
}

export interface UserResponse {
    id: number | null;
    username: string;
    email: string;
    firstName: string | null;
    lastName: string | null;
} 