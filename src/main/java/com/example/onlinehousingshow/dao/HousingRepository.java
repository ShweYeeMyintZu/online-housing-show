package com.example.onlinehousingshow.dao;

import com.example.onlinehousingshow.dto.HousingDTO;
import com.example.onlinehousingshow.model.Housing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface HousingRepository extends JpaRepository<Housing, Integer>, JpaSpecificationExecutor<Housing> {
    // Pagination without filtering
    Page<Housing> findAll(Pageable pageable);
    Page<Housing> findAll(Specification<Housing> spec, Pageable pageable);

    // Filtering by housingName
    Page<Housing> findByHousingNameContainingIgnoreCase(String housingName, Pageable pageable);

    // Filtering by floors
    Page<Housing> findByNumberOfFloors(int numberOfFloors, Pageable pageable);

    // Filtering by masterRoom
    Page<Housing> findByNumberOfMasterRoom(int numberOfMasterRoom, Pageable pageable);

    // Filtering by singleRoom
    Page<Housing> findByNumberOfSingleRoom(int numberOfSingleRoom, Pageable pageable);

    // Filtering by amount
    Page<Housing> findByAmount(double amount, Pageable pageable);

    // Filtering by createdDate (postedDate)
    Page<Housing> findByCreatedDate(Date createdDate, Pageable pageable);

    // Custom query method for combined filtering (example: filtering by multiple fields)
    @Query("SELECT h FROM Housing h WHERE (:housingName IS NULL OR h.housingName LIKE %:housingName%) " +
            "AND (:numberOfFloors IS NULL OR h.numberOfFloors = :numberOfFloors) " +
            "AND (:numberOfMasterRoom IS NULL OR h.numberOfMasterRoom = :numberOfMasterRoom) " +
            "AND (:numberOfSingleRoom IS NULL OR h.numberOfSingleRoom = :numberOfSingleRoom) " +
            "AND (:amount IS NULL OR h.amount = :amount) " +
            "AND (:createdDate IS NULL OR h.createdDate = :createdDate)")
    Page<Housing> findByMultipleCriteria(@Param("housingName") String housingName,
                                         @Param("numberOfFloors") Integer numberOfFloors,
                                         @Param("numberOfMasterRoom") Integer numberOfMasterRoom,
                                         @Param("numberOfSingleRoom") Integer numberOfSingleRoom,
                                         @Param("amount") Double amount,
                                         @Param("createdDate") Date createdDate,
                                         Pageable pageable);
}



