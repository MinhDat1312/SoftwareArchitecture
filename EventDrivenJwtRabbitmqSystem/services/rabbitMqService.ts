import { Message, User } from '../types';

/**
 * MOCK RABBITMQ SERVICE with Cross-Tab Communication
 * 
 * Uses localStorage 'storage' events to simulate an Exchange/Queue broker.
 * This allows two browser tabs logged in as different users to chat.
 */

const HISTORY_KEY = 'rbt_chat_history';

type MessageHandler = (msg: Message) => void;

class MockRabbitMQService {
  private handlers: MessageHandler[] = [];
  private isConnected: boolean = false;
  private currentUser: User | null = null;
  private storageHandler: ((e: StorageEvent) => void) | null = null;

  connect(user: User) {
    this.currentUser = user;
    this.isConnected = true;
    console.log(`[RabbitMQ] Connected as ${user.username}`);

    // Load existing history immediately
    this.loadHistory();

    // Listen for messages from other tabs
    this.storageHandler = (e: StorageEvent) => {
      if (e.key === HISTORY_KEY && e.newValue) {
        const history: Message[] = JSON.parse(e.newValue);
        const latestMsg = history[history.length - 1];
        if (latestMsg) {
          this.notifySubscribers(latestMsg);
        }
      }
    };
    window.addEventListener('storage', this.storageHandler);
  }

  disconnect() {
    this.isConnected = false;
    this.handlers = [];
    if (this.storageHandler) {
      window.removeEventListener('storage', this.storageHandler);
      this.storageHandler = null;
    }
    console.log(`[RabbitMQ] Disconnected`);
  }

  subscribe(handler: MessageHandler) {
    this.handlers.push(handler);
    return () => {
      this.handlers = this.handlers.filter(h => h !== handler);
    };
  }

  publish(content: string, recipientId?: string) {
    if (!this.isConnected || !this.currentUser) return;

    const newMessage: Message = {
      id: crypto.randomUUID(),
      content,
      senderId: this.currentUser.id,
      recipientId: recipientId, // Undefined = Public Channel
      timestamp: new Date().toISOString(),
      type: 'text'
    };

    // 1. Get current history
    const historyStr = localStorage.getItem(HISTORY_KEY);
    const history: Message[] = historyStr ? JSON.parse(historyStr) : [];

    // 2. Append new message
    history.push(newMessage);

    // 3. Save (This triggers 'storage' event in OTHER tabs)
    localStorage.setItem(HISTORY_KEY, JSON.stringify(history));

    // 4. Notify OWN UI (storage event doesn't fire for current tab)
    this.notifySubscribers(newMessage);

    // MOCK: Auto-reply bot if in general channel
    if (!recipientId && content.includes('@bot')) {
      this.simulateAutoReply(content);
    }
  }

  // Load all history on connect and notify subscribers (so UI populates)
  private loadHistory() {
    const historyStr = localStorage.getItem(HISTORY_KEY);
    if (historyStr) {
      const history: Message[] = JSON.parse(historyStr);
      history.forEach(msg => this.notifySubscribers(msg));
    }
  }

  private notifySubscribers(msg: Message) {
    this.handlers.forEach(handler => handler(msg));
  }

  private simulateAutoReply(triggerContent: string) {
    setTimeout(() => {
      const reply: Message = {
        id: crypto.randomUUID(),
        content: `Auto-reply to "${triggerContent}"`,
        senderId: 'bot-1',
        timestamp: new Date().toISOString(),
        type: 'text'
      };
      
      const history = JSON.parse(localStorage.getItem(HISTORY_KEY) || '[]');
      history.push(reply);
      localStorage.setItem(HISTORY_KEY, JSON.stringify(history));
      this.notifySubscribers(reply);
    }, 1000);
  }
}

export const rabbitService = new MockRabbitMQService();
