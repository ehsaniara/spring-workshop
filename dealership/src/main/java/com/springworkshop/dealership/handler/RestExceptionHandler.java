package com.springworkshop.dealership.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(CarNotFoundException.class)
    protected ErrorDetails handleCarNotFound(CarNotFoundException ex, WebRequest request) {
        return ErrorDetails.builder()
                .timestamp(new Date())
                .error(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getDescription(false))
                .build();
    }

    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(IllegalArgumentException.class)
    protected ErrorDetails handleEntityIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        return ErrorDetails.builder()
                .timestamp(new Date())
                .error(ex.getMessage())
                .status(HttpStatus.NOT_ACCEPTABLE.value())
                .path(request.getDescription(false))
                .build();
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorDetails {
        private Date timestamp;
        private String error;
        private Integer status;
        private String path;
    }
}
