package com.example.fleet.controller;

import com.example.fleet.model.Trip;
import com.example.fleet.service.TripService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripController {

    private final TripService service;

    public TripController(TripService service) {
        this.service = service;
    }

    @GetMapping
    public List<Trip> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Trip create(@RequestBody Trip trip) {
        return service.create(trip);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}