import React, { useEffect, useState, useRef } from 'react';
import { useAuth } from '../context/AuthContext';
import { AuthService } from '../services/authService';
import { rabbitService } from '../services/rabbitMqService';
import { Message, User } from '../types';
import { Send, LogOut, RefreshCw, Menu, Search, Image as ImageIcon, MoreVertical, Phone, Video, Hash, User as UserIcon } from 'lucide-react';
import { Button } from '../components/Button';

export const ChatPage: React.FC = () => {
  const { user, logout, refreshSession } = useAuth();
  const [messages, setMessages] = useState<Message[]>([]);
  const [inputText, setInputText] = useState('');
  
  // 'general' or a userId for DM
  const [activeRoomId, setActiveRoomId] = useState<string>('general');
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [availableUsers, setAvailableUsers] = useState<User[]>([]);
  
  const messagesEndRef = useRef<HTMLDivElement>(null);

  // Load available users for DM list
  useEffect(() => {
    const loadUsers = async () => {
      try {
        const users = await AuthService.getAllUsers();
        // Filter out self
        setAvailableUsers(users.filter(u => u.id !== user?.id));
      } catch (error) {
        console.error("Failed to load users", error);
      }
    };
    if (user) loadUsers();
  }, [user]);

  // Connect to RabbitMQ (Mock)
  useEffect(() => {
    if (user) {
      setMessages([]); // Clear local state on re-login/mount, let service repopulate from history
      rabbitService.connect(user);
      
      const unsubscribe = rabbitService.subscribe((msg) => {
        setMessages((prev) => {
          // Avoid duplicates if history loads same messages
          if (prev.some(p => p.id === msg.id)) return prev;
          return [...prev, msg];
        });
      });

      return () => {
        unsubscribe();
        rabbitService.disconnect();
      };
    }
  }, [user]);

  // Scroll to bottom on new message
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, activeRoomId]);

  const handleSendMessage = (e: React.FormEvent) => {
    e.preventDefault();
    if (!inputText.trim()) return;

    // If activeRoomId is 'general', recipientId is undefined.
    // If activeRoomId is a userId, recipientId is that userId.
    const recipientId = activeRoomId === 'general' ? undefined : activeRoomId;

    rabbitService.publish(inputText, recipientId);
    setInputText('');
  };

  const handleRefreshToken = async () => {
    await refreshSession();
    alert('JWT Refreshed! Check LocalStorage.');
  };

  // Filter messages for the current view
  const displayedMessages = messages.filter(msg => {
    if (activeRoomId === 'general') {
      // Show only public channel messages (no recipient)
      return !msg.recipientId;
    } else {
      // It's a DM with activeRoomId (which is a userId)
      const isMyMessageToThem = msg.senderId === user?.id && msg.recipientId === activeRoomId;
      const isTheirMessageToMe = msg.senderId === activeRoomId && msg.recipientId === user?.id;
      return isMyMessageToThem || isTheirMessageToMe;
    }
  });

  const getRoomName = () => {
    if (activeRoomId === 'general') return 'General';
    const otherUser = availableUsers.find(u => u.id === activeRoomId);
    return otherUser ? otherUser.username : 'Unknown User';
  };

  return (
    <div className="flex h-screen bg-white overflow-hidden">
      {/* Mobile Sidebar Overlay */}
      {isSidebarOpen && (
        <div 
          className="fixed inset-0 bg-black bg-opacity-50 z-20 lg:hidden"
          onClick={() => setIsSidebarOpen(false)}
        />
      )}

      {/* Sidebar */}
      <aside className={`
        fixed inset-y-0 left-0 z-30 w-72 bg-gray-50 border-r border-gray-200 transform transition-transform duration-300 ease-in-out lg:relative lg:translate-x-0
        ${isSidebarOpen ? 'translate-x-0' : '-translate-x-full'}
      `}>
        <div className="h-full flex flex-col">
          {/* Sidebar Header */}
          <div className="p-4 border-b border-gray-200 flex items-center justify-between bg-white">
            <div className="flex items-center space-x-3">
              <img src={user?.avatar} alt="Me" className="w-10 h-10 rounded-full border-2 border-green-400" />
              <div className="overflow-hidden">
                <h3 className="font-semibold text-gray-900 truncate max-w-[120px]">{user?.username}</h3>
                <span className="text-xs text-green-600 flex items-center">
                  <span className="w-2 h-2 bg-green-500 rounded-full mr-1"></span> Online
                </span>
              </div>
            </div>
            <div className="flex space-x-1">
                 <button onClick={handleRefreshToken} title="Refresh Token" className="p-2 text-gray-500 hover:bg-gray-100 rounded-full">
                    <RefreshCw className="w-4 h-4" />
                </button>
                <button onClick={logout} title="Logout" className="p-2 text-gray-500 hover:bg-gray-100 rounded-full">
                    <LogOut className="w-4 h-4" />
                </button>
            </div>
          </div>

          {/* Search */}
          <div className="p-4">
            <div className="relative">
              <input 
                type="text" 
                placeholder="Search..." 
                className="w-full pl-10 pr-4 py-2 bg-white border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
              <Search className="absolute left-3 top-2.5 w-4 h-4 text-gray-400" />
            </div>
          </div>

          {/* Sidebar Content */}
          <div className="flex-1 overflow-y-auto px-2 space-y-4">
            
            {/* Channels */}
            <div>
              <h4 className="px-3 text-xs font-semibold text-gray-500 uppercase tracking-wider mb-2">Channels</h4>
              <button
                onClick={() => { setActiveRoomId('general'); setIsSidebarOpen(false); }}
                className={`w-full flex items-center px-3 py-2 rounded-lg text-sm transition-colors ${
                  activeRoomId === 'general' ? 'bg-indigo-50 text-indigo-700' : 'text-gray-700 hover:bg-gray-100'
                }`}
              >
                <Hash className="w-4 h-4 mr-2 opacity-75" />
                <span className="font-medium">General</span>
              </button>
            </div>

            {/* Direct Messages */}
            <div>
              <h4 className="px-3 text-xs font-semibold text-gray-500 uppercase tracking-wider mb-2">Direct Messages</h4>
              {availableUsers.length === 0 ? (
                <p className="px-3 text-xs text-gray-400 italic">No other users found.</p>
              ) : (
                availableUsers.map(u => (
                  <button
                    key={u.id}
                    onClick={() => { setActiveRoomId(u.id); setIsSidebarOpen(false); }}
                    className={`w-full flex items-center px-3 py-2 rounded-lg text-sm transition-colors ${
                      activeRoomId === u.id ? 'bg-indigo-50 text-indigo-700' : 'text-gray-700 hover:bg-gray-100'
                    }`}
                  >
                    <div className="relative mr-2">
                        <img src={u.avatar} className="w-6 h-6 rounded-full" alt="" />
                        <span className="absolute bottom-0 right-0 w-2 h-2 bg-green-500 border-2 border-white rounded-full"></span>
                    </div>
                    <span className="font-medium truncate">{u.username}</span>
                  </button>
                ))
              )}
            </div>
          </div>
        </div>
      </aside>

      {/* Main Chat Area */}
      <main className="flex-1 flex flex-col min-w-0 bg-white">
        {/* Chat Header */}
        <header className="flex items-center justify-between px-6 py-4 border-b border-gray-200 bg-white shadow-sm z-10">
          <div className="flex items-center">
            <button 
              onClick={() => setIsSidebarOpen(true)}
              className="lg:hidden mr-4 text-gray-500 hover:text-gray-700"
            >
              <Menu className="w-6 h-6" />
            </button>
            <div>
              <div className="flex items-center gap-2">
                {activeRoomId === 'general' ? <Hash className="w-5 h-5 text-gray-400" /> : <UserIcon className="w-5 h-5 text-gray-400" />}
                <h2 className="text-lg font-bold text-gray-800">{getRoomName()}</h2>
              </div>
              <p className="text-sm text-gray-500">
                {activeRoomId === 'general' ? 'Public Channel' : 'Private Conversation'}
              </p>
            </div>
          </div>
          <div className="flex items-center space-x-4 text-gray-400">
             <Phone className="w-5 h-5 cursor-pointer hover:text-indigo-600" />
             <Video className="w-5 h-5 cursor-pointer hover:text-indigo-600" />
             <MoreVertical className="w-5 h-5 cursor-pointer hover:text-gray-600" />
          </div>
        </header>

        {/* Messages List */}
        <div className="flex-1 overflow-y-auto p-6 bg-gray-50 space-y-6 scrollbar-hide">
          {displayedMessages.length === 0 ? (
            <div className="h-full flex flex-col items-center justify-center text-gray-400 space-y-4">
              <div className="w-16 h-16 bg-gray-200 rounded-full flex items-center justify-center">
                <Send className="w-8 h-8 text-gray-400" />
              </div>
              <p>No messages yet. Say hello!</p>
            </div>
          ) : (
            displayedMessages.map((msg) => {
              const isMe = msg.senderId === user?.id;
              return (
                <div key={msg.id} className={`flex ${isMe ? 'justify-end' : 'justify-start'}`}>
                  <div className={`flex max-w-[75%] ${isMe ? 'flex-row-reverse space-x-reverse' : 'flex-row'} space-x-3`}>
                    
                    {/* Bubble */}
                    <div className={`relative px-4 py-2 shadow-sm rounded-2xl ${
                      isMe 
                        ? 'bg-indigo-600 text-white rounded-tr-none' 
                        : 'bg-white text-gray-800 border border-gray-100 rounded-tl-none'
                    }`}>
                      {/* Show sender name in Public channel if not me */}
                      {!isMe && activeRoomId === 'general' && (
                         <p className="text-xs font-bold text-indigo-500 mb-1">{msg.senderId === 'bot-1' ? 'Bot' : availableUsers.find(u=>u.id===msg.senderId)?.username || msg.senderId}</p>
                      )}
                      
                      <p className="text-sm break-words">{msg.content}</p>
                      <span className={`text-[10px] block mt-1 ${isMe ? 'text-indigo-200' : 'text-gray-400'}`}>
                        {new Date(msg.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                      </span>
                    </div>
                  </div>
                </div>
              );
            })
          )}
          <div ref={messagesEndRef} />
        </div>

        {/* Input Area */}
        <div className="p-4 bg-white border-t border-gray-200">
          <form onSubmit={handleSendMessage} className="flex items-center space-x-4 max-w-4xl mx-auto">
            <button type="button" className="p-2 text-gray-400 hover:text-gray-600 hover:bg-gray-100 rounded-full transition-colors">
              <ImageIcon className="w-6 h-6" />
            </button>
            <div className="flex-1 relative">
                <input
                    type="text"
                    value={inputText}
                    onChange={(e) => setInputText(e.target.value)}
                    placeholder={`Message ${activeRoomId === 'general' ? '#General' : '@' + getRoomName()}`}
                    className="w-full bg-gray-100 text-gray-800 placeholder-gray-500 border-0 rounded-full py-3 px-5 focus:ring-2 focus:ring-indigo-500 focus:bg-white transition-all shadow-inner"
                />
            </div>
            <Button 
                type="submit" 
                disabled={!inputText.trim()}
                className={`rounded-full w-12 h-12 p-0 flex items-center justify-center shadow-lg transition-transform active:scale-95 ${!inputText.trim() ? 'opacity-50' : 'hover:scale-105'}`}
            >
              <Send className="w-5 h-5 ml-0.5" />
            </Button>
          </form>
        </div>
      </main>
    </div>
  );
};