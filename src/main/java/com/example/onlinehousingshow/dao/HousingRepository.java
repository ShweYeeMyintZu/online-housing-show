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
import java.util.List;

@Repository
public interface HousingRepository extends JpaRepository<Housing, Integer>, JpaSpecificationExecutor<Housing> {

    @Query("SELECT h FROM Housing h WHERE h.owner.ownerUserName = :ownerUserName "+
            "AND (:housingName IS NULL OR h.housingName LIKE %:housingName%) " +
            "AND (:numberOfFloors IS NULL OR h.numberOfFloors = :numberOfFloors OR (:floors = 0 AND h.numberOfFloors IS NULL))  " +
            "AND (:numberOfMasterRoom IS NULL OR h.numberOfMasterRoom = :numberOfMasterRoom) " +
            "AND (:numberOfSingleRoom IS NULL OR h.numberOfSingleRoom = :numberOfSingleRoom) " +
            "AND (:amount IS NULL OR h.amount = :amount) " +
            "AND (:createdDate IS NULL OR h.createdDate = :createdDate)")
    List<Housing> findByOwnerUserName(String ownerUserName,String housingName,Integer numberOfFloors,Integer numberOfMasterRoom, Integer numberOfSingleRoom,
                                     Double amount,Date createdDate,
                                      Pageable pageable);
}



