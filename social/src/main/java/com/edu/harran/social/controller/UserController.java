package com.edu.harran.social.controller;

import com.edu.harran.social.dto.userDto.UserResponseDto;
import com.edu.harran.social.entity.User;
import com.edu.harran.social.service.JwtService;
import com.edu.harran.social.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/users")
@CrossOrigin(origins = "*",allowedHeaders = "*")

public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/profile")
    public UserResponseDto getProfile(@RequestHeader("Authorization") String token){
        String email = jwtService.extractEmail(token.substring(7));
        UserResponseDto userDto = new UserResponseDto();
        User user = userService.findUserByEmail(email);
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setSurname(user.getSurname());
        userDto.setUserId(user.getUserId());
        return userDto;
    }

}
