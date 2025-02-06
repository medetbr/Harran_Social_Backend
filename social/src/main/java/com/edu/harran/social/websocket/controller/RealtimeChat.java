package com.edu.harran.social.websocket.controller;

import com.edu.harran.social.dto.messageDto.MessageResponseDto;
import com.edu.harran.social.websocket.entity.AddUserToChatStatus;
import com.edu.harran.social.websocket.entity.Chat;
import com.edu.harran.social.websocket.entity.ChatUser;
import com.edu.harran.social.websocket.service.ChatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RealtimeChat {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;
    private final ModelMapper modelMapper;
   /* @MessageMapping("/message/new-group")
    @SendTo("/group/reply")
    public MessageResponseDto newGroup(@Payload MessageResponseDto message){
        simpMessagingTemplate.convertAndSend("/group/new-group"+message.getChatId(),message);
        return message;
    }*/
    @MessageMapping("/message")
    @SendTo("/group/reply")
    @Transactional
    public MessageResponseDto reciveMessage(@Payload MessageResponseDto message){
        Chat chat = chatService.findChatByChatId(message.getChatId());
        for(ChatUser user : chat.getChatUsers()){
            if(user.getVerificationStatus().equals(AddUserToChatStatus.APPROVED)){
                simpMessagingTemplate.convertAndSend("/group/"+user.getUser().getUserId(),message);
            }
        }

        return message;
    }
}
