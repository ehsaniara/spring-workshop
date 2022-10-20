package com.springworkshop.dealership.domain;

import lombok.*;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CarDto {
    private Integer id;
    private String name;
    private CarType carType;
}