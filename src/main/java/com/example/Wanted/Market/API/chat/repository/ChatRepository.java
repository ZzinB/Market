package com.example.Wanted.Market.API.chat.repository;

import com.example.Wanted.Market.API.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByReceiverId(Long receiverId);
}
