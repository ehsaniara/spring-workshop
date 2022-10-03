package com.springworkshop.dealership.domain;

import lombok.*;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Car {
    private int id;
    private String name;
    private CarType carType;
}