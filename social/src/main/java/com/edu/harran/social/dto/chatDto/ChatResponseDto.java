package com.edu.harran.social.dto.chatDto;


import com.edu.harran.social.dto.userDto.UserResponseDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChatResponseDto {
    private String chatName;
    private String chatImage;
    private String chatId;
    private String lastMessage;
    private LocalDateTime createdAt;
    private UserResponseDto user;
    private List<UserResponseDto> users;
    private Integer notification;
    private Boolean isReadAllUser;
    private Boolean isDeliveredAllUser;
}
