import { AuthResponse, User } from '../types';

// Constants for LocalStorage
const ACCESS_TOKEN_KEY = 'rbt_access_token';
const REFRESH_TOKEN_KEY = 'rbt_refresh_token';
const USER_KEY = 'rbt_user';
const USERS_DB_KEY = 'rbt_users_db'; // "Database" of registered users

// Mock delay to simulate network latency
const delay = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms));

export const AuthService = {
  // Initialize DB with a demo user if empty
  init: () => {
    if (!localStorage.getItem(USERS_DB_KEY)) {
      const demoUser: User = {
        id: 'u_demo',
        username: 'demo',
        password: 'password', // In real app, this would be hashed
        avatar: `https://ui-avatars.com/api/?name=demo&background=0D8ABC&color=fff`,
        status: 'online'
      };
      localStorage.setItem(USERS_DB_KEY, JSON.stringify([demoUser]));
    }
  },

  // Simulate Login with credential check
  login: async (username: string, password: string): Promise<AuthResponse> => {
    AuthService.init();
    await delay(800);
    
    const usersStr = localStorage.getItem(USERS_DB_KEY);
    const users: User[] = usersStr ? JSON.parse(usersStr) : [];
    
    const foundUser = users.find(u => u.username === username && u.password === password);

    if (!foundUser) {
      throw new Error('Invalid username or password');
    }
    
    // Remove password from returned object
    const { password: _, ...safeUser } = foundUser;

    const response: AuthResponse = {
      user: safeUser as User,
      accessToken: 'mock_jwt_access_token_' + Date.now(),
      refreshToken: 'mock_jwt_refresh_token_' + Date.now(),
    };

    AuthService.saveSession(response);
    return response;
  },

  // Simulate Register and save to DB
  register: async (username: string, password: string): Promise<AuthResponse> => {
    AuthService.init();
    await delay(800);

    const usersStr = localStorage.getItem(USERS_DB_KEY);
    const users: User[] = usersStr ? JSON.parse(usersStr) : [];

    if (users.find(u => u.username === username)) {
      throw new Error('Username already taken');
    }

    const newUser: User = {
      id: 'u_' + Date.now(),
      username: username,
      password: password,
      avatar: `https://ui-avatars.com/api/?name=${username}&background=random&color=fff`,
      status: 'online'
    };

    users.push(newUser);
    localStorage.setItem(USERS_DB_KEY, JSON.stringify(users));

    const { password: _, ...safeUser } = newUser;

    const response: AuthResponse = {
      user: safeUser as User,
      accessToken: 'mock_jwt_access_token_' + Date.now(),
      refreshToken: 'mock_jwt_refresh_token_' + Date.now(),
    };

    AuthService.saveSession(response);
    return response;
  },

  // Get all users (for the chat sidebar contact list)
  getAllUsers: async (): Promise<User[]> => {
    await delay(300);
    const usersStr = localStorage.getItem(USERS_DB_KEY);
    const users: User[] = usersStr ? JSON.parse(usersStr) : [];
    return users.map(({ password, ...u }) => u as User); // Return without passwords
  },

  // Simulate Refresh Token
  refreshToken: async (): Promise<string> => {
    const currentRefresh = localStorage.getItem(REFRESH_TOKEN_KEY);
    if (!currentRefresh) throw new Error('No refresh token available');

    await delay(500);
    const newAccessToken = 'mock_jwt_access_token_refreshed_' + Date.now();
    localStorage.setItem(ACCESS_TOKEN_KEY, newAccessToken);
    return newAccessToken;
  },

  logout: () => {
    localStorage.removeItem(ACCESS_TOKEN_KEY);
    localStorage.removeItem(REFRESH_TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
  },

  saveSession: (response: AuthResponse) => {
    localStorage.setItem(ACCESS_TOKEN_KEY, response.accessToken);
    localStorage.setItem(REFRESH_TOKEN_KEY, response.refreshToken);
    localStorage.setItem(USER_KEY, JSON.stringify(response.user));
  },

  getUser: (): User | null => {
    const userStr = localStorage.getItem(USER_KEY);
    return userStr ? JSON.parse(userStr) : null;
  },

  isAuthenticated: (): boolean => {
    return !!localStorage.getItem(ACCESS_TOKEN_KEY);
  }
};