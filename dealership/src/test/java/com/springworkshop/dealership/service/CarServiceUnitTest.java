package com.springworkshop.dealership.service;

import com.springworkshop.dealership.domain.Car;
import com.springworkshop.dealership.domain.CarType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CarServiceUnitTest {
    private final CarService carService = new CarService();

    @Test
    void getCarTest() {
        Car tesla = Car.builder().carType(CarType.NEW_CAR).id(1).name("Tesla").build();
        Car ford = Car.builder().carType(CarType.NEW_CAR).id(2).name("Ford").build();
        Assertions.assertEquals(tesla, carService.getCarById(1));
        Assertions.assertEquals(ford, carService.getCarById(2));
    }
}