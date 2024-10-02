package com.seniordesafio.api.controller;

import com.seniordesafio.api.models.Hospede;
import com.seniordesafio.api.models.Reserva;
import com.seniordesafio.api.services.ReservaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService service;

    @PostMapping
    public ResponseEntity<Reserva> fazerReserva(@Valid @RequestBody Reserva reserva) {
        Reserva reservaResult = service.reserva(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaResult);
    }

    @PostMapping("/checkin/{id}")
    public ResponseEntity<String> checkIn(@PathVariable Long id) throws Exception {
        String checkInResult = service.checkIn(id);
        return ResponseEntity.status(HttpStatus.OK).body(checkInResult);
    }


    @PostMapping("/checkout/{id}")
    public ResponseEntity<String> checkOut(@PathVariable Long id) {
        String checkOutResult = service.checkOut(id);
        return ResponseEntity.status(HttpStatus.OK).body(checkOutResult);
    }
}
