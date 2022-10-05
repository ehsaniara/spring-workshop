package com.springworkshop.dealership.mapper;

import com.springworkshop.dealership.domain.Car;
import com.springworkshop.dealership.domain.CarEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CarMapper {

    Car toCarDto(CarEntity carEntity);

    CarEntity toCarEntity(Car carDto);
}
