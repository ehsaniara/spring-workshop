package com.springworkshop.dealership.service;

import com.springworkshop.dealership.domain.Car;
import com.springworkshop.dealership.domain.CarType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

class CarServiceUnitTest {
    private final CarService carService = new CarService();

    @Test
    void getCarTest() {
        Car tesla = Car.builder().carType(CarType.NEW_CAR).id(1).name("Tesla").build();
        Car ford = Car.builder().carType(CarType.NEW_CAR).id(2).name("Ford").build();
        Assertions.assertEquals(tesla, carService.getCarById(1));
        Assertions.assertEquals(ford, carService.getCarById(2));
    }

    @Test
    void getCar_singleThread_counterIncremented() {
        IntStream.range(0, 99).forEach(i -> carService.getCarById(1));
        Car carById = carService.getCarById(1);
        Assertions.assertEquals(100, carById.getVisitorCounter().get());
    }

    @Test
    void getCar_multiThread_counterIncremented() throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(99999);
        IntStream.range(0, 99999).forEach(i -> {
            Thread myVisitor = new Thread(() ->{
                carService.getCarById(1);
                cdl.countDown();
            });
            myVisitor.start();
        });
        cdl.await();
        Car carById = carService.getCarById(1);
        Assertions.assertEquals(100000, carById.getVisitorCounter().get());
    }
}
