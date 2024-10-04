package com.seniordesafio.api.controller;

import com.seniordesafio.api.models.Hospede;
import com.seniordesafio.api.repositories.HospedeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HospedeControllerTest {

    @InjectMocks
    private HospedeController hospedeController;

    @Mock
    private HospedeRepository hospedeRepository;

    @Test
    public void testCadastrarHospede() {
        Hospede hospede = new Hospede();
        hospede.setTelefone("96270908");
        hospede.setNome("Thiago");
        hospede.setCpf("12345678998");

        when(hospedeRepository.save(Mockito.any(Hospede.class))).thenReturn(hospede);

        ResponseEntity<Hospede> response = hospedeController.cadastrarHospede(hospede);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(hospede, response.getBody());
        Mockito.verify(hospedeRepository, times(1)).save(hospede);
    }

    @Test
    public void testBuscarHospedesSemCheckin() {
        List<Hospede> hospedes = new ArrayList<>();
        when(hospedeRepository.findByReservas_CheckInFalse()).thenReturn(hospedes);

        List<Hospede> result = hospedeController.buscarHospedesSemCheckin();

        assertEquals(hospedes, result);
        Mockito.verify(hospedeRepository, times(1)).findByReservas_CheckInFalse();
    }

    @Test
    public void testBuscarHospedesSemCheckout() {
        List<Hospede> hospedes = new ArrayList<>();
        when(hospedeRepository.findDistinctByReservas_CheckOutFalse()).thenReturn(hospedes);

        List<Hospede> result = hospedeController.buscarHospedesSemCheckout();

        assertEquals(hospedes, result);
        Mockito.verify(hospedeRepository, times(1)).findDistinctByReservas_CheckOutFalse();
    }

    @Test
    public void testBuscarPorNome() {
        String nome = "João";
        List<Hospede> hospedes = new ArrayList<>();
        when(hospedeRepository.findByNome(nome)).thenReturn(hospedes);

        ResponseEntity<List<Hospede>> response = hospedeController.buscarPorNome(nome);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(hospedes, response.getBody());
        Mockito.verify(hospedeRepository, times(1)).findByNome(nome);
    }

    @Test
    public void testBuscarPorCpf() {
        String cpf = "12345678900";
        List<Hospede> hospedes = new ArrayList<>();
        when(hospedeRepository.findByCpf(cpf)).thenReturn(hospedes);

        ResponseEntity<List<Hospede>> response = hospedeController.buscarPorCpf(cpf);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(hospedes, response.getBody());
        Mockito.verify(hospedeRepository, times(1)).findByCpf(cpf);
    }

    @Test
    public void testBuscarPorTelefone() {
        String telefone = "123456789";
        List<Hospede> hospedes = new ArrayList<>();
        when(hospedeRepository.findByTelefone(telefone)).thenReturn(hospedes);

        ResponseEntity<List<Hospede>> response = hospedeController.buscarPorTelefone(telefone);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(hospedes, response.getBody());
        Mockito.verify(hospedeRepository, times(1)).findByTelefone(telefone);
    }
}