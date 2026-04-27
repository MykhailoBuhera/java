package com.example.fleet.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Trip {
    private Long id;
    private Long vehicleId;
    private Long driverId;
    private String startLocation;
    private String endLocation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}