package com.edu.harran.social.controller;

import com.edu.harran.social.dto.userDto.UserRequestDto;
import com.edu.harran.social.entity.AuthenticationResponse;
import com.edu.harran.social.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/auth/")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class AuthenticationController {

    private final AuthenticationService authService;


/*
    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody User request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }
*/
    @PostMapping("login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody UserRequestDto request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }
    @PostMapping("refresh-token")
    public AuthenticationResponse refreshToken(
            HttpServletRequest request, HttpServletResponse response
            ) {
        return authService.refreshToken(request,response);
    }
    @PostMapping("is-token-valid")
    public Boolean isTokenValid(
            HttpServletRequest request, HttpServletResponse response
    ) {
        return authService.isTokenValid(request,response);
    }

}
