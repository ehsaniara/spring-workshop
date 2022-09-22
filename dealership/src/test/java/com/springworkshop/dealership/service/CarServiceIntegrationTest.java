package com.springworkshop.dealership.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CarServiceIntegrationTest {
    @Autowired
    private CarService carService;

    @Test
    void getCarTest() {
        Assertions.assertEquals("Tesla", carService.getCar(1));
        Assertions.assertEquals("Ford", carService.getCar(2));
    }
}