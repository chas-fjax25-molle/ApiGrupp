
package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

import com.example.demo.model.LoginDto;
import com.example.demo.service.AuthService;

@RestController
public class AuthController {
    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto login) {
        return authService.login(login);
    }

}
