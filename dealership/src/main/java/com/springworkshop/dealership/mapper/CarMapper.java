package com.springworkshop.dealership.mapper;

import com.springworkshop.dealership.domain.Car;
import com.springworkshop.dealership.domain.CarEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarMapper {

    Car toCarDto(CarEntity carEntity);

    CarEntity toCarEntity(Car carDto);
}
