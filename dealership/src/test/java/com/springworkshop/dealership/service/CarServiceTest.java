package com.springworkshop.dealership.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

class CarServiceTest {
    private final CarService carService = Mockito.mock(CarService.class);

    @BeforeEach
    void setUp() {
        Mockito.when(carService.getCar(1)).thenReturn("Tesla");
        Mockito.when(carService.getCar(2)).thenReturn("Ford");
    }

    @Test
    void getCarTest() {
        Assertions.assertEquals("Tesla", carService.getCar(1));
        Assertions.assertEquals("Ford", carService.getCar(2));
    }
}