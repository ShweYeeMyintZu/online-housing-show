package com.example.onlinehousingshow.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "housings")
public class Housing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String housingName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private int numberOfFloors;

    @Column(nullable = false)
    private int numberOfMasterRoom;

    @Column(nullable = false)
    private int numberOfSingleRoom;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private Date createdDate;

    @Column(nullable = false)
    private Date updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    public Housing() {
    }
    public Housing(String housingName, String address, int numberOfFloors,
                   int numberOfMasterRoom, int numberOfSingleRoom, double amount, Owner owner) {
        this.housingName = housingName;
        this.address = address;
        this.numberOfFloors = numberOfFloors;
        this.numberOfMasterRoom = numberOfMasterRoom;
        this.numberOfSingleRoom = numberOfSingleRoom;
        this.amount = amount;
        this.owner = owner;
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }
}
