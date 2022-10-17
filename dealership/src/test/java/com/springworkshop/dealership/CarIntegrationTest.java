package com.springworkshop.dealership;

import com.springworkshop.dealership.domain.Car;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.testcontainers.shaded.com.trilead.ssh2.ChannelCondition.TIMEOUT;

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
        ResponseEntity<List<Car>> response = restTemplate.exchange(createURLWithPort("/cars"),
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        List<Car> cars = response.getBody();
        AssertionsForInterfaceTypes.assertThat(cars).isNotNull();
        AssertionsForInterfaceTypes.assertThat(cars).isEmpty();
    }

    @Sql(scripts = {"/clean-up-cars.sql", "/cars.sql"})
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    public void getCarTest_PreLoadDb(int carId) {
        ResponseEntity<Car> response = restTemplate.exchange(createURLWithPort("/cars/" + carId),
                HttpMethod.GET, null, Car.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Car car = response.getBody();
        assertThat(car).isNotNull();
    }

    @Test
    @Sql("/clean-up-cars.sql")
    public void createCarTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Assertions.assertEquals(
                restTemplate.exchange(createURLWithPort("/cars"),
                        HttpMethod.POST,
                        new HttpEntity<>(Car.builder()
                                .carType(CarType.NEW_CAR)
                                .name("Honda")
                                .build(), headers), Void.class
                ).getStatusCode(), HttpStatus.CREATED);

        ResponseEntity<Car> response = restTemplate.exchange(createURLWithPort("/cars/1"),
                HttpMethod.GET, null, Car.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Car honda = response.getBody();
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
                new HttpEntity<>(Car.builder()
                        .carType(CarType.NEW_CAR)
                        .name("Honda")
                        .build(), headers),
                Void.class
        );
        assertThat(updateResponse).isNotNull();
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        ResponseEntity<Car> getResponse = restTemplate.exchange(createURLWithPort("/cars/1"),
                HttpMethod.GET, null, Car.class);
        Assertions.assertEquals(getResponse.getStatusCode(), HttpStatus.OK);
        Car honda = getResponse.getBody();
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

        ResponseEntity<Car> getResponse = restTemplate.exchange(createURLWithPort("/cars/1"),
                HttpMethod.GET, null, Car.class);
        Assertions.assertEquals(getResponse.getStatusCode(), HttpStatus.OK);
        Car honda = getResponse.getBody();
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
