package com.springworkshop.dealership.controller;

import com.springworkshop.dealership.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cars")
@AllArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping("/{carId}")
    public String getCar(@PathVariable("carId") int carId){
        return carService.getCar(carId);
    }
}
