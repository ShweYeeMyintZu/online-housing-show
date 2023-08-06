package com.example.onlinehousingshow.controller;
import com.example.onlinehousingshow.dto.HousingDTO;
import com.example.onlinehousingshow.dto.HousingMapper;
import com.example.onlinehousingshow.model.Housing;
import com.example.onlinehousingshow.model.Owner;
import com.example.onlinehousingshow.service.HousingService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/housings")
public class HousingController {
    @Autowired
    private HousingService housingService;
    @Value("${app.jwtSecret}")
    private String jwtSecret;
    //create
    @PostMapping("/create")
    public HousingDTO saveHousing(@RequestBody @Validated HousingMapper mapper, BindingResult result, @RequestHeader HttpHeaders headers) {
        if (result.hasErrors()){
            throw  new EntityNotFoundException();
        }
        return housingService.saveHousing(mapper,headers,jwtSecret);
    }
    //Update
    @PutMapping("/owner/change/{housingId}")
    public HousingDTO updateHousing(@PathVariable int housingId,
                                                 @RequestBody @Validated HousingMapper mapper, BindingResult result, @RequestHeader HttpHeaders headers) {
        if (result.hasErrors()){
            throw new EntityNotFoundException();
        }
        return housingService.updateHousing(housingId,mapper,headers,jwtSecret);
    }
    //delete
    @PutMapping("/owner/delete/{housingId}")
    public HousingDTO deleteHousing(@PathVariable int housingId, @RequestHeader HttpHeaders headers) {
        return housingService.deleteHousing(housingId, headers, jwtSecret);
    }
    // Private API
    @GetMapping("/owner")
    public List<HousingDTO> GetHousingListByOwnerUserName(@RequestHeader HttpHeaders headers,
                                                @RequestParam Optional<String> housingName,
                                                @RequestParam("numberOfFloors") Optional<Integer> floors,
                                                @RequestParam Optional<Integer> masterRoom,
                                                @RequestParam Optional<Integer> singleRoom,
                                                @RequestParam Optional<Double> amount,
                                                @RequestParam Optional<Date> createdDate,
                                                @RequestParam(required = false,defaultValue = "0")int current,
                                                @RequestParam(required = false,defaultValue = "2")int size) {
        return housingService.getOwnerHousing(headers, jwtSecret, housingName, floors, masterRoom, singleRoom, amount, createdDate, current, size);
    }
        // Public API for visitors
    @GetMapping("/public")
        public List<HousingDTO> findAll(@RequestParam Optional<String> housingName,
                @RequestParam("numberOfFloors") Optional<Integer> floors,
                @RequestParam Optional<Integer> masterRoom,
                @RequestParam Optional<Integer> singleRoom,
                @RequestParam Optional<Double> amount,
                @RequestParam Optional<Date> createdDate,
        @RequestParam(required = false,defaultValue = "0")int current,
        @RequestParam(required = false,defaultValue = "2")int size){
            return housingService.getAllHousing(housingName,floors,masterRoom,singleRoom,amount,createdDate,current,size);
        }


}
