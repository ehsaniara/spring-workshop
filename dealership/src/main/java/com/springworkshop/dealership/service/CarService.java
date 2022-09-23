package com.springworkshop.dealership.service;

import com.springworkshop.dealership.domain.Car;
import com.springworkshop.dealership.domain.CarType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CarService {
    private final Map<Integer, Car> carInventory = new HashMap<>();

    public CarService() {
        Car tesla = Car.builder().carType(CarType.NEW_CAR).id(1).name("Tesla").build();
        Car ford = Car.builder().carType(CarType.NEW_CAR).id(2).name("Ford").build();
        carInventory.put(1, tesla);
        carInventory.put(2, ford);
    }

    public Car getCarById(int carId){
        log.debug("Card ID: {}", carId);
        return carInventory.get(carId);
    }

}
