export interface User {
  id: string;
  username: string;
  password?: string; // Only used internally for mock auth check
  avatar?: string;
  status: 'online' | 'offline' | 'busy';
}

export interface Message {
  id: string;
  content: string;
  senderId: string;
  recipientId?: string; // If present, it's a DM. If null/undefined, it's a public channel message
  timestamp: string; // ISO string
  type: 'text' | 'image';
}

export interface AuthResponse {
  user: User;
  accessToken: string;
  refreshToken: string;
}

export interface ChatRoom {
  id: string;
  name: string;
  type: 'channel' | 'dm';
  participants: User[];
  lastMessage?: Message;
  unreadCount: number;
}
