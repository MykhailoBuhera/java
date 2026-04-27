package com.example.fleet.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Booking {
    private Long id;
    private Long vehicleId;
    private Long driverId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status; // ACTIVE, CANCELLED, COMPLETED
}