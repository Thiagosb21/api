package com.seniordesafio.api.controller;

import com.seniordesafio.api.models.Hospede;
import com.seniordesafio.api.models.Reserva;
import com.seniordesafio.api.repositories.HospedeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hospedes")
public class HospedeController {

    @Autowired
    private HospedeRepository hospedeRepository;

    @PostMapping
    public ResponseEntity<Hospede> cadastrarHospede(@Valid @RequestBody Hospede hospede) {
        Hospede hospedeResult = hospedeRepository.save(hospede);
        return ResponseEntity.status(HttpStatus.CREATED).body(hospedeResult);
    }

    @GetMapping("/sem-checkin")
    public List<Hospede> buscarHospedesSemCheckin() {
        return hospedeRepository.findByReservas_CheckInFalse();
    }

    @GetMapping("/sem-checkout")
    public List<Hospede> buscarHospedesSemCheckout() {
        return hospedeRepository.findDistinctByReservas_CheckOutFalse();
    }

    @GetMapping("/buscar/nome")
    public ResponseEntity<List<Hospede>> buscarPorNome(@RequestParam String nome) {
        List<Hospede> clientes = hospedeRepository.findByNome(nome);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/buscar/cpf")
    public ResponseEntity<List<Hospede>> buscarPorCpf(@RequestParam String cpf) {
        List<Hospede> clientes = hospedeRepository.findByCpf(cpf);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/buscar/telefone")
    public ResponseEntity<List<Hospede>> buscarPorTelefone(@RequestParam String telefone) {
        List<Hospede> clientes = hospedeRepository.findByTelefone(telefone);
        return ResponseEntity.ok(clientes);
    }
}