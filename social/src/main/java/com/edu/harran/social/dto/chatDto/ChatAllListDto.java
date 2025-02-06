package com.edu.harran.social.dto.chatDto;

import com.edu.harran.social.dto.userDto.UserResponseDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChatAllListDto {
    private String chatName;
    private String chatImage;
    private String chatId;
    private LocalDateTime createdAt;
    private List<UserResponseDto> users;
}
