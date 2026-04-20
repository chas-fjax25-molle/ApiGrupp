package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }
    
    @Test
    void getPrivate_withoutToken_returnsForbidden() throws Exception {
        mockMvc.perform(get("/private")).andExpect(status().isForbidden());
    }

    @Test
    void postMessages_withoutToken_returnsForbidden() throws Exception {
        mockMvc.perform(post("/messages").contentType(MediaType.APPLICATION_JSON)
            .content("{\"content\": \"Hej:)\"}")).andExpect(status().isForbidden());
    }

    @Test
    void getPrivate_withInvalidToken_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/private").header("Authorization", "Bearer invalidToken"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void getPrivate_withExpiredToken_returnsForbidden() throws Exception {
        // TODO: In real cases this should be read in from environment or similar
        String secret = "624938d7fa8990f2a224074ac19adf0782a39e37b50a30b5ead96ac02a0e03d2!";
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        String expiredToken = Jwts.builder()
            .setSubject("testuser")
            .setIssuedAt(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2)))
            .setExpiration(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)))
            .signWith(key)
            .compact();

        mockMvc.perform(get("/private")
                .header("Authorization", "Bearer " + expiredToken))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void login_withValidCredentials_returnsToken() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user);
        
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\": \"testuser\", \"password\": \"password\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.token").exists());
    }

    @Test
    void getPrivate_withValidToken_returns200() throws Exception {

    }

    @Test
    void postMessages_withValidToken_returnsSuccess() throws Exception {

    }

    @Test
    void getPublic_withoutToken_returns200() throws Exception {

    }
}
