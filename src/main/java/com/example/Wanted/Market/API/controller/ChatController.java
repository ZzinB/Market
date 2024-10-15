package com.example.Wanted.Market.API.controller;

import com.example.Wanted.Market.API.domain.Chat;
import com.example.Wanted.Market.API.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    //메시지 전송
    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody Chat chat){
        chatService.sendMessage(chat);
        return ResponseEntity.ok().build();
    }

    //사용자의 채팅 기록 조회
    @GetMapping("/history/{receiverId}")
    public ResponseEntity<List<Chat>> getChatHistory(@PathVariable Long receiverId){
        List<Chat> chatHistory = chatService.getChatHistory(receiverId);
        return ResponseEntity.ok(chatHistory);
    }
}
