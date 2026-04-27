package com.example.fleet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.fleet.model.Booking;
import com.example.fleet.model.Driver;
import com.example.fleet.model.Vehicle;

@Service
public class BookingService {

    private final Map<Long, Booking> storage = new HashMap<>();
    private Long currentId = 1L;

    private final VehicleService vehicleService;
    private final DriverService driverService;

    public BookingService(VehicleService vehicleService, DriverService driverService) {
        this.vehicleService = vehicleService;
        this.driverService = driverService;
    }

    public List<Booking> getAll() {
        return new ArrayList<>(storage.values());
    }

    public Booking create(Booking b) {

        Vehicle vehicle = vehicleService.getById(b.getVehicleId());
        Driver driver = driverService.getById(b.getDriverId());

        if (vehicle == null || driver == null) {
            throw new RuntimeException("Driver or Vehicle not found");
        }

        if (!"AVAILABLE".equals(vehicle.getStatus())) {
            throw new RuntimeException("Vehicle not available");
        }

        // 🔥 бронюємо авто
        vehicle.setStatus("IN_USE");

        b.setId(currentId++);
        b.setStatus("ACTIVE");

        storage.put(b.getId(), b);

        return b;
    }

    public Booking cancel(Long id) {
        Booking booking = storage.get(id);

        if (booking == null) return null;

        booking.setStatus("CANCELLED");

        Vehicle vehicle = vehicleService.getById(booking.getVehicleId());
        if (vehicle != null) {
            vehicle.setStatus("AVAILABLE");
        }

        return booking;
    }

    public void delete(Long id) {
        storage.remove(id);
    }
}