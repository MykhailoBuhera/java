package com.example.fleet.service;

import com.example.fleet.model.Driver;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DriverService {

    private final Map<Long, Driver> storage = new HashMap<>();
    private Long currentId = 1L;

    public List<Driver> getAll() {
        return new ArrayList<>(storage.values());
    }

    public Driver getById(Long id) {
        return storage.get(id);
    }

    public Driver create(Driver driver) {
        driver.setId(currentId++);
        storage.put(driver.getId(), driver);
        return driver;
    }

    public Driver update(Long id, Driver updated) {
        Driver existing = storage.get(id);
        if (existing == null) return null;

        existing.setName(updated.getName());
        existing.setLicenseNumber(updated.getLicenseNumber());
        existing.setPhone(updated.getPhone());

        return existing;
    }

    public void delete(Long id) {
        storage.remove(id);
    }
}