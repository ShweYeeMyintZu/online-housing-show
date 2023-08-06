package com.example.onlinehousingshow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OwnerMapper {
    private String ownerUserName;
    private String ownerName;
    private String ownerEmail;
    private String password;

    public OwnerMapper(String ownerUserName, String ownerName, String ownerEmail, String password) {
        this.ownerUserName = ownerUserName;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.password = password;
    }
}
