package com.edu.harran.social.websocket.service;

import com.edu.harran.social.dto.chatDto.ChatAllListDto;
import com.edu.harran.social.dto.chatDto.ChatResponseDto;
import com.edu.harran.social.dto.userDto.UserResponseDto;
import com.edu.harran.social.entity.Department;
import com.edu.harran.social.entity.User;
import com.edu.harran.social.request.DepartmentGroupRequest;
import com.edu.harran.social.request.GroupChatRequest;
import com.edu.harran.social.service.DepartmentService;
import com.edu.harran.social.service.FileService;
import com.edu.harran.social.service.JwtService;
import com.edu.harran.social.service.UserService;
import com.edu.harran.social.websocket.entity.AddUserToChatStatus;
import com.edu.harran.social.websocket.entity.Chat;
import com.edu.harran.social.websocket.entity.ChatUser;
import com.edu.harran.social.websocket.entity.Message;
import com.edu.harran.social.websocket.repository.ChatRepository;
import com.edu.harran.social.websocket.repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;
    private final DepartmentService departmentService;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageStatusService messageStatusService;
    private final FileService fileService;

    @MessageMapping("/message/new-group")
    @SendTo("/group/reply")
    @Transactional
    public ChatResponseDto createGroup(GroupChatRequest req, MultipartFile image) {
        Chat group = new Chat();
        ChatUser newChatUser;
        List<ChatUser> chatUsers = new ArrayList<>();

        User admin = userService.findByUserId(req.getAdmin());

        group.setChatImage(req.getChatImage());
        group.setChatName(req.getChatName());
        group.setCreatedBy(admin);
        group.getAdmins().add(admin);

        newChatUser = new ChatUser();
        newChatUser.setUser(admin);
        newChatUser.setChat(group);
        newChatUser.setVerificationStatus(AddUserToChatStatus.APPROVED);
        chatUsers.add(newChatUser);

        group.setDepartment(admin.getStudent().getDepartment());


        for (String userId : req.getUsers()) {
            User user = userService.findByUserId(userId);
            newChatUser = new ChatUser();
            newChatUser.setChat(group);
            newChatUser.setUser(user);
            newChatUser.setVerificationStatus(AddUserToChatStatus.PENDING);
            chatUsers.add(newChatUser);
        }
        group.setChatUsers(chatUsers);
        Chat createdChat = chatRepository.save(group);
        try {
            fileService.createFileForChat(createdChat);
            if (image != null ) {
                String img = fileService.uploadImageForChatProfile(createdChat,image);
                createdChat.setChatImage(img);
                chatRepository.save(createdChat);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ChatResponseDto chatDto = modelMapper.map(createdChat, ChatResponseDto.class);
        List<UserResponseDto> usersDto = new ArrayList<>();
        for (ChatUser user : chatUsers) {
            UserResponseDto usrDto = modelMapper.map(user.getUser(), UserResponseDto.class);
            usrDto.setVerificationStatus(user.getVerificationStatus());
            usersDto.add(usrDto);
        }
        chatDto.setUsers(usersDto);
        chatDto.setUser(modelMapper.map(admin, UserResponseDto.class));

        for (ChatUser chatUser : createdChat.getChatUsers()) {
            simpMessagingTemplate.convertAndSend("/group/new-group/" + chatUser.getUser().getUserId(), chatDto);
        }
        return chatDto;
    }

    public void createDepartmentGroup(DepartmentGroupRequest req, User reqUser) {
        Department department = departmentService.findByDepartmenId(req.getDepartment_id());
        Chat group = new Chat();
        group.setChatImage(req.getChatImage());
        group.setChatName(req.getChatName());
        group.setCreatedBy(reqUser);
        group.setDepartment(department);
        group.getAdmins().add(reqUser);
        chatRepository.save(group);
    }

    public void addUserToDepartmentGroup(String chatId, String reqUserId, String addedByUserId) {
        Chat chat = chatRepository.findByChatId(chatId);
        User addedByUser = userService.findByUserId(addedByUserId);
        User user = userService.findByUserId(reqUserId);
        if (chat != null) {
            if (chat.getAdmins().contains(addedByUser)) {
                ChatUser newChatUser = new ChatUser();
                newChatUser.setChat(chat);
                newChatUser.setUser(user);
                newChatUser.setVerificationStatus(AddUserToChatStatus.APPROVED);
                chat.getChatUsers().add(newChatUser);
                chatRepository.save(chat);
            }
        }
    }

    public List<ChatResponseDto> findChatsByUser(String token) {
        List<ChatResponseDto> chatResponseDtoList = new ArrayList<>();
        String email = jwtService.extractEmail(token.substring(7));
        User user = userService.findUserByEmail(email);
        List<Chat> chats = chatRepository.findChatByUserid(user.getId());
        for (Chat chat : chats) {
            boolean verificationStatusIsPENDING = false;
            ChatResponseDto dto = modelMapper.map(chat, ChatResponseDto.class);
            Message msg = messageRepository.findLastMessage(chat.getId());
            if (msg != null) {
                dto.setLastMessage(msg.getContent());
                dto.setCreatedAt(msg.getCreatedAt());
                UserResponseDto userDto = new UserResponseDto();
                userDto.setUserId(msg.getUser().getUserId());
                userDto.setName(msg.getUser().getName());
                userDto.setEmail(msg.getUser().getEmail());
                userDto.setSurname(msg.getUser().getSurname());
                dto.setUser(userDto);
                dto.setIsReadAllUser(messageStatusService.findIsReadAllUser(msg.getId()));
                dto.setIsDeliveredAllUser(messageStatusService.findIsDeliveredAllUser(msg.getId()));
            }
            List<UserResponseDto> usersDto = new ArrayList<>();
            for (ChatUser usr : chat.getChatUsers()) {
                UserResponseDto usrDto = modelMapper.map(usr.getUser(), UserResponseDto.class);
                usrDto.setVerificationStatus(usr.getVerificationStatus());
                usersDto.add(usrDto);
                if (usr.getVerificationStatus().equals(AddUserToChatStatus.PENDING) && usr.getUser().getUserId().equals(user.getUserId())) {
                    dto.setLastMessage("grup davet isteği");
                    dto.setCreatedAt(usr.getCreatedAt());
                    verificationStatusIsPENDING = true;
                }
            }
            if (!verificationStatusIsPENDING) {
                int count = messageStatusService.countUnreadMessageStatus(user, chat);
                dto.setNotification(count);
            }else {
                dto.setNotification(0);
            }

            dto.setUsers(usersDto);
            chatResponseDtoList.add(dto);
        }
        return chatResponseDtoList;
    }

    public Chat findChatByChatId(String chatId) {
        return chatRepository.findByChatId(chatId);
    }

    public List<ChatAllListDto> findAll() {
        List<Chat> chats = chatRepository.findAll();
        List<ChatAllListDto> chatResponseDtoList = new ArrayList<>();
        for (Chat chat : chats) {
            ChatAllListDto dto = modelMapper.map(chat, ChatAllListDto.class);
            List<UserResponseDto> usersDto = chat.getChatUsers()
                    .stream()
                    .map(user -> modelMapper.map(user.getUser(), UserResponseDto.class))
                    .toList();
            dto.setUsers(usersDto);
            chatResponseDtoList.add(dto);

        }
        return chatResponseDtoList;

    }

    public List<Chat> findChatByDepartmentId(Long id) {
        return chatRepository.findByDepartmentId(id);
    }

    public void acceptChatInvitationRequest(String token, String chatId) {
        String email = jwtService.extractEmail(token.substring(7));
        User user = userService.findUserByEmail(email);
        Chat chat = findChatByChatId(chatId);
        for (ChatUser chatUser : chat.getChatUsers()) {
            if (user.getUserId().equals(chatUser.getUser().getUserId()) && chatUser.getVerificationStatus().equals(AddUserToChatStatus.PENDING)) {
                chatUser.setVerificationStatus(AddUserToChatStatus.APPROVED);
                chatRepository.save(chat);
            }
        }
    }

    public ResponseEntity<?> getChatImg(String chatId, String fileName) {
        return fileService.getChatProfileImg(chatId,fileName);
    }
}
