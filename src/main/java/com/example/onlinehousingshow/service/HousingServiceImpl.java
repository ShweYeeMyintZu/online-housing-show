package com.example.onlinehousingshow.service;
import com.example.onlinehousingshow.dao.HousingRepository;
import com.example.onlinehousingshow.dao.OwnerRepository;
import com.example.onlinehousingshow.dto.HousingDTO;
import com.example.onlinehousingshow.dto.HousingMapper;
import com.example.onlinehousingshow.model.Housing;
import com.example.onlinehousingshow.model.Owner;
import com.example.onlinehousingshow.security.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.onlinehousingshow.specifications.HousingSpecifications.*;

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
    public HousingDTO deleteHousing(int housingId, HttpHeaders headers, String loginToken) {
        String token = headers.get("Authorization").get(0);
        String jwt = token.replace("Bearer", "");
        String ownerUserName = Jwts.parser().setSigningKey(loginToken).parseClaimsJws(jwt).getBody().getSubject();

        // Retrieve the existing housing by ID
        Housing existingHousing = housingRepository.findById(housingId)
                .orElseThrow(EntityNotFoundException::new);

        // Check if the authenticated owner is the owner of the housing
        if (!existingHousing.getOwner().getOwnerUserName().equals(ownerUserName)) {
            // If the authenticated owner is not the owner of the housing, throw an exception or return an error response
            throw new AccessDeniedException("You are not authorized to delete this housing.");
        }

        housingRepository.delete(existingHousing);

        return HousingDTO.housingData(existingHousing);
    }

    @Override
    public List<HousingDTO> getAllHousing(Optional<String> housingName, Optional<Integer> floors,
                                          Optional<Integer> masterRoom, Optional<Integer> singleRoom,
                                          Optional<Double> amount, Optional<Date> createdDate,
                                          int current, int size){
        var specification = withHousingName(housingName).and(withFloors(floors)).and(withMasterRoom(masterRoom))
                .and(withSingleRoom(singleRoom)).and(withAmount(amount)).and(withCreatedDate(createdDate));
        return housingRepository.findAll(specification, PageRequest.of(current,size)).stream()
                .map(existinghousing -> HousingDTO.housingData(existinghousing)).collect(Collectors.toList());
    }
    @Override
    public List<HousingDTO> getOwnerHousing(HttpHeaders headers, String loginToken,
                                            Optional<String> housingName, Optional<Integer> floors,
                                            Optional<Integer> masterRoom, Optional<Integer> singleRoom,
                                            Optional<Double> amount, Optional<Date> createdDate,
                                            int current, int size){
        String token = headers.get("Authorization").get(0);
        String jwt = token.replace("Bearer","");
        String ownerUserName = Jwts.parser().setSigningKey(loginToken).parseClaimsJws(jwt).getBody().getSubject();

        Specification<Housing> spec = withHousingName(housingName).and(withFloors(floors)).and(withMasterRoom(masterRoom))
                .and(withSingleRoom(singleRoom)).and(withAmount(amount)).and(withCreatedDate(createdDate));

        return housingRepository.findByOwnerUserName(ownerUserName,housingName.orElse(null),floors.orElse(null)
                        ,masterRoom.orElse(null),singleRoom.orElse(null),amount.orElse(null),createdDate.orElse(null), PageRequest.of(current,size))
                .stream().map(existingHousing -> HousingDTO.housingData(existingHousing)).collect(Collectors.toList());
    }


}

