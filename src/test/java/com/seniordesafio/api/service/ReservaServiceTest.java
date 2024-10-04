package com.seniordesafio.api.service;

import com.seniordesafio.api.models.Hospede;
import com.seniordesafio.api.models.Reserva;
import com.seniordesafio.api.repositories.HospedeRepository;
import com.seniordesafio.api.repositories.ReservaRepository;
import com.seniordesafio.api.services.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private HospedeRepository hospedeRepository;

    @InjectMocks
    private ReservaService reservaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReserva() {
        Hospede hospede = new Hospede();
        hospede.setId_hospede(1L);
        hospede.setNome("John Doe");
        hospede.setTelefone("123456789");
        hospede.setCpf("12345678900");
        hospede.setCarro(true);

        Reserva reserva = new Reserva();
        reserva.setHospede(hospede);

        when(hospedeRepository.findById(1L)).thenReturn(Optional.of(hospede));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        Reserva resultado = reservaService.reserva(reserva);

        assertEquals("John Doe", resultado.getHospede().getNome());
        assertEquals("123456789", resultado.getHospede().getTelefone());
        assertEquals("12345678900", resultado.getHospede().getCpf());
        assertEquals(true, resultado.getHospede().isCarro());
    }
    @Test
    void testCheckInSuccess() throws Exception {
        Reserva reserva = new Reserva();
        reserva.setId_reserva(1L);
        reserva.setCheckIn(false);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        reservaService.setClock(Clock.fixed(Instant.parse("2024-10-04T14:00:00Z"), ZoneOffset.UTC));

        String resultado = reservaService.checkIn(reserva);

        assertEquals("Check-in realizado com sucesso!", resultado);
    }

    @Test
    void testCheckInThrowsException() throws Exception {
        Reserva reserva = new Reserva();
        reserva.setId_reserva(1L);
        reserva.setCheckIn(false);

        when(reservaRepository.findById(reserva.getId_reserva())).thenThrow(new RuntimeException("Erro ao acessar o repositório"));

        String resultado = reservaService.checkIn(reserva);

        assertEquals("Erro ao realizar o check-in: Erro ao acessar o repositório", resultado);
    }

    @Test
    void testCheckInAlreadyDone() throws Exception {
        Reserva reserva = new Reserva();
        reserva.setId_reserva(1L);
        reserva.setCheckIn(true);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        String resultado = reservaService.checkIn(reserva);

        assertEquals("Check-in já cadastrado!", resultado);
    }

    @Test
    void testCheckInBefore14h() throws Exception {
        Reserva reserva = new Reserva();
        reserva.setId_reserva(1L);
        reserva.setCheckIn(false);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        reservaService.setClock(Clock.fixed(Instant.parse("2024-10-04T13:00:00Z"), ZoneOffset.UTC));

        String resultado = reservaService.checkIn(reserva);

        assertEquals("Alerta: Check-in só pode ser realizado após as 14h!", resultado);
    }

    @Test
    void testCheckOutSuccess() {
        Hospede hospede = new Hospede();
        hospede.setId_hospede(1L);
        hospede.setNome("John Doe");
        hospede.setCarro(true);

        Reserva reserva = new Reserva();
        reserva.setId_reserva(1L);
        reserva.setCheckOut(false);
        reserva.setHospede(hospede);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime checkInDateTime = LocalDateTime.of(2024, 10, 1, 14, 30);
        LocalDateTime checkOutDateTime = LocalDateTime.of(2024, 10, 6, 12, 45);

        reserva.setDataCheckIn(checkInDateTime);
        reserva.setDataCheckOut(checkOutDateTime);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        String resultado = reservaService.checkOut(reserva);

        String mensagemEsperada = String.format(
                "Check-out realizado com sucesso!\n" +
                        "Detalhes do pagamento:\n" +
                        "- Diárias: R$ %.2f\n" +
                        "- Estacionamento: R$ %.2f\n" +
                        "- Check-out pós 12:00: R$ %.2f\n" +
                        "----------------------------------\n" +
                        "Total a pagar: R$ %.2f\n" +
                        "Data de Check-out: %s",
                660.00, 80.00, 90.00, 830.00, checkOutDateTime.format(formatter)
        );

        assertEquals(mensagemEsperada, resultado);
    }

    @Test
    void testCheckOutSemCarro() {
        Hospede hospede = new Hospede();
        hospede.setId_hospede(1L);
        hospede.setNome("John Doe");
        hospede.setCarro(false);

        Reserva reserva = new Reserva();
        reserva.setId_reserva(1L);
        reserva.setCheckOut(false);
        reserva.setHospede(hospede);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime checkInDateTime = LocalDateTime.of(2024, 10, 1, 14, 30);
        LocalDateTime checkOutDateTime = LocalDateTime.of(2024, 10, 4, 12, 45);

        reserva.setDataCheckIn(checkInDateTime);
        reserva.setDataCheckOut(checkOutDateTime);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        String resultado = reservaService.checkOut(reserva);

        String mensagemEsperada = String.format(
                "Check-out realizado com sucesso!\n" +
                        "Detalhes do pagamento:\n" +
                        "- Diárias: R$ %.2f\n" +
                        "- Estacionamento: R$ %.2f\n" +
                        "- Check-out pós 12:00: R$ %.2f\n" +
                        "----------------------------------\n" +
                        "Total a pagar: R$ %.2f\n" +
                        "Data de Check-out: %s",
                360.00, 0.00, 60.00, 420.00, checkOutDateTime.format(formatter)
        );

        assertEquals(mensagemEsperada, resultado);
    }

    @Test
    void testCheckOutFinalDeSemana() {
        Hospede hospede = new Hospede();
        hospede.setId_hospede(1L);
        hospede.setNome("John Doe");
        hospede.setCarro(false);

        Reserva reserva = new Reserva();
        reserva.setId_reserva(1L);
        reserva.setCheckOut(false);
        reserva.setHospede(hospede);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime checkInDateTime = LocalDateTime.of(2024, 10, 1, 14, 30);
        LocalDateTime checkOutDateTime = LocalDateTime.of(2024, 10, 6, 12, 45);

        reserva.setDataCheckIn(checkInDateTime);
        reserva.setDataCheckOut(checkOutDateTime);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        String resultado = reservaService.checkOut(reserva);

        String mensagemEsperada = String.format(
                "Check-out realizado com sucesso!\n" +
                        "Detalhes do pagamento:\n" +
                        "- Diárias: R$ %.2f\n" +
                        "- Estacionamento: R$ %.2f\n" +
                        "- Check-out pós 12:00: R$ %.2f\n" +
                        "----------------------------------\n" +
                        "Total a pagar: R$ %.2f\n" +
                        "Data de Check-out: %s",
                660.00, 0.00, 90.00, 750.00, checkOutDateTime.format(formatter)
        );

        assertEquals(mensagemEsperada, resultado);
    }

    @Test
    void testCheckOutAlreadyDone() {
        Hospede hospede = new Hospede();
        hospede.setId_hospede(1L);
        hospede.setNome("John Doe");
        hospede.setCarro(true);

        Reserva reserva = new Reserva();
        reserva.setId_reserva(1L);
        reserva.setCheckOut(true);
        reserva.setHospede(hospede);

        reserva.setDataCheckIn(LocalDate.of(2024, 10, 1).atStartOfDay());
        reserva.setDataCheckOut(LocalDate.of(2024, 10, 4).atStartOfDay());

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        String resultado = reservaService.checkOut(reserva);

        assertEquals("Check-out já realizado", resultado);
    }
}