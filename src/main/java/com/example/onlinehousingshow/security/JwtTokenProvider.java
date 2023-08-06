package com.example.onlinehousingshow.security;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.example.onlinehousingshow.model.Owner;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;



    public String generateToken(String username, Set<String> roles) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtExpirationInMs);
        return  Jwts.builder()
                .setSubject(username)
                .claim("roles", roles) // Add roles as a custom claim in the token
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(getSigningKey())
                .compact();

    }

    private Key getSigningKey() {
        byte[] keyBytes= Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token) {
        try {
            // Parse the token and extract the claims using the secure key for verification
            Claims claims = Jwts.parser()
                    .setSigningKey(getSigningKey()) // Use the secure key for verification
                    .parseClaimsJws(token)
                    .getBody();

            // Check if the token is not expired
            Date expirationDate = claims.getExpiration();
            Date currentDate = new Date();
            return expirationDate.after(currentDate);
        } catch (JwtException | IllegalArgumentException e) {
            // Token validation failed
            return false;
        }
    }

}

