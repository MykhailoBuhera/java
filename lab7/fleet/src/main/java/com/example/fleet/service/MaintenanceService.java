package com.example.fleet.service;

import com.example.fleet.model.Maintenance;
import com.example.fleet.model.Vehicle;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MaintenanceService {

    private final Map<Long, Maintenance> storage = new HashMap<>();
    private Long currentId = 1L;

    private final VehicleService vehicleService;

    public MaintenanceService(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public List<Maintenance> getAll() {
        return new ArrayList<>(storage.values());
    }

    public Maintenance create(Maintenance m) {

        Vehicle vehicle = vehicleService.getById(m.getVehicleId());

        if (vehicle == null) {
            throw new RuntimeException("Vehicle not found");
        }

        // 🔥 логіка: авто йде на ремонт
        vehicle.setStatus("MAINTENANCE");

        m.setId(currentId++);
        storage.put(m.getId(), m);

        return m;
    }

    public void delete(Long id) {
        storage.remove(id);
    }
}