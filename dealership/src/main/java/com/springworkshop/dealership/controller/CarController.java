package com.springworkshop.dealership.controller;

import com.springworkshop.dealership.domain.Car;
import com.springworkshop.dealership.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    final String applicationName = "dealership";

    @GetMapping("/{carId}")
    public Car getCar(@PathVariable("carId") int carId) {
        return carService.getCarById(carId);
    }

    @GetMapping
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewCar(@RequestBody Car newCar) {
        newCar.setId(null);
        carService.createCar(newCar);
    }

    @PutMapping("/{carId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateCar(@RequestBody Car newCar, @PathVariable("carId") int carId) {
        newCar.setId(carId);
        carService.updateCar(newCar);
    }

    @PatchMapping("/{carId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateCar(@RequestBody Map<String, String> carProperties, @PathVariable("carId") int carId) {
        carService.updateCar(carId, carProperties);
    }

    @PatchMapping("/{carId}/carName/{carName}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateCarName(@PathVariable("carId") int carId, @PathVariable("carName") String carName) {
        carService.updateCarName(carId, carName);
    }
}
