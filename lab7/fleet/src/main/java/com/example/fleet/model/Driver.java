package com.example.fleet.model;

import lombok.Data;

@Data
public class Driver {
    private Long id;
    private String name;
    private String licenseNumber;
    private String phone;
}