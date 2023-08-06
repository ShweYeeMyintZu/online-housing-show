package com.example.onlinehousingshow.service;

import com.example.onlinehousingshow.model.Owner;
import com.example.onlinehousingshow.service.OwnerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class JpaUserDetailsService  implements UserDetailsService{

    private final OwnerService ownerService;

    public JpaUserDetailsService(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Owner owner = ownerService.getByUsername(username);
        if (owner == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new User(owner.getOwnerUserName(), owner.getPassword(), getAuthorities(owner));

    }

    private Collection<? extends GrantedAuthority> getAuthorities(Owner owner) {
        // Assuming that your Owner entity has a roles field (e.g., Set<String> roles)
        // You can map the roles to SimpleGrantedAuthority or any other representation based on your needs.
        return owner.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}

