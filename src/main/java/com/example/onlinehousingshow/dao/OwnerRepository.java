package com.example.onlinehousingshow.dao;



import com.example.onlinehousingshow.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer> {
    boolean existsByOwnerUserName(String ownerUserName);
    boolean existsByOwnerEmail(String ownerEmail);
    Owner findByOwnerUserName(String ownerUserName);
}
