package com.edu.harran.social.service;

import com.edu.harran.social.dto.userDto.UserRequestDto;
import com.edu.harran.social.entity.AuthenticationResponse;
import com.edu.harran.social.entity.Student;
import com.edu.harran.social.entity.User;
import com.edu.harran.social.exception.responseException.InternalServerException;
import com.edu.harran.social.repository.StudentRepository;
import com.edu.harran.social.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final JwtService jwtService;
    //private final AuthenticationManager authenticationManager;
/*
    public AuthenticationResponse resgister(User request){
        User user = new User();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserType(user.getUserType());

        user = repository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }
*/
    public AuthenticationResponse login(UserRequestDto request){
        try {
            User user = null;
            if(request.getEmail()!=null){
                user = repository.findByEmail(request.getEmail());
            }
            String token;
            String refreshToken;
            if(user==null) throw new InternalServerException("E-Posta ya da şifre hatalı");
            boolean isPasswordCorrect = passwordEncoder.matches(request.getPassword(),user.getPassword());
            if(isPasswordCorrect){
                token = jwtService.generateToken(user);
                refreshToken = jwtService.generateRefreshToken(user);
                return new AuthenticationResponse(token,refreshToken);
            }else throw new InternalServerException("E-Posta ya da şifre hatalı");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        refreshToken = authHeader.substring(7);
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return null;
        }
        String email = jwtService.extractEmail(refreshToken);

        if (email != null ){
            UserDetails userDetails =  repository.findByEmail(email);
            if(jwtService.isValid(refreshToken,userDetails)){
                User user = repository.findByEmail(email);
                String accessToken = jwtService.generateToken(user);
                return new AuthenticationResponse(accessToken,refreshToken);
            }
        }
        return null;
    }

    public Boolean isTokenValid(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return null;
        }
            String token = authHeader.substring(7);
            String email = jwtService.extractEmail(token);
            if (email != null ) {
                UserDetails userDetails = repository.findByEmail(email);
                return jwtService.isValid(token, userDetails);
            }
        return false;
    }
}
