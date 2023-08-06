package com.example.onlinehousingshow.controller;

import com.example.onlinehousingshow.dto.LoginRequest;
import com.example.onlinehousingshow.dto.OwnerDTO;
import com.example.onlinehousingshow.model.Owner;
import com.example.onlinehousingshow.security.JwtTokenProvider;
import com.example.onlinehousingshow.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Set;

@RestController
public class AuthenticationController {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private OwnerService ownerService;

    @PostMapping("/api/auth/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            Owner owner = ownerService.getByUsername(loginRequest.getUsername());

            if (owner == null || !isValidCredentials(owner, loginRequest.getPassword())) {
                throw new BadCredentialsException("Invalid username or password.");
            }
            String username = loginRequest.getUsername();
            Owner owner1=ownerService.findOwnerByUsername(username);
            String token = jwtTokenProvider.generateToken(loginRequest.getUsername(), owner.getRoles());
            System.out.println(token);
            OwnerDTO ownerDto = OwnerDTO.ownerData(owner1, token);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }



    private boolean isValidCredentials(Owner owner, String password) {
        return owner.getPassword().equals(password);
    }

}

