package com.example.fleet.model;

import lombok.Data;

@Data
public class Vehicle {
    private Long id;
    private String brand;
    private String model;
    private String licensePlate;
    private String status;
}