package com.springworkshop.dealership.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springworkshop.dealership.domain.Car;
import com.springworkshop.dealership.domain.CarType;
import com.springworkshop.dealership.service.CarService;
import org.hamcrest.Matchers;
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

import java.util.List;
import java.util.Map;
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
        Mockito.verify(carService, Mockito.times(1)).createCar(tesla);
    }

    @Test
    void getAllCars() throws Exception {
        Car tesla = Car.builder().name("Tesla").carType(CarType.NEW_CAR).id(1).build();
        Car ford = Car.builder().name("Ford").carType(CarType.NEW_CAR).id(2).build();
        Mockito.when(carService.getAllCars()).thenReturn(List.of(tesla, ford));
        mockMvc.perform(MockMvcRequestBuilders.get("/cars"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Tesla"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].carType").value("NEW_CAR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Ford"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].carType").value("NEW_CAR"))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(carService, Mockito.times(1)).getAllCars();
    }

    @Test
    void updateCar_202() throws Exception {
        ObjectMapper objMapper = new ObjectMapper();
        Car teslaUsed = Car.builder().carType(CarType.USED_CAR).name("Tesla").build();
        int carId = 1;
        Car teslaExpected = Car.builder().carType(CarType.USED_CAR).id(carId).name("Tesla").build();
        mockMvc.perform(MockMvcRequestBuilders.put("/cars/{carId}", carId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(teslaUsed)))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andDo(MockMvcResultHandlers.print());
        Mockito.verify(carService, Mockito.times(1)).updateCar(teslaExpected);
    }

    @Test
    void updateCarName_202() throws Exception {
        int carId = 1;
        String carName = "Toyota";
        mockMvc.perform(MockMvcRequestBuilders.patch("/cars/{carId}/carName/{carName}", carId, carName))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andDo(MockMvcResultHandlers.print());
        Mockito.verify(carService, Mockito.times(1)).updateCarName(carId, carName);
    }

    @Test
    void updateCar_withProperties_202() throws Exception {
        int carId = 1;
        Map<String, String> properties = Map.of("name", "Toyota");
        ObjectMapper objMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.patch("/cars/{carId}", carId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(properties)))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andDo(MockMvcResultHandlers.print());
        Mockito.verify(carService, Mockito.times(1)).updateCar(carId, properties);
    }
}
