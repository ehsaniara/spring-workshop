package com.springworkshop.dealership.service;

import com.springworkshop.dealership.domain.Car;
import com.springworkshop.dealership.domain.CarType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CarServiceIntegrationTest {
    @Autowired
    private CarService carService;

    @BeforeEach
    void setUp() {
        Car tesla = Car.builder().carType(CarType.NEW_CAR).name("Tesla").build();
        carService.createOrUpdateCar(tesla);
        Car ford = Car.builder().carType(CarType.NEW_CAR).name("Ford").build();
        carService.createOrUpdateCar(ford);
    }

    @Test
    void getCarByIdTest() {
        Car tesla = Car.builder().carType(CarType.NEW_CAR).id(1).name("Tesla").build();
        Car ford = Car.builder().carType(CarType.NEW_CAR).id(2).name("Ford").build();

        Assertions.assertEquals(tesla, carService.getCarById(1).orElseThrow());
        Assertions.assertEquals(ford, carService.getCarById(2).orElseThrow());
    }
}
