package com.springworkshop.dealership.service;

import com.springworkshop.dealership.domain.Car;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CarService {
    private final Map<Integer, String> carInventory = new HashMap<>();

    public CarService() {
        carInventory.put(1, "Tesla");
        carInventory.put(2, "Ford");
    }

    public Car getCarById(int carId){
        return carInventory.get(carId);
    }

}
