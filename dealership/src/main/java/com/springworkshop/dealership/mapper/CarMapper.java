package com.springworkshop.dealership.mapper;

import com.springworkshop.dealership.domain.CarDto;
import com.springworkshop.dealership.domain.CarEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarMapper {

    CarDto toCarDto(CarEntity carEntity);

    CarEntity toCarEntity(CarDto carDto);
}
