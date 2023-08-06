package com.example.onlinehousingshow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HousingMapper {
    private String housingName;
    private String address;
    private int numberOfFloors;
    private int numberOfMasterRoom;
    private int numberOfSingleRoom;
    private int amount;
    private int owner;
}
