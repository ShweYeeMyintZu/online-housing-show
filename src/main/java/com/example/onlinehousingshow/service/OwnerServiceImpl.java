package com.example.onlinehousingshow.service;

import com.example.onlinehousingshow.dao.OwnerRepository;
import com.example.onlinehousingshow.dto.OwnerDTO;
import com.example.onlinehousingshow.model.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

@Service
public class OwnerServiceImpl implements OwnerService {
    @Autowired
    private OwnerRepository ownerRepository;

    @Override
    public Owner saveOwner(OwnerDTO ownerDTO) {

        if (StringUtils.isEmpty(ownerDTO.getOwnerUserName())
                || StringUtils.isEmpty(ownerDTO.getOwnerName())
                || StringUtils.isEmpty(ownerDTO.getOwnerEmail())
                || StringUtils.isEmpty(ownerDTO.getPassword())) {
            throw new IllegalArgumentException("All fields are required.");
        }

        // Validate email format
        if (!isValidEmail(ownerDTO.getOwnerEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        // Check for unique username and email
        if (ownerRepository.existsByOwnerUserName(ownerDTO.getOwnerUserName())) {
            throw new IllegalArgumentException("Username already exists.");
        }
        if (ownerRepository.existsByOwnerEmail(ownerDTO.getOwnerEmail())) {
            throw new IllegalArgumentException("Email already registered.");
        }

        // Validate password strength (example: minimum 8 characters)
        if (ownerDTO.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }
        Owner owner = new Owner(ownerDTO.getOwnerUserName(), ownerDTO.getOwnerName(),
                ownerDTO.getOwnerEmail(),ownerDTO.getPassword());
        Set<String> roles = new HashSet<>();
        roles.add("owner");
        owner.setRoles(roles);
        return ownerRepository.save(owner);
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return email.matches(emailRegex);
    }

    @Override
    public Owner findOwnerByUsername(String ownerUserName) {
        return ownerRepository.findByOwnerUserName(ownerUserName);
    }

    @Override
    public Owner getByUsername(String username) {
        return ownerRepository.findByOwnerUserName(username);
    }
}

