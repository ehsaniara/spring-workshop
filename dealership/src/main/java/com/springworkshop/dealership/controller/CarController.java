package com.springworkshop.dealership.controller;

import com.springworkshop.dealership.domain.Car;
import com.springworkshop.dealership.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    final String applicationName = "dealership";

    @GetMapping("/{carId}")
    public Car getCar(@PathVariable("carId") int carId) {
        Optional<Car> result = carService.getCarById(carId);
        if (result.isPresent()) {
            return result.get();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
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
        try {
            carService.updateCar(newCar);
        } catch(Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage()
            );
        }
    }

    @PatchMapping("/{carId}/carName/{carName}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateCarName(@PathVariable("carId") int carId, @PathVariable("carName") String carName) {
        try {
            carService.updateCarName(carId, carName);
        } catch(Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage()
            );
        }
    }
}
