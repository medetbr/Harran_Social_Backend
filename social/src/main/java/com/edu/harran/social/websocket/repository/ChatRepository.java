package com.edu.harran.social.websocket.repository;

import com.edu.harran.social.websocket.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Long> {
    @Query("select c from Chat c join c.chatUsers u where u.user.id =:userId ")
    List<Chat> findChatByUserid(@Param("userId") Long userId);

    Chat findByChatId(String chatId);

    List<Chat> findByDepartmentId(Long id);
}
