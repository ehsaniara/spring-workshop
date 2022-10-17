package com.springworkshop.dealership.service;

import com.springworkshop.dealership.domain.Car;
import com.springworkshop.dealership.domain.CarEntity;
import com.springworkshop.dealership.domain.CarRepository;
import com.springworkshop.dealership.domain.CarType;
import com.springworkshop.dealership.mapper.CarMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.IntStream;

class CarServiceTest {

    private final CarRepository carRepository = Mockito.mock(CarRepository.class);
    private final CarMapper carMapper = Mappers.getMapper(CarMapper.class);
    private final CarService carService = new CarService(carRepository, carMapper);

    Car tesla = Car.builder().carType(CarType.NEW_CAR).id(1).name("Tesla").build();
    CarEntity teslaEntity = CarEntity.builder().carType(CarType.NEW_CAR).id(1).name("Tesla").build();
    Car ford = Car.builder().carType(CarType.NEW_CAR).id(2).name("Ford").build();
    CarEntity fordEntity = CarEntity.builder().carType(CarType.NEW_CAR).id(2).name("Ford").build();

    @BeforeEach
    void setUp() {
        //init
        Mockito.when(carRepository.findById(1)).thenReturn(Optional.of(teslaEntity));
        Mockito.when(carRepository.findById(2)).thenReturn(Optional.of(fordEntity));
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
        Car toyota = Car.builder().carType(CarType.NEW_CAR).name("Toyota").build();
        carService.createCar(toyota);
        Mockito.verify(carRepository, Mockito.times(1)).save(Mockito.any());
    }
    @Test
    void updateCar() {
        Car toyota = Car.builder().carType(CarType.NEW_CAR).name("Toyota").id(1).build();
        CarEntity toyotaExpected = CarEntity.builder().carType(CarType.NEW_CAR).name("Toyota").id(1).build();
        carService.updateCar(toyota);
        Mockito.verify(carRepository, Mockito.times(1)).save(toyotaExpected);
    }

    @Test
    void updateCarName() {
        CarEntity toyotaExpected = CarEntity.builder().carType(CarType.NEW_CAR).name("Toyota").id(1).build();
        carService.updateCarName(1, "Toyota");
        Mockito.verify(carRepository, Mockito.times(1)).findById(1);
        Mockito.verify(carRepository, Mockito.times(1)).save(toyotaExpected);
    }

    @Test
    void getAllCars() {
        List<CarEntity> expectedRepositoryCarList = List.of(teslaEntity, fordEntity);
        List<Car> expectedCarList = List.of(tesla, ford);
        Mockito.when(carRepository.findAll()).thenReturn(expectedRepositoryCarList);
        List<Car> actualCarList = carService.getAllCars();

        Assertions.assertEquals(expectedCarList, actualCarList);
        Mockito.verify(carRepository, Mockito.times(1)).findAll();
    }
}
