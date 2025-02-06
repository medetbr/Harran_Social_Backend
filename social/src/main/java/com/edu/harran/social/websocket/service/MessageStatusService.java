package com.edu.harran.social.websocket.service;

import com.edu.harran.social.dto.messageDto.MessageResponseDto;
import com.edu.harran.social.dto.messageDto.MessageStatusDto;
import com.edu.harran.social.dto.userDto.UserResponseDto;
import com.edu.harran.social.entity.User;
import com.edu.harran.social.websocket.entity.Chat;
import com.edu.harran.social.websocket.entity.ChatUser;
import com.edu.harran.social.websocket.entity.Message;
import com.edu.harran.social.websocket.entity.MessageStatus;
import com.edu.harran.social.websocket.repository.MessageRepository;
import com.edu.harran.social.websocket.repository.MessageStatusRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageStatusService {
    private final MessageStatusRepository repository;
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;
    public void insert(List<ChatUser> chatUserList, Message message, User sender){
        List<MessageStatus> messageStatusList = new ArrayList<>();
        for(ChatUser chatUser: chatUserList){
            if (!sender.getUserId().equals(chatUser.getUser().getUserId())){
                MessageStatus messageStatus = new MessageStatus();
                messageStatus.setMessage(message);
                messageStatus.setUser(chatUser.getUser());
                messageStatus.setDeliveredDate(null);
                messageStatus.setReadDate(null);
                messageStatusList.add(messageStatus);
            }
        }
        repository.saveAll(messageStatusList);
    }

    public List<MessageStatusDto> findByMessageId(String messageId) {
        List<MessageStatusDto> messageStatusDtoList = new ArrayList<>();
        Message message = messageRepository.findByMessageId(messageId);
        List<MessageStatus> messageStatusList = repository.findByMessageId(message.getId());
        for(MessageStatus messageStatus : messageStatusList){
            MessageStatusDto dto = new MessageStatusDto();
            dto.setDeliveredDate(messageStatus.getDeliveredDate());
            dto.setReadDate(messageStatus.getReadDate());
            dto.setMessage(modelMapper.map(messageStatus.getMessage(), MessageResponseDto.class));
            dto.setUser(modelMapper.map(messageStatus.getUser(), UserResponseDto.class));
            messageStatusDtoList.add(dto);
        }
        return messageStatusDtoList;
    }
    public void updateMessageDeliveredDate(Long userId,Long messageId){
        MessageStatus messageStatus = repository.findByUserId(userId,messageId);
        if(messageStatus!=null) {
            messageStatus.setDeliveredDate(LocalDateTime.now());
            repository.save(messageStatus);
        }
    }
    @MessageMapping("/message")
    public void updateMessageDeliveredDateRealTime(String userId){
        List<MessageStatus> messageStatus = repository.findByUserIdAndDeliveredDateNull(userId);
        for (MessageStatus status : messageStatus){
            status.setDeliveredDate(LocalDateTime.now());
            repository.save(status);
            String destination = "/topic/status/" + status.getMessage().getMessageId();
            simpMessagingTemplate.convertAndSend(destination, userId);
        }
    }

    @MessageMapping("/message")
    public void updateMessageReadDateRealTime(String userId,Chat chat){
        List<MessageStatus> messageStatus = repository.findByUserIdAndReadDateNull(userId);
        for (MessageStatus status : messageStatus){
            if (status.getMessage().getChat().getId().equals(chat.getId())){
                status.setReadDate(LocalDateTime.now());
                if(status.getDeliveredDate()==null) status.setDeliveredDate(LocalDateTime.now());
                repository.save(status);
                String destination = "/topic/status/" + status.getMessage().getMessageId();
                simpMessagingTemplate.convertAndSend(destination, userId);
            }

        }
    }

    public Integer countUnreadMessageStatus(User user,Chat chat) {
        return repository.findByUser(user.getId(), chat.getId());
    }

    public Boolean findIsDeliveredAllUser(Long messageId){
        return repository.findIsDeliveredAllUser(messageId);
    }

    public Boolean findIsReadAllUser(Long messageId){
        return repository.findIsReadAllUser(messageId);
    }
}
