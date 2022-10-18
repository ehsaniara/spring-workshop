package com.springworkshop.dealership.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(CarNotFoundException.class)
    protected ResponseEntity<ErrorDetails> handleCarNotFound(CarNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ErrorDetails.builder().timestamp(new Date()).error(ex.getMessage()).status(HttpStatus.NOT_FOUND.value()).path(request.getDescription(false)).build(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorDetails> handleEntityIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return new ResponseEntity<>(ErrorDetails.builder().timestamp(new Date()).error(ex.getMessage()).status(HttpStatus.EXPECTATION_FAILED.value()).path(request.getDescription(false)).build(), HttpStatus.NOT_ACCEPTABLE);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        List<String> validationList = ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();

        return new ResponseEntity<>(ErrorDetails.builder().timestamp(new Date()).error(validationList.toString()).status(status.value()).path(request.getDescription(false)).build(), status);
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
