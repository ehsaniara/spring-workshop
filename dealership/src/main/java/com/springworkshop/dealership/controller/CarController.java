package com.springworkshop.dealership.controller;

import com.springworkshop.dealership.domain.CarDto;
import com.springworkshop.dealership.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    final String applicationName = "dealership";

    @GetMapping("/{carId}")
    public CarDto getCar(@PathVariable("carId") int carId) {
        return carService.getCarById(carId);
    }

    @GetMapping
    public List<CarDto> getAllCars() {
        return carService.getAllCars();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewCar(@RequestBody CarDto newCarDto) {
        newCarDto.setId(null);
        carService.createCar(newCarDto);
    }

    @PutMapping("/{carId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateCar(@RequestBody CarDto newCarDto, @PathVariable("carId") int carId) {
        newCarDto.setId(carId);
        carService.updateCar(newCarDto);
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
