package com.edu.harran.social.websocket.repository;

import com.edu.harran.social.entity.User;
import com.edu.harran.social.websocket.entity.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageStatusRepository extends JpaRepository<MessageStatus,Long> {
    List<MessageStatus> findByMessageId(Long messageId);

    @Query("select m from MessageStatus m where m.user.id=:userId and m.message.id=:messageId")
    MessageStatus findByUserId(@Param("userId") Long userId,@Param("messageId") Long messageId);

    @Query("select m from MessageStatus m where m.user.userId=:userId and m.deliveredDate is null")
    List<MessageStatus> findByUserIdAndDeliveredDateNull(@Param("userId") String userId);

    @Query("select m from MessageStatus m where m.user.userId=:userId and m.readDate is null")
    List<MessageStatus> findByUserIdAndReadDateNull(@Param("userId") String userId);

    @Query("select count(m) from MessageStatus m where m.user.id=:id and m.message.chat.id=:chatId and m.readDate is null")
    Integer findByUser(@Param("id")Long id, @Param("chatId")Long chatId);

    @Query("SELECT CASE WHEN COUNT(m) < 1 THEN TRUE ELSE FALSE END FROM MessageStatus m where m.message.id=:messageId and m.deliveredDate is null")
    Boolean findIsDeliveredAllUser(@Param("messageId") Long messageId);

    @Query("SELECT CASE WHEN COUNT(m) < 1 THEN TRUE ELSE FALSE END FROM MessageStatus m WHERE m.message.id = :messageId AND m.readDate IS NULL")
    Boolean findIsReadAllUser(@Param("messageId") Long messageId);
}
