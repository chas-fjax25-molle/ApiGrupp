package com.example.demo.service;


import com.example.demo.exception.InvalidLoginException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.LoginDto;
import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;
import com.example.demo.security.JwtUtil;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwrodEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwrodEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String login(LoginDto login) {
        var user = getUser(login.username());

        if (!passwordPasses(user, login.password())) {
            throw new InvalidLoginException("Error: ");
        }

        return generateToken(user);
    }
    
    private String generateToken(User user) {
        return jwtUtil.generateToken(user.getUsername());
    }

    private boolean passwordPasses(User user, String password) {
        return passwrodEncoder.matches(password, user.getPassword());
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }
}
