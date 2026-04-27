package com.example.fleet.service;

import com.example.fleet.model.Trip;
import com.example.fleet.model.Vehicle;
import com.example.fleet.model.Driver;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TripService {

    private final Map<Long, Trip> storage = new HashMap<>();
    private Long currentId = 1L;

    private final VehicleService vehicleService;
    private final DriverService driverService;

    public TripService(VehicleService vehicleService, DriverService driverService) {
        this.vehicleService = vehicleService;
        this.driverService = driverService;
    }

    public List<Trip> getAll() {
        return new ArrayList<>(storage.values());
    }

    public Trip create(Trip trip) {

        Vehicle vehicle = vehicleService.getById(trip.getVehicleId());
        Driver driver = driverService.getById(trip.getDriverId());

        // 🔥 бізнес логіка
        if (vehicle == null || driver == null) {
            throw new RuntimeException("Driver or Vehicle not found");
        }

        if (!"AVAILABLE".equals(vehicle.getStatus())) {
            throw new RuntimeException("Vehicle is not available");
        }

        // змінюємо статус авто
        vehicle.setStatus("IN_USE");

        trip.setId(currentId++);
        storage.put(trip.getId(), trip);

        return trip;
    }

    public void delete(Long id) {
        storage.remove(id);
    }
}