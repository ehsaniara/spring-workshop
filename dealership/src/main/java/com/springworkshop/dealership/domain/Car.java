package com.springworkshop.dealership.domain;

import lombok.*;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Car {
    private Integer id;
    private String name;
    private CarType carType;
}
