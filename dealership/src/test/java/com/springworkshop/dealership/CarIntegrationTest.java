package com.springworkshop.dealership;

import com.springworkshop.dealership.domain.CarDto;
import com.springworkshop.dealership.domain.CarType;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CarIntegrationTest extends IntegrationTestBaseClass {
    @Autowired
    public RestTemplate restTemplate;

    protected String createURLWithPort(String uri) {
        return String.format("http://localhost:%d%s", port, uri);
    }

    @Test
    @Sql("/clean-up-cars.sql")
    public void getAllCarsTest_EmptyDb() {
        assertThat(postgres.isRunning()).isTrue();
        ResponseEntity<List<CarDto>> response = restTemplate.exchange(createURLWithPort("/cars"),
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        List<CarDto> carDtos = response.getBody();
        AssertionsForInterfaceTypes.assertThat(carDtos).isNotNull();
        AssertionsForInterfaceTypes.assertThat(carDtos).isEmpty();
    }

    @Sql(scripts = {"/clean-up-cars.sql", "/cars.sql"})
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    public void getCarTest_PreLoadDb(int carId) {
        ResponseEntity<CarDto> response = restTemplate.exchange(createURLWithPort("/cars/" + carId),
                HttpMethod.GET, null, CarDto.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        CarDto carDto = response.getBody();
        assertThat(carDto).isNotNull();
    }

    @Test
    @Sql("/clean-up-cars.sql")
    public void createCarTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Assertions.assertEquals(
                restTemplate.exchange(createURLWithPort("/cars"),
                        HttpMethod.POST,
                        new HttpEntity<>(CarDto.builder()
                                .carType(CarType.NEW_CAR)
                                .name("Honda")
                                .build(), headers), Void.class
                ).getStatusCode(), HttpStatus.CREATED);

        ResponseEntity<CarDto> response = restTemplate.exchange(createURLWithPort("/cars/1"),
                HttpMethod.GET, null, CarDto.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        CarDto honda = response.getBody();
        assertThat(honda).isNotNull();
        assertThat(honda.getName()).isEqualTo("Honda");
    }

    @Test
    @Sql(scripts = {"/clean-up-cars.sql", "/cars.sql"})
    public void updateCarTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> updateResponse = restTemplate.exchange(createURLWithPort("/cars/1"),
                HttpMethod.PUT,
                new HttpEntity<>(CarDto.builder()
                        .carType(CarType.NEW_CAR)
                        .name("Honda")
                        .build(), headers),
                Void.class
        );
        assertThat(updateResponse).isNotNull();
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        ResponseEntity<CarDto> getResponse = restTemplate.exchange(createURLWithPort("/cars/1"),
                HttpMethod.GET, null, CarDto.class);
        Assertions.assertEquals(getResponse.getStatusCode(), HttpStatus.OK);
        CarDto honda = getResponse.getBody();
        assertThat(honda).isNotNull();
        assertThat(honda.getName()).isEqualTo("Honda");
        assertThat(honda.getCarType()).isEqualTo(CarType.NEW_CAR);
    }

    @Test
    @Sql(scripts = {"/clean-up-cars.sql", "/cars.sql"})
    public void updateCarNameTest() {
        ResponseEntity<Void> updateResponse = restTemplate.exchange(createURLWithPort("/cars/1/carName/Toyota"),
                HttpMethod.PATCH,
                null,
                Void.class
        );
        assertThat(updateResponse).isNotNull();
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        ResponseEntity<CarDto> getResponse = restTemplate.exchange(createURLWithPort("/cars/1"),
                HttpMethod.GET, null, CarDto.class);
        Assertions.assertEquals(getResponse.getStatusCode(), HttpStatus.OK);
        CarDto honda = getResponse.getBody();
        assertThat(honda).isNotNull();
        assertThat(honda.getName()).isEqualTo("Toyota");
        assertThat(honda.getCarType()).isEqualTo(CarType.NEW_CAR);
    }

    @Test
    @Sql(scripts = {"/clean-up-cars.sql", "/cars.sql"})
    public void updateCarPropertiesTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> updateResponse = restTemplate.exchange(createURLWithPort("/cars/1"),
                HttpMethod.PATCH,
                new HttpEntity<>(Map.of("name", "Toyota"), headers),
                Void.class
        );
        assertThat(updateResponse).isNotNull();
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        ResponseEntity<CarDto> getResponse = restTemplate.exchange(createURLWithPort("/cars/1"),
                HttpMethod.GET, null, CarDto.class);
        Assertions.assertEquals(getResponse.getStatusCode(), HttpStatus.OK);
        CarDto honda = getResponse.getBody();
        assertThat(honda).isNotNull();
        assertThat(honda.getName()).isEqualTo("Toyota");
        assertThat(honda.getCarType()).isEqualTo(CarType.NEW_CAR);
    }

    @TestConfiguration
    static class config {
        @Bean
        @Primary
        public RestTemplate restTemplate() {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            return restTemplate;
        }
    }
}
