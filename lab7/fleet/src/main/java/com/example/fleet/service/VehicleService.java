package com.example.fleet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.fleet.model.Vehicle;
import com.example.fleet.repository.VehicleRepository;

@Service
public class VehicleService {

    private final VehicleRepository repository;
    private Long currentId = 1L;

    public VehicleService(VehicleRepository repository) {
        this.repository = repository;
    }

    public List<Vehicle> getAll() {
        return repository.findAll();
    }

    public Vehicle getById(Long id) {
        return repository.findById(id);
    }

    public Vehicle create(Vehicle vehicle) {
        vehicle.setId(currentId++);
        repository.save(vehicle);
        return vehicle;
    }

    public Vehicle update(Long id, Vehicle updated) {
        Vehicle existing = repository.findById(id);
        if (existing == null) return null;

        existing.setBrand(updated.getBrand());
        existing.setModel(updated.getModel());
        existing.setLicensePlate(updated.getLicensePlate());
        existing.setStatus(updated.getStatus());

        repository.save(existing);

        return existing;
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}