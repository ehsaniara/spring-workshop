package com.springworkshop.dealership.domain;

import lombok.*;

import java.util.concurrent.atomic.AtomicLong;

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
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private AtomicLong visitorCounter = new AtomicLong(0);
}
