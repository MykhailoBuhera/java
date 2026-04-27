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

import com.example.fleet.model.Driver;
import com.example.fleet.service.DriverService;

@RestController
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService service;

    public DriverController(DriverService service) {
        this.service = service;
    }

    @GetMapping
    public List<Driver> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Driver getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Driver create(@RequestBody Driver driver) {
        return service.create(driver);
    }

    @PutMapping("/{id}")
    public Driver update(@PathVariable Long id, @RequestBody Driver driver) {
        return service.update(id, driver);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}