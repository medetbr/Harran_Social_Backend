package com.edu.harran.social.websocket.controller;

import com.edu.harran.social.dto.messageDto.DocumentResponseDto;
import com.edu.harran.social.dto.messageDto.MessageResponseDto;
import com.edu.harran.social.dto.userDto.UserResponseDto;
import com.edu.harran.social.entity.User;
import com.edu.harran.social.request.SendMessageRequest;
import com.edu.harran.social.service.JwtService;
import com.edu.harran.social.service.UserService;
import com.edu.harran.social.websocket.entity.Document;
import com.edu.harran.social.websocket.entity.Message;
import com.edu.harran.social.websocket.entity.MessageType;
import com.edu.harran.social.websocket.service.DocumentService;
import com.edu.harran.social.websocket.service.MessageService;
import com.edu.harran.social.websocket.service.MessageStatusService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/api/messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final MessageStatusService messageStatusService;
    private final DocumentService documentService;

    @PostMapping("/create")
    public MessageResponseDto sendMessageHandler(@RequestBody SendMessageRequest req, @RequestHeader("Authorization") String token){
        User user = isHaveUserByToken(token);
        Message message = messageService.sendMessage(req, user, MessageType.TEXT);
        return createResponseDto(message);
    }

    @PostMapping("/chats/create/document/{chatId}")
    public MessageResponseDto createDocument(@PathVariable String chatId,
                                             @RequestPart(value = "document") MultipartFile document,
                                             @RequestPart(value = "content", required = false) String content,
                                             @RequestPart(value = "pageCount", required = false) String page,
                                             @RequestHeader("Authorization") String token){
        User user = isHaveUserByToken(token);
        if(user!=null){
            Message documentMessage = messageService.createDocument(chatId, user, document, content, page);
            MessageResponseDto responseDto = createResponseDto(documentMessage);
            Document documentContent = documentService.getDocumentByMessage(documentMessage);
            responseDto.setDocument(modelMapper.map(documentContent, DocumentResponseDto.class));
            return responseDto;
        }else return null;
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<MessageResponseDto>> getChatsMessagesHandler(
            @PathVariable String chatId,
            @RequestHeader("Authorization") String token
           // @RequestParam(name = "page", required = false, defaultValue = "20") int page
    ){
        User user = isHaveUserByToken(token);
        List<MessageResponseDto> messages = messageService.getMessagesByChatId(chatId,user);
        return new ResponseEntity<List<MessageResponseDto>>(messages, HttpStatus.OK);
    }
    //,@RequestHeader("Authorization") String jwt
    /*
    @DeleteMapping("/{messageId}")
    public void deleteMessagesHandler(@PathVariable Long messageId){
        User user = userService.findUserProfile(jwt);
        messageService.deleteMessage(messageId,user);
    }*/

    private User isHaveUserByToken(String token) {
        String email = jwtService.extractEmail(token.substring(7));
        return userService.findUserByEmail(email);
    }

    private MessageResponseDto createResponseDto(Message message) {
        MessageResponseDto dto = new MessageResponseDto();
        Boolean isDeliveredAllUser = messageStatusService.findIsDeliveredAllUser(message.getId());
        Boolean isReadAllUser = messageStatusService.findIsReadAllUser(message.getId());
        dto.setMessageId(message.getMessageId());
        dto.setUser(modelMapper.map(message.getUser(), UserResponseDto.class));
        dto.setIsDeliveredAllUser(isDeliveredAllUser);
        dto.setIsReadAllUser(isReadAllUser);
        dto.setContent(message.getContent());
        dto.setCreatedAt(message.getCreatedAt());
        dto.setChatId(message.getChat().getChatId());
        return dto;
    }

}
