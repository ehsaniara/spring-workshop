package com.springworkshop.dealership.handler;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(String message) {
        super(message);
    }
}
