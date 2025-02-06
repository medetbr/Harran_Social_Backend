package com.edu.harran.social.dto.messageDto;

import com.edu.harran.social.dto.userDto.UserResponseDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageStatusDto {
    private MessageResponseDto message;
    private UserResponseDto user;
    private LocalDateTime deliveredDate;
    private LocalDateTime readDate;
}
