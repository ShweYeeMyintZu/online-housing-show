package com.example.onlinehousingshow.service;

import com.example.onlinehousingshow.dto.OwnerDTO;
import com.example.onlinehousingshow.model.Owner;

public interface OwnerService {
    Owner saveOwner(OwnerDTO ownerDTO);
    Owner findOwnerByUsername(String ownerUserName);

    Owner getByUsername(String username);
}

