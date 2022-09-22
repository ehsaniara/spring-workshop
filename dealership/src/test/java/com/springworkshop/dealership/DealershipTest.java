package com.springworkshop.dealership;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class DealershipTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void mainTest() {
        Assertions.assertNotNull(applicationContext);
    }

}