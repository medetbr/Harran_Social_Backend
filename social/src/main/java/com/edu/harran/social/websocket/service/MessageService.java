package com.edu.harran.social.websocket.service;

import com.edu.harran.social.config.WebSocketEventListener;
import com.edu.harran.social.dto.messageDto.DocumentResponseDto;
import com.edu.harran.social.dto.messageDto.MessageResponseDto;
import com.edu.harran.social.entity.User;
import com.edu.harran.social.request.SendMessageRequest;
import com.edu.harran.social.websocket.entity.*;
import com.edu.harran.social.websocket.repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final ModelMapper modelMapper;
    private final MessageStatusService messageStatusService;
    private final DocumentService documentService;

    public Message sendMessage(SendMessageRequest req, User user,MessageType type) {
        req.setContent(req.getContent().trim());
        if (req.getContent().isEmpty()) return null;
        Chat chat = chatService.findChatByChatId(req.getChatId());
        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setMessageType(type);
        message.setContent(req.getContent());
        Message msg = messageRepository.save(message);

        messageStatusService.insert(chat.getChatUsers(), msg, user);
        for (ChatUser usr : chat.getChatUsers()) {
            if (
                    WebSocketEventListener.activeSessions.containsValue(usr.getUser().getUserId()) &&
                            !usr.getUser().getId().equals(msg.getUser().getId())
                            && usr.getVerificationStatus().equals(AddUserToChatStatus.APPROVED)) {
                messageStatusService.updateMessageDeliveredDate(usr.getUser().getId(), msg.getId());
            }
        }
        return msg;
    }

    public List<MessageResponseDto> getMessagesByChatId(String chatId, User reqUser) {
        Chat chat = chatService.findChatByChatId(chatId);
        List<MessageResponseDto> messageResponseList = new ArrayList<>();

        for (ChatUser chatUser : chat.getChatUsers()) {
            if (chatUser.getUser().getUserId().equals(reqUser.getUserId()) &&
                    !chatUser.getVerificationStatus().equals(AddUserToChatStatus.APPROVED)
            ) {
                return messageResponseList;
            }
        }
        //Pageable pageable = PageRequest.of(page, 20);
        List<Message> messages = messageRepository.findByChatId(chat.getId()); //pageable

        for (Message msg : messages) {

            MessageResponseDto dto = modelMapper.map(msg, MessageResponseDto.class);
            if(msg.getMessageType().equals(MessageType.DOCUMENT)){
                Document document = documentService.getDocumentByMessage(msg);
                DocumentResponseDto documentResponseDto = modelMapper.map(document,DocumentResponseDto.class);
                dto.setDocument(documentResponseDto);
            }

            if (reqUser.getId().equals(msg.getUser().getId())) {
                dto.setIsMyMessage(true);
                dto.setIsReadAllUser(messageStatusService.findIsReadAllUser(msg.getId()));
                dto.setIsDeliveredAllUser(messageStatusService.findIsDeliveredAllUser(msg.getId()));
            }
            dto.setChatId(chat.getChatId());
            messageResponseList.add(dto);
        }
        messageStatusService.updateMessageReadDateRealTime(reqUser.getUserId(), chat);
        return messageResponseList;
    }

    @Transactional
    public Message createDocument(String chatId,User user, MultipartFile document,String content,String page) {
        Chat chat = chatService.findChatByChatId(chatId);
        try {
            if(chat!=null) {
                SendMessageRequest createMessageDto = new SendMessageRequest();
                createMessageDto.setContent(content==null?document.getOriginalFilename():content);
                createMessageDto.setChatId(chatId);
                Message createdMessage = sendMessage(createMessageDto, user,MessageType.DOCUMENT);
                documentService.createDocument(createdMessage,document,page);
                return createdMessage;
            }else return null;
        }catch (Exception e){
            throw new RuntimeException("Error creating document", e);
        }
    }
}
