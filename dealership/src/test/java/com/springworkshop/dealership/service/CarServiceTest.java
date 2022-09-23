package com.springworkshop.dealership.service;

import com.springworkshop.dealership.domain.Car;
import com.springworkshop.dealership.domain.CarType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CarServiceTest {
    private final CarService carService = Mockito.mock(CarService.class);

    Car tesla = Car.builder().carType(CarType.NEW_CAR).id(1).name("Tesla").build();
    Car ford = Car.builder().carType(CarType.NEW_CAR).id(2).name("Ford").build();
    @BeforeEach
    void setUp() {
        Mockito.when(carService.getCarById(1)).thenReturn(tesla);
        Mockito.when(carService.getCarById(2)).thenReturn(ford);
    }

    @Test
    void getCarTest() {
        Assertions.assertEquals(tesla, carService.getCarById(1));
        Assertions.assertEquals(ford, carService.getCarById(2));
    }
}