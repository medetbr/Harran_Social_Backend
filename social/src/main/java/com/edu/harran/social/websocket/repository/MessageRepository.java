package com.edu.harran.social.websocket.repository;

import com.edu.harran.social.websocket.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
   @Query("select m from Message m join m.chat c where c.id=:chatId order by m.createdAt")

//    @Query("SELECT m.id AS messageId, m.content, m.createdAt, d.fileUrl, d.fileName, d.fileSize, d.originalName " +
//            "FROM Message m " +
//            "LEFT JOIN Document d ON m.id = d.message.id " +
//            "WHERE m.chat.id = :chatId " +
//            "ORDER BY m.createdAt")
    List<Message> findByChatId(@Param("chatId") Long chatId); //, Pageable pageable

    @Query("select m from Message m where m.chat.id=:id ORDER BY m.createdAt DESC limit 1")
    Message findLastMessage(Long id);

    Message findByMessageId(String messageId);
}
