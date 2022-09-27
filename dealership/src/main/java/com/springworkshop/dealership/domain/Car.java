package com.springworkshop.dealership.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicLong;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class Car {
    private int id;
    private String name;
    private CarType carType;
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private AtomicLong visitorCounter = new AtomicLong(0);
}
