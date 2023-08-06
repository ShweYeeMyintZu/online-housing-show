package com.example.onlinehousingshow.service;
import com.example.onlinehousingshow.dao.HousingRepository;
import com.example.onlinehousingshow.dao.OwnerRepository;
import com.example.onlinehousingshow.dto.HousingDTO;
import com.example.onlinehousingshow.dto.HousingMapper;
import com.example.onlinehousingshow.model.Housing;
import com.example.onlinehousingshow.model.Owner;
import com.example.onlinehousingshow.security.JwtTokenProvider;
import com.example.onlinehousingshow.specifications.HousingSpecifications;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;

@Service
public class HousingServiceImpl implements HousingService {
    @Autowired
    private HousingRepository housingRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public HousingDTO saveHousing(HousingMapper mapper, HttpHeaders headers, String loginToken) {
//        Getting username from token
        String token = headers.get("Authorization").get(0);
        String jwt = token.replace("Bearer", "");
        String ownerUserName = Jwts.parser().setSigningKey(loginToken).parseClaimsJws(jwt).getBody().getSubject();

//        Saving housing data
        Housing housing = new Housing();
        housing.setHousingName(mapper.getHousingName());
        housing.setAddress(mapper.getAddress());
        housing.setNumberOfFloors(mapper.getNumberOfFloors());
        housing.setNumberOfMasterRoom(mapper.getNumberOfMasterRoom());
        housing.setNumberOfSingleRoom(mapper.getNumberOfSingleRoom());

        housing.setAmount(mapper.getAmount());
        Owner owner = ownerRepository.findByOwnerUserName(ownerUserName);
        housing.setOwner(owner);
        housing.setCreatedDate(new Date());
        housing.setUpdatedDate(new Date());

        housingRepository.save(housing);
        return HousingDTO.housingData(housing);

        }

    @Override
    public HousingDTO updateHousing(int housingId, HousingMapper housingMapper, HttpHeaders headers, String loginToken) {
        String token = headers.get("Authorization").get(0);
        String jwt = token.replace("Bearer","");
        String ownerUserName = Jwts.parser().setSigningKey(loginToken).parseClaimsJws(jwt).getBody().getSubject();

        return HousingDTO.housingData(housingRepository.findById(housingId).map(existingHouse->{
            existingHouse.setHousingName(housingMapper.getHousingName());
            existingHouse.setAddress(housingMapper.getAddress());
            existingHouse.setNumberOfFloors(housingMapper.getNumberOfFloors());
            existingHouse.setNumberOfMasterRoom(housingMapper.getNumberOfMasterRoom());
            existingHouse.setNumberOfSingleRoom(housingMapper.getNumberOfSingleRoom());
            existingHouse.setAmount(housingMapper.getAmount());
            Owner owner = ownerRepository.findByOwnerUserName(ownerUserName);
            existingHouse.setOwner(owner);
            existingHouse.setUpdatedDate(new Date());
            return existingHouse;
        }).orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public Page<Housing> getAllHousings(Pageable pageable,
                                        String housingName, Integer floors,
                                        Integer masterRoom, Integer singleRoom,
                                        Double amount, Date createdDate) {
        Specification<Housing> spec = Specification.where(HousingSpecifications.withHousingName(housingName))
                .and(HousingSpecifications.withFloors(floors))
                .and(HousingSpecifications.withMasterRoom(masterRoom))
                .and(HousingSpecifications.withSingleRoom(singleRoom))
                .and(HousingSpecifications.withAmount(amount))
                .and(HousingSpecifications.withCreatedDate(createdDate));

        // Use the custom findAll method with specifications and pagination
        return housingRepository.findAll(spec, pageable);
    }



    @Override
    public Page<Housing> getHousingsByFilter(String housingName, Integer numberOfFloors, Integer numberOfMasterRoom, Integer numberOfSingleRoom, Double amount, Date createdDate, Pageable pageable) {
        if (housingName != null) {
            return housingRepository.findByHousingNameContainingIgnoreCase(housingName, pageable);
        } else if (numberOfFloors != null) {
            return housingRepository.findByNumberOfFloors(numberOfFloors, pageable);
        } else if (numberOfMasterRoom != null) {
            return housingRepository.findByNumberOfMasterRoom(numberOfMasterRoom, pageable);
        } else if (numberOfSingleRoom != null) {
            return housingRepository.findByNumberOfSingleRoom(numberOfSingleRoom, pageable);
        } else if (amount != null) {
            return housingRepository.findByAmount(amount, pageable);
        } else if (createdDate != null) {
            return housingRepository.findByCreatedDate(createdDate, pageable);
        } else {
            // If no filter is provided, retrieve all housings with pagination
            return housingRepository.findAll(pageable);
        }
    }


    @Override
    public Page<Housing> getOwnerHousings(int ownerId, Pageable pageable,
                                          String housingName, Integer floors,
                                          Integer masterRoom, Integer singleRoom,
                                          Double amount, Date createdDate) {
        Specification<Housing> spec = Specification.where(HousingSpecifications.forOwner(ownerId))
                .and(HousingSpecifications.withHousingName(housingName))
                .and(HousingSpecifications.withFloors(floors))
                .and(HousingSpecifications.withMasterRoom(masterRoom))
                .and(HousingSpecifications.withSingleRoom(singleRoom))
                .and(HousingSpecifications.withAmount(amount))
                .and(HousingSpecifications.withCreatedDate(createdDate));

        return housingRepository.findAll(spec, pageable);
    }


}

