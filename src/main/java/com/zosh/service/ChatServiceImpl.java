package com.zosh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zosh.model.Chat;
import com.zosh.repository.ChatRepository;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    // This constructor fixes the "Field injection is not recommended" warning
    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }

//    @Override
//    public Chat addUsersToChat(String chatId, List<String> userIds) throws ChatException {
//        Chat chat = chatRepository.findById(chatId)
//                .orElseThrow(() -> new ChatException("Chat not found with id: " + chatId));
//
//        List<User> usersToAdd = userRepository.findAllById(userIds);
//
//        chat.getUsers().addAll(usersToAdd);
//
//        return chatRepository.save(chat);
//    }

//    @Override
//    public List<Chat> searchChatsByName(String name) throws ChatException {
//        List<Chat> searchedChats = chatRepository.findByProjectNameContainingIgnoreCase(name);
//        if(searchedChats!=null) return searchedChats;
//        throw new ChatException("Chats not available");
//    }

}