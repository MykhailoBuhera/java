package com.example.fleet.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fleet.model.Vehicle;
import com.example.fleet.service.VehicleService;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    // GET ALL
    @GetMapping
    public List<Vehicle> getAll() {
        return service.getAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Vehicle getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // CREATE
    @PostMapping
    public Vehicle create(@RequestBody Vehicle vehicle) {
        return service.create(vehicle);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Vehicle update(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        return service.update(id, vehicle);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}