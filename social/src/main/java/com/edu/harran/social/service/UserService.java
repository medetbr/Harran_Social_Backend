package com.edu.harran.social.service;

import com.edu.harran.social.entity.User;
import com.edu.harran.social.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public User findUserById(Long userId) {
        return userRepository.findUserById(userId);
    }

    public User findUserProfile(String jwt) {
        return null;
    }

    public User getUserByUserId(String userId) {
        return userRepository.findUserByUserId(userId);
    }

    public User findByUserId(String userId) {
        return userRepository.findUserByUserId(userId);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
