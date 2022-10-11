package com.springworkshop.dealership;

import com.springworkshop.dealership.domain.Car;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


class CarControllerIntegrationTest extends IntegrationTestBaseClass {

    @Autowired
    public RestTemplate restTemplate;

    protected String createURLWithPort(String uri) {
        return String.format("http://localhost:%d%s", port, uri);
    }

    @Test
    @Sql(scripts = {"/clean-up-db.sql"})
    public void getAllCarsTest_EmptyDb() {
        assertThat(postgres.isRunning()).isTrue();
        ResponseEntity<List<Car>> response = restTemplate.exchange(createURLWithPort("/cars"),
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        List<Car> rates = response.getBody();
        assertThat(rates).isNotNull();
        assertThat(rates).isEmpty();
    }

    @Test
    @Sql(scripts = {"/clean-up-db.sql", "/cars.sql"})
    public void getAllCarsTest_PreLoadDb() {
        ResponseEntity<List<Car>> response = restTemplate.exchange(createURLWithPort("/cars"),
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        List<Car> rates = response.getBody();
        assertThat(rates).isNotNull();
        assertThat(rates).hasSize(5);
    }

    @Test
    @Sql(scripts = {"/clean-up-db.sql", "/cars.sql"})
    public void getCarTest_PreLoadDb() {
        ResponseEntity<Car> response = restTemplate.exchange(createURLWithPort("/cars/1"),
                HttpMethod.GET, null, Car.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Car rates = response.getBody();
        assertThat(rates).isNotNull();
    }

    @TestConfiguration
    static class config {
        @Bean
        @Primary
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }
}
