package com.springworkshop.dealership.service;

import com.springworkshop.dealership.domain.Car;
import com.springworkshop.dealership.domain.CarType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class CarService {
    private final Map<Integer, Car> carInventory = new HashMap<>();
    private final List<Integer> carVisitors = new LinkedList<>();

    public Optional<Car> getCarById(int carId){
        log.debug("Card ID: {}", carId);
        Car result = carInventory.get(carId);
        if (result == null) {
            return Optional.empty();
        }
        carVisitors.add(carId);
        return Optional.of(result);
    }

    public long getCarVisitors(int carId) {
        return carVisitors.stream().filter(id -> id.equals(carId)).count();
    }

    public void createNewCar(Car newCar) {
        int id = carInventory.size() + 1;
        newCar.setId(id);
        carInventory.put(id, newCar);
    }
}
