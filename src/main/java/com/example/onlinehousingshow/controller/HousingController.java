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
import java.util.List;

@RestController
@RequestMapping("/api/housings")
public class HousingController {
    @Autowired
    private HousingService housingService;
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @PostMapping("/create")
    public HousingDTO saveHousing(@RequestBody @Validated HousingMapper mapper, BindingResult result, @RequestHeader HttpHeaders headers) {
        if (result.hasErrors()){
            throw  new EntityNotFoundException();
        }
        return housingService.saveHousing(mapper,headers,jwtSecret);
    }



    // Endpoint to update existing housing
    @PutMapping("/owner/change/{housingId}")
    public ResponseEntity<Housing> updateHousing(@PathVariable int housingId,
                                                 @RequestBody HousingDTO housingDTO) {
        Housing updatedHousing = housingService.updateHousing(housingId, housingDTO);
        if (updatedHousing != null) {
            return ResponseEntity.ok(updatedHousing);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // Private API for the owner to view their housing list with pagination and filtering
    @GetMapping("/owner")
    public ResponseEntity<List<Housing>> getOwnerHousings(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "housingName", required = false) String housingName,
            @RequestParam(value = "floors", required = false) Integer floors,
            @RequestParam(value = "masterRoom", required = false) Integer masterRoom,
            @RequestParam(value = "singleRoom", required = false) Integer singleRoom,
            @RequestParam(value = "amount", required = false) Double amount,
            @RequestParam(value = "postedDate", required = false) LocalDate postedDate,
            @AuthenticationPrincipal Owner authenticatedOwner
    ) {
        // Fetch the housing records for the authenticated owner with pagination and filtering
        Page<Housing> ownerHousings = housingService.getOwnerHousings(authenticatedOwner.getId(),
                PageRequest.of(page, size), housingName, floors, masterRoom, singleRoom, amount, postedDate);

        return ResponseEntity.ok(ownerHousings.getContent());
    }
    // Public API for visitors to view all housing list with pagination and search
    @GetMapping("/public")
    public ResponseEntity<Page<Housing>> getAllHousings(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "housingName", required = false) String housingName,
            @RequestParam(value = "floors", required = false) Integer floors,
            @RequestParam(value = "masterRoom", required = false) Integer masterRoom,
            @RequestParam(value = "singleRoom", required = false) Integer singleRoom,
            @RequestParam(value = "amount", required = false) Double amount,
            @RequestParam(value = "postedDate", required = false) LocalDate postedDate
    ) {
        // Fetch all housing records with pagination and filtering
        Page<Housing> allHousings = housingService.getAllHousings(
                PageRequest.of(page, size), housingName, floors, masterRoom, singleRoom, amount, postedDate);

        return ResponseEntity.ok(allHousings);
    }

    private String extractJwtToken(HttpHeaders headers) {
        // Implement your logic to extract the JWT token from the headers
        // For example, if you are using an "Authorization" header with "Bearer <token>", you can do this:
        List<String> authorizationHeaders = headers.get(HttpHeaders.AUTHORIZATION);
        if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
            String authorizationHeader = authorizationHeaders.get(0);
            if (authorizationHeader.startsWith("Bearer ")) {
                return authorizationHeader.substring(7);
            }
        }
        return null;
    }



}
