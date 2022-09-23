package com.springworkshop.dealership.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CarServiceUnitTest {
    private final CarService carService = new CarService();

    @Test
    void getCarTest() {
        Assertions.assertEquals("Tesla", carService.getCarById(1));
        Assertions.assertEquals("Ford", carService.getCarById(2));
    }
}