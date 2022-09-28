package com.springworkshop.dealership.service;

import com.springworkshop.dealership.domain.Car;
import com.springworkshop.dealership.domain.CarType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

class CarServiceTest {
    private final CarService carService = new CarService();

    @BeforeEach
    void setUp() {
        Car tesla = Car.builder().carType(CarType.NEW_CAR).name("Tesla").build();
        carService.createNewCar(tesla);
        Car ford = Car.builder().carType(CarType.NEW_CAR).name("Ford").build();
        carService.createNewCar(ford);
    }

    @Test
    void getCarTest() {
        Car teslaExpected = Car.builder().carType(CarType.NEW_CAR).id(1).name("Tesla").build();
        Car fordExpected = Car.builder().carType(CarType.NEW_CAR).id(2).name("Ford").build();
        Assertions.assertEquals(teslaExpected, carService.getCarById(1));
        Assertions.assertEquals(fordExpected, carService.getCarById(2));
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

    @Test
    void getCar_multiThread_pool_threadExecutor_counterIncremented() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        IntStream.range(0, 99999).forEach(i -> {
            executorService.execute(new Thread(() ->{
                carService.getCarById(1);
            }));
        });
        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(10, TimeUnit.SECONDS);
        Assertions.assertTrue(terminated);
        Car carById = carService.getCarById(1);
        Assertions.assertEquals(100000, carById.getVisitorCounter().get());
    }

    @Test
    void getCar_multiThread_pool_callable_counterIncremented() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        List<Callable<Object>> callables = new ArrayList<>(99999);
        IntStream.range(0, 99999).forEach(i -> {
            callables.add(Executors.callable(() -> {
                carService.getCarById(1);
            }));
        });
        executorService.invokeAll(callables);
        Car carById = carService.getCarById(1);
        Assertions.assertEquals(100000, carById.getVisitorCounter().get());
    }

    @Test
    void createNewCar() {
        Car toyota = Car.builder().carType(CarType.NEW_CAR).name("Toyota").build();
        carService.createNewCar(toyota);
        Car toyotaExpected = Car.builder().carType(CarType.NEW_CAR).id(3).name("Toyota").build();
        Assertions.assertEquals(toyotaExpected, carService.getCarById(3));
    }
}
