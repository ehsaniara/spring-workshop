package com.springworkshop.dealership.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CarServiceUnitTest {
    private final CarService carService = new CarService();

    @Test
    void getCarTest() {
        Assertions.assertEquals("Tesla", carService.getCar(1));
        Assertions.assertEquals("Ford", carService.getCar(2));
    }
}