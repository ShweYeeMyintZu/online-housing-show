package com.example.onlinehousingshow.dto;

import com.example.onlinehousingshow.model.Owner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDTO {
    private int id;
    private String ownerUserName;
    private String ownerName;
    private String ownerEmail;
    private String password;

    private Date createdDate;
    private Date updatedDate;
    String jwtToken;

    public static OwnerDTO ownerData(Owner owner, String jwtToken){
        return new OwnerDTO(owner.getId(),owner.getOwnerUserName(),owner.getOwnerName(),owner.getOwnerEmail(),owner.getPassword()
                ,owner.getCreatedDate(),owner.getUpdatedDate(),jwtToken);
    }
}