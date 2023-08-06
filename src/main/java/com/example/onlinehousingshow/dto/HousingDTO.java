package com.example.onlinehousingshow.dto;
import com.example.onlinehousingshow.model.Housing;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HousingDTO {
    private int id;
    private String housingName;
    private String address;
    private int numberOfFloors;
    private int numberOfMasterRoom;
    private int numberOfSingleRoom;
    private double amount;
    private String owner;
    private Date createdDate;
    private Date updatedDate;

    public static HousingDTO housingData(Housing housing){
        return new HousingDTO(housing.getId(),housing.getHousingName(),housing.getAddress(),housing.getNumberOfFloors(),
                housing.getNumberOfMasterRoom(),housing.getNumberOfSingleRoom(),housing.getAmount(),housing.getOwner().getOwnerUserName(),
                housing.getCreatedDate(),housing.getUpdatedDate());
    }
}