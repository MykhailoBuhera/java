package com.example.fleet.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Maintenance {
    private Long id;
    private Long vehicleId;
    private String description;
    private LocalDate date;
    private double cost;
}