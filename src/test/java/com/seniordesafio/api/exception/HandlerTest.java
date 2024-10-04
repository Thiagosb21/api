package com.seniordesafio.api.exception;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HandlerTest {

    @InjectMocks
    private Handler handler;

    @Mock
    private BindingResult bindingResult;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandleValidationExceptions() {
        FieldError fieldError = new FieldError("reserva", "dataCheckIn", "Data de check-in inválida");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        when(bindingResult.getObjectName()).thenReturn("reserva");

        Method mockMethod = mock(Method.class);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                new org.springframework.core.MethodParameter(mockMethod, -1), bindingResult
        );

        ResponseEntity<Map<String, Object>> responseEntity = handler.handleValidationExceptions(exception);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Map<String, Object> responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseBody.get("status"));
        assertEquals("Bad Request", responseBody.get("error"));
        assertEquals("Validation failed for one or more fields.", responseBody.get("message"));
        assertNotNull(responseBody.get("timestamp"));
        assertEquals("reserva", responseBody.get("path"));

        Map<String, String> errors = (Map<String, String>) responseBody.get("errors");
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("Data de check-in inválida", errors.get("dataCheckIn"));
    }
}