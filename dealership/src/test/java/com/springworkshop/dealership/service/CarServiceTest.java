package com.springworkshop.dealership.service;

import com.springworkshop.dealership.domain.Car;
import com.springworkshop.dealership.domain.CarEntity;
import com.springworkshop.dealership.domain.CarRepository;
import com.springworkshop.dealership.domain.CarType;
import com.springworkshop.dealership.mapper.CarMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.IntStream;

class CarServiceTest {

    private final CarRepository carRepository = Mockito.mock(CarRepository.class);
    private final CarMapper carMapper = Mockito.mock(CarMapper.class);
    private final CarService carService = new CarService(carRepository, carMapper);

    Car tesla = Car.builder().carType(CarType.NEW_CAR).id(1).name("Tesla").build();
    CarEntity teslaEntity = CarEntity.builder().carType(CarType.NEW_CAR).id(1).name("Tesla").build();
    Car ford = Car.builder().carType(CarType.NEW_CAR).id(2).name("Ford").build();
    CarEntity fordEntity = CarEntity.builder().carType(CarType.NEW_CAR).id(2).name("Ford").build();

    Car toyotaBefore = Car.builder().carType(CarType.NEW_CAR).name("Toyota").build();

    @BeforeEach
    void setUp() {
        //init
        Mockito.when(carMapper.toCarDto(teslaEntity)).thenReturn(tesla);
        Mockito.when(carMapper.toCarDto(fordEntity)).thenReturn(ford);

        Mockito.when(carRepository.findById(1)).thenReturn(Optional.of(teslaEntity));
        Mockito.when(carRepository.findById(2)).thenReturn(Optional.of(fordEntity));
//        carService.createOrUpdateCar(tesla);
//        carService.createOrUpdateCar(ford);

//        Mockito.when(carRepository.save(Mockito.any()))
//                .thenReturn(CarEntity.builder().id(1).carType(CarType.NEW_CAR).name("Tesla").build())
//                .thenReturn(CarEntity.builder().id(2).carType(CarType.NEW_CAR).name("Ford").build());
    }

    @Test
    void getCarTest() {
        Assertions.assertEquals(tesla, carService.getCarById(1).orElseThrow());
        Assertions.assertEquals(ford, carService.getCarById(2).orElseThrow());
    }

    @Test
    void getCarTest_notExist() {
        Assertions.assertTrue(carService.getCarById(3).isEmpty());
    }

    @Test
    void getCar_singleThread_counterIncremented() {
        IntStream.range(0, 99).forEach(i -> carService.getCarById(1));
        Assertions.assertEquals(99, carService.getCarVisitors(1));
    }

    @Test
    void getCar_multiThread_counterIncremented() throws InterruptedException {
        int threadNums = 99_999;
        CountDownLatch cdl = new CountDownLatch(threadNums);
        IntStream.range(0, threadNums).forEach(i -> {
            Thread myVisitor = new Thread(() -> {
                carService.getCarById(1);
                cdl.countDown();
            });
            myVisitor.start();
        });
        cdl.await();
        Assertions.assertEquals(threadNums, carService.getCarVisitors(1));
    }

    @Test
    void getCar_multiThread_pool_threadExecutor_counterIncremented() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        int threadNums = 99_999;
        IntStream.range(0, threadNums).forEach(i -> executorService.execute(new Thread(() -> carService.getCarById(1))));
        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(10, TimeUnit.SECONDS);
        Assertions.assertTrue(terminated);
        Assertions.assertEquals(threadNums, carService.getCarVisitors(1));
    }

    @Test
    void getCar_multiThread_pool_callable_counterIncremented() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        int threadNums = 99_999;
        List<Callable<Object>> callables = new ArrayList<>(threadNums);
        IntStream.range(0, threadNums).forEach(i -> callables.add(Executors.callable(() -> {
            carService.getCarById(1);
        })));
        executorService.invokeAll(callables);
        Assertions.assertEquals(threadNums, carService.getCarVisitors(1));
    }

    @Test
    void createNewCar() {
        CarEntity carEntityBefore = CarEntity.builder().carType(CarType.NEW_CAR).name("Toyota").build();
        Mockito.when(carMapper.toCarEntity(toyotaBefore)).thenReturn(carEntityBefore);

        Car toyota = Car.builder().carType(CarType.NEW_CAR).name("Toyota").build();
        carService.createOrUpdateCar(toyota);

        Mockito.verify(carRepository, Mockito.times(1)).save(carEntityBefore);
    }
}
