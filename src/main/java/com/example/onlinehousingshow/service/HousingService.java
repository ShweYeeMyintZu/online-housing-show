package com.example.onlinehousingshow.service;
import com.example.onlinehousingshow.dto.HousingDTO;
import com.example.onlinehousingshow.dto.HousingMapper;
import com.example.onlinehousingshow.model.Housing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface HousingService {

    HousingDTO saveHousing(HousingMapper mapper, HttpHeaders headers, String loginToken);

    HousingDTO updateHousing(int housingId, HousingMapper housingMapper, HttpHeaders headers, String loginToken);

    HousingDTO deleteHousing(int housingId,HttpHeaders headers,String loginToken);



    List<HousingDTO> getAllHousing(Optional<String> housingName, Optional<Integer> floors,
                                   Optional<Integer> masterRoom, Optional<Integer> singleRoom,
                                   Optional<Double> amount, Optional<Date> createdDate,
                                   int current, int size);

    List<HousingDTO> getOwnerHousing(HttpHeaders headers, String loginToken,
                                     Optional<String> housingName, Optional<Integer> floors,
                                     Optional<Integer> masterRoom, Optional<Integer> singleRoom,
                                     Optional<Double> amount, Optional<Date> createdDate,
                                     int current, int size);
}


