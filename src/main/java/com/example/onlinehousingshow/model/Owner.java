package com.example.onlinehousingshow.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "owners")
@Getter
@Setter
public class Owner implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String ownerUserName;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private String ownerEmail;
    @Column(nullable=false)
    private String password;

    @Column(nullable = false)
    private Date createdDate;

    @Column(nullable = false)
    private Date updatedDate;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Housing> housings = new ArrayList<>();
    public Owner() {
    }
    public Owner(String ownerUserName, String ownerName, String ownerEmail,String password) {
        this.ownerUserName = ownerUserName;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.password=password;
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }


    @Override
    public String getUsername() {
        return ownerUserName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
