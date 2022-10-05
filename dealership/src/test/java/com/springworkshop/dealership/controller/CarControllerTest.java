package com.springworkshop.dealership.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springworkshop.dealership.domain.Car;
import com.springworkshop.dealership.domain.CarType;
import com.springworkshop.dealership.service.CarService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;
@WebMvcTest(CarController.class)
class CarControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;


    @Test
    void getCarTest_200() throws Exception {
        Car tesla = Car.builder().name("Tesla").carType(CarType.NEW_CAR).id(1).build();
        Mockito.when(carService.getCarById(Mockito.anyInt())).thenReturn(Optional.of(tesla));
        mockMvc.perform(MockMvcRequestBuilders.get("/cars/{carId}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Tesla"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carType").value("NEW_CAR"))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(carService, Mockito.times(1)).getCarById(1);
    }
    @Test
    void getCarTest_404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cars/{carId}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(carService, Mockito.times(1)).getCarById(1);
    }
    @Test
    void createNewCarTest() throws Exception {
        ObjectMapper objMapper = new ObjectMapper();
        Car tesla = Car.builder().carType(CarType.NEW_CAR).name("Tesla").build();
        mockMvc.perform(MockMvcRequestBuilders.post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(tesla)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
        Mockito.verify(carService, Mockito.times(1)).createOrUpdateCar(tesla);
    }
}
