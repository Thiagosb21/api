package com.seniordesafio.api.controller;

import com.seniordesafio.api.models.Reserva;
import com.seniordesafio.api.services.ReservaService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class ReservaControllerTest {

    @Mock
    private ReservaService service;

    @InjectMocks
    private ReservaController reservaController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFazerReserva() {
        Reserva reserva = new Reserva();
        reserva.setId_reserva(1L);

        when(service.reserva(any(Reserva.class))).thenReturn(reserva);

        ResponseEntity<Reserva> reservaResultado = reservaController.fazerReserva(reserva);

        assertNotNull(reservaResultado);
        assertEquals(HttpStatus.CREATED, reservaResultado.getStatusCode());
        assertEquals(reserva, reservaResultado.getBody());

        Mockito.verify(service, times(1)).reserva(any(Reserva.class));
    }

    @Test
    public void testCheckIn() throws Exception {
        Reserva reserva = new Reserva();
        reserva.setId_reserva(1L);
        String checkInResultado = "Check-in realizado com sucesso!";

        when(service.checkIn(any(Reserva.class))).thenReturn(checkInResultado);

        ResponseEntity<String> reservaResultado = reservaController.checkIn(reserva);

        assertNotNull(reservaResultado);
        assertEquals(HttpStatus.OK, reservaResultado.getStatusCode());
        assertEquals(checkInResultado, reservaResultado.getBody());

        Mockito.verify(service, times(1)).checkIn(any(Reserva.class));
    }

    @Test
    public void testCheckOut() {
        Reserva reserva = new Reserva();
        reserva.setId_reserva(1L);
        String checkOutResultado = "Check-out realizado com sucesso!";

        when(service.checkOut(any(Reserva.class))).thenReturn(checkOutResultado);

        ResponseEntity<String> reservaResultado = reservaController.checkOut(reserva);

        assertNotNull(reservaResultado);
        assertEquals(HttpStatus.OK, reservaResultado.getStatusCode());
        assertEquals(checkOutResultado, reservaResultado.getBody());

        Mockito.verify(service, times(1)).checkOut(any(Reserva.class));
    }
}
