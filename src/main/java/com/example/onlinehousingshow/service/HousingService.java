package com.example.onlinehousingshow.service;
import com.example.onlinehousingshow.dto.HousingDTO;
import com.example.onlinehousingshow.dto.HousingMapper;
import com.example.onlinehousingshow.model.Housing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import java.time.LocalDate;
import java.util.Date;

public interface HousingService {

    HousingDTO saveHousing(HousingMapper mapper, HttpHeaders headers, String loginToken);

    HousingDTO updateHousing(int housingId, HousingMapper housingMapper, HttpHeaders headers, String loginToken);

    Page<Housing> getAllHousings(Pageable pageable,
                                 String housingName, Integer floors,
                                 Integer masterRoom, Integer singleRoom,
                                 Double amount, Date createdDate);

    Page<Housing> getHousingsByFilter(String housingName, Integer numberOfFloors,
                                      Integer numberOfMasterRoom, Integer numberOfSingleRoom,
                                      Double amount, Date createdDate, Pageable pageable);


    Page<Housing> getOwnerHousings(int ownerId, Pageable pageable,
                                   String housingName, Integer floors,
                                   Integer masterRoom, Integer singleRoom,
                                   Double amount, Date createdDate);
}


