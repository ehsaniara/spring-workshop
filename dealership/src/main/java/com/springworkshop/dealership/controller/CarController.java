package com.springworkshop.dealership.controller;

import com.springworkshop.dealership.service.CarService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    final String applicationName = "dealership";

    @GetMapping("/{carId}")
    public String getCar(@PathVariable("carId") int carId){
        return carService.getCarById(carId);
    }
}
