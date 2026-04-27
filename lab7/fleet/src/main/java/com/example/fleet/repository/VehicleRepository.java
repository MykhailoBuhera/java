package com.example.fleet.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.fleet.model.Vehicle;

@Repository
public class VehicleRepository {

    private final Map<Long, Vehicle> storage = new HashMap<>();

    public List<Vehicle> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Vehicle findById(Long id) {
        return storage.get(id);
    }

    public void save(Vehicle vehicle) {
        storage.put(vehicle.getId(), vehicle);
    }

    public void delete(Long id) {
        storage.remove(id);
    }
}