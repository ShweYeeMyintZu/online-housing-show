package com.example.onlinehousingshow.controller;

import com.example.onlinehousingshow.dto.OwnerDTO;
import com.example.onlinehousingshow.model.Owner;
import com.example.onlinehousingshow.service.OwnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Set;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {
    @Autowired
    private OwnerService ownerService;


    @PostMapping
    public ResponseEntity<?> saveOwner(@RequestBody OwnerDTO owner) {
        try {
            Owner savedOwner = ownerService.saveOwner(owner);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            // Handle the duplicate username exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    // Add other endpoints for owner-related operations
}

