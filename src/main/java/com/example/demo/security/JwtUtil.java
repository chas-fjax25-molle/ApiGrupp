
package com.example.demo.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final String SECRET = "624938d7fa8990f2a224074ac19adf0782a39e37b50a30b5ead96ac02a0e03d2";
    private final Long expiration = 180000000L;

    public String generateToken(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(generateKey()).compact();
    }

    public boolean validateToken(String token) {
        try{
            extractUsername(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * This function throws if token is invalid
     * @param token
     * @return
     */
    public String extractUsername(String token) {
        var parser = Jwts.parserBuilder().setSigningKey(generateKey()).build();
        return parser.parseClaimsJws(token).getBody().getSubject();
    }
    
    private SecretKey generateKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // TODO: Implementera:
    // - generateToken(username)
    // - validateToken(token)
    // - extractUsername(token)

}
