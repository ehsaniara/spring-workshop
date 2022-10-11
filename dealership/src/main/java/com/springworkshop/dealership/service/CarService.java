package com.springworkshop.dealership.service;

import com.springworkshop.dealership.domain.Car;
import com.springworkshop.dealership.domain.CarEntity;
import com.springworkshop.dealership.domain.CarRepository;
import com.springworkshop.dealership.handler.CarNotFoundException;
import com.springworkshop.dealership.mapper.CarMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final List<Integer> carVisitors = Collections.synchronizedList(new ArrayList<>());

    public Optional<Car> getCarById(int carId) {
        log.debug("Card ID: {}", carId);
        Optional<CarEntity> carEntityOptional = carRepository.findById(carId);
        if (carEntityOptional.isPresent()) {
            carVisitors.add(carId);
            return Optional.of(carMapper.toCarDto(carEntityOptional.get()));
        }
        return Optional.empty();
    }

    public long getCarVisitors(int carId) {
        return carVisitors.stream().filter(id -> id.equals(carId)).count();
    }

    public void createCar(Car newCar) {
        CarEntity carEntity = carMapper.toCarEntity(newCar);
        carRepository.save(carEntity);
    }

    public void updateCar(Car newCar) {
         carRepository.findById(newCar.getId()).orElseThrow(CarNotFoundException::new);
         carRepository.save(carMapper.toCarEntity(newCar));
    }

    public List<Car> getAllCars() {
        List<Car> cars = carRepository
                .findAll()
                .stream()
                .map(carMapper::toCarDto)
                .toList();
        return cars;
    }
}
