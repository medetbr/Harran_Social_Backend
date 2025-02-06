package com.edu.harran.social.dto.userDto;

import com.edu.harran.social.websocket.entity.AddUserToChatStatus;
import lombok.Data;

@Data
public class UserResponseDto {
    private String name;
    private String surname;
    private String email;
    private String userId;
    private AddUserToChatStatus verificationStatus;
}
