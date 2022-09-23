package com.springworkshop.dealership.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CarServiceTest {
    private final CarService carService = Mockito.mock(CarService.class);

    @BeforeEach
    void setUp() {
        Mockito.when(carService.getCarById(1)).thenReturn("Tesla");
        Mockito.when(carService.getCarById(2)).thenReturn("Ford");
    }

    @Test
    void getCarTest() {
        Assertions.assertEquals("Tesla", carService.getCarById(1));
        Assertions.assertEquals("Ford", carService.getCarById(2));
    }
}