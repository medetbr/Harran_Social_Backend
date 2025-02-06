package com.edu.harran.social.websocket.controller;

import com.edu.harran.social.dto.chatDto.ChatAllListDto;
import com.edu.harran.social.dto.chatDto.ChatResponseDto;
import com.edu.harran.social.entity.User;
import com.edu.harran.social.request.DepartmentGroupRequest;
import com.edu.harran.social.request.GroupChatRequest;
import com.edu.harran.social.service.UserService;
import com.edu.harran.social.websocket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*",allowedHeaders = "*")

public class ChatController {

    private final ChatService chatService;
    private final UserService userService;

    @PostMapping("/chats/create")
    public ChatResponseDto createGroupHandler(@RequestPart("groupChatRequestDto") GroupChatRequest req,@RequestPart(value = "image", required = false) MultipartFile image){
         return chatService.createGroup(req,image);
    }
    @GetMapping("/chats/{chatId}/profile-img/{fileName}")
    public ResponseEntity<?> getFile(@PathVariable String chatId, @PathVariable String fileName) {
       return chatService.getChatImg(chatId,fileName);
    }
    @PostMapping("/chats/accept-invite/{chatId}")
    public void acceptChatInvitationRequest(@RequestHeader("Authorization") String token,@PathVariable String chatId){
        chatService.acceptChatInvitationRequest(token,chatId);
    }
    @PostMapping("/chats/department-group")
    public void createDepartmentGroupHandler(@RequestBody DepartmentGroupRequest req){
        User reqUser = userService.getUserByUserId(req.getUser_id());
        chatService.createDepartmentGroup(req,reqUser);
    }
    @PutMapping("/chats/department-group/{chatId}/add/{userId}/by/{addedByUserId}")
    public void addUserDepartmentToGroupHandler(@PathVariable String chatId,@PathVariable String userId,@PathVariable String addedByUserId){
        chatService.addUserToDepartmentGroup(chatId,userId,addedByUserId);
    }
    @GetMapping("/admin/chats/all-chats")
    public List<ChatAllListDto> getAllChats(){
        return chatService.findAll();
    }


    @GetMapping("/chats/")
    public List<ChatResponseDto> getChatsByUser(@RequestHeader("Authorization") String token){
        return chatService.findChatsByUser(token);
    }

}