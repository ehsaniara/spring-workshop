package com.springworkshop.dealership.mapper;

import com.springworkshop.dealership.domain.Car;
import com.springworkshop.dealership.domain.CarEntity;
import com.springworkshop.dealership.domain.CarType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CarMapperTest {

    private final CarMapper carMapper = Mappers.getMapper(CarMapper.class);

    @Test
    void toCarDtoTest() {
        Car car = Car.builder().id(1).name("Tesla").carType(CarType.NEW_CAR).build();

        CarEntity teslaEntity = carMapper.toCarEntity(car);

        assertThat(teslaEntity).isNotNull();
        assertThat(teslaEntity.getId()).isEqualTo(1);
        assertThat(teslaEntity.getName()).isEqualTo("Tesla");
        assertThat(teslaEntity.getCarType()).isEqualTo(CarType.NEW_CAR);
    }

    @Test
    void toCarEntityTest() {
        CarEntity carEntity = CarEntity.builder().id(1).name("Tesla").carType(CarType.NEW_CAR).build();

        Car tesla = carMapper.toCarDto(carEntity);

        assertThat(tesla).isNotNull();
        assertThat(tesla.getId()).isEqualTo(1);
        assertThat(tesla.getName()).isEqualTo("Tesla");
        assertThat(tesla.getCarType()).isEqualTo(CarType.NEW_CAR);
    }
}
