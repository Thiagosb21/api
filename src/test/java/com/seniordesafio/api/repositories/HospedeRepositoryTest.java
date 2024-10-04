package com.seniordesafio.api.repositories;

import com.seniordesafio.api.models.Hospede;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class HospedeRepositoryTest {

    @Mock
    private HospedeRepository hospedeRepository;

    private Hospede hospede;
    private List<Hospede> hospedes;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        hospede = new Hospede();
        hospede.setNome("João");

        hospedes = new ArrayList<>();
        hospedes.add(hospede);
    }

    @Test
    public void testFindByReservas_CheckInFalse() {
        when(hospedeRepository.findByReservas_CheckInFalse()).thenReturn(hospedes);

        List<Hospede> result = hospedeRepository.findByReservas_CheckInFalse();

        assertEquals(1, result.size());
        assertEquals("João", result.get(0).getNome());

        verify(hospedeRepository, times(1)).findByReservas_CheckInFalse();
    }

    @Test
    public void testFindDistinctByReservas_CheckOutFalse() {
        when(hospedeRepository.findDistinctByReservas_CheckOutFalse()).thenReturn(hospedes);

        List<Hospede> hospedeResultado = hospedeRepository.findDistinctByReservas_CheckOutFalse();

        assertEquals(1, hospedeResultado.size());
        assertEquals("João", hospedeResultado.get(0).getNome());

        verify(hospedeRepository, times(1)).findDistinctByReservas_CheckOutFalse();
    }

    @Test
    public void testFindByNome() {
        when(hospedeRepository.findByNome("João")).thenReturn(hospedes);

        List<Hospede> hospedeResultado = hospedeRepository.findByNome("João");

        assertEquals(1, hospedeResultado.size());
        assertEquals("João", hospedeResultado.get(0).getNome());

        verify(hospedeRepository, times(1)).findByNome("João");
    }

    @Test
    public void testFindByCpf() {
        when(hospedeRepository.findByCpf("12345678900")).thenReturn(hospedes);

        List<Hospede> hospedeResultado = hospedeRepository.findByCpf("12345678900");

        assertEquals(1, hospedeResultado.size());
        assertEquals("João", hospedeResultado.get(0).getNome());

        verify(hospedeRepository, times(1)).findByCpf("12345678900");
    }

    @Test
    public void testFindByTelefone() {
        when(hospedeRepository.findByTelefone("123456789")).thenReturn(hospedes);

        List<Hospede> hospedeResultado = hospedeRepository.findByTelefone("123456789");

        assertEquals(1, hospedeResultado.size());
        assertEquals("João", hospedeResultado.get(0).getNome());

        verify(hospedeRepository, times(1)).findByTelefone("123456789");
    }
}

