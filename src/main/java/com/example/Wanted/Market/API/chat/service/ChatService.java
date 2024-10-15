package com.example.Wanted.Market.API.chat.service;

import com.example.Wanted.Market.API.chat.domain.Chat;
import com.example.Wanted.Market.API.chat.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    //메시지 전송
    public void sendMessage(Chat chat){
        chatRepository.save(chat);
    }

    //채팅 기록 조회
    public List<Chat> getChatHistory(Long receiverId){
        return chatRepository.findByReceiverId(receiverId);
    }
}
