package com.edu.harran.social.dto.messageDto;

import com.edu.harran.social.dto.userDto.UserResponseDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponseDto {
    private String content;
    private String messageId;
    private Boolean isMyMessage=false;
    private LocalDateTime createdAt;
    private String chatId;
    private UserResponseDto user;
    private Boolean isReadAllUser;
    private Boolean isDeliveredAllUser;
    private DocumentResponseDto document;
}
