package com.example.Wanted.Market.API.repository;

import com.example.Wanted.Market.API.domain.Chat;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByReceiverId(Long receiverId);
}
