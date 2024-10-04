package com.seniordesafio.api.services;

import com.seniordesafio.api.models.Hospede;
import com.seniordesafio.api.models.Reserva;
import com.seniordesafio.api.repositories.HospedeRepository;
import com.seniordesafio.api.repositories.ReservaRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private HospedeRepository hospedeRepository;

    @Setter
    private Clock clock;

    public ReservaService() {
        this.clock = Clock.systemDefaultZone();
    }

    public Reserva reserva(Reserva reserva) {
        Optional<Hospede> hospede = hospedeRepository.findById(reserva.getHospede().getId_hospede());

        reserva.setCheckIn(false);
        reserva.setCheckOut(false);
        reserva.getHospede().setNome(hospede.get().getNome());
        reserva.getHospede().setTelefone(hospede.get().getTelefone());
        reserva.getHospede().setCpf(hospede.get().getCpf());
        reserva.getHospede().setCarro(hospede.get().isCarro());
        return reservaRepository.save(reserva);
    }

    public String checkIn(Reserva reserva) throws Exception {
        try {
            Reserva reservaRetorno = reservaRepository.findById(reserva.getId_reserva())
                    .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
            LocalTime agora = LocalTime.now(clock);

            if (reservaRetorno.isCheckIn()) {
                return "Check-in já cadastrado!";
            }

            if (agora.isBefore(LocalTime.of(14, 0))) {
                return "Alerta: Check-in só pode ser realizado após as 14h!";
            }

            reservaRetorno.setDataCheckIn(reserva.getDataCheckIn());
            reservaRetorno.setCheckIn(true);
            reservaRepository.save(reservaRetorno);

            return "Check-in realizado com sucesso!";
        } catch (Exception e) {
            return "Erro ao realizar o check-in: " + e.getMessage();
        }
    }


    public String checkOut(Reserva reserva) {
        Reserva reservaRetorno = reservaRepository.findById(reserva.getId_reserva()).orElseThrow();

        if (reservaRetorno.isCheckOut()) {
            return "Check-out já realizado";
        }

        reservaRetorno.setDataCheckOut(reserva.getDataCheckOut());
        reservaRetorno.setCheckOut(true);

        double totalDiaria = calcularValorDiaria(reservaRetorno);
        double totalEstac = calcularTaxaEstacionamento(reservaRetorno);

        LocalDate dataReferencia = LocalDate.now();
        LocalDateTime dataHoraLimite = dataReferencia.atTime(12, 0);

        double totalHora = 0;

        if (reservaRetorno.getDataCheckOut().isAfter(dataHoraLimite)) {
            if (reservaRetorno.getDataCheckOut().getDayOfWeek() == DayOfWeek.SATURDAY || reservaRetorno.getDataCheckOut().getDayOfWeek() == DayOfWeek.SUNDAY) {
                totalHora = 90;
            } else {
                totalHora = 60;
            }
        }

        double totalCheckOut = totalDiaria + totalEstac + totalHora;

        reservaRepository.save(reservaRetorno);

        return String.format("Check-out realizado com sucesso!\n" + "Detalhes do pagamento:\n" + "- Diárias: R$ %.2f\n" + "- Estacionamento: R$ %.2f\n" + "- Check-out pós 12:00: R$ %.2f\n" + "----------------------------------\n" + "Total a pagar: R$ %.2f\n" + "Data de Check-out: %s", totalDiaria, totalEstac, totalHora, totalCheckOut, reservaRetorno.getFormattedDataCheckOut());
    }


    private double calcularValorDiaria(Reserva reserva) {
       long dias = ChronoUnit.DAYS.between(reserva.getDataCheckIn(), reserva.getDataCheckOut());
        LocalTime horaCheckIn = reserva.getDataCheckIn().toLocalTime();
        if (horaCheckIn.isAfter(LocalTime.of(12, 0))) {
            dias += 1;
        }

        double valorDiaria = 0;

        for (long i = 0; i < dias; i++) {
            LocalDate data = LocalDate.from(reserva.getDataCheckIn().plusDays(i));
            if (data.getDayOfWeek() == DayOfWeek.SATURDAY || data.getDayOfWeek() == DayOfWeek.SUNDAY) {
                valorDiaria += 180.0;
            } else {
                valorDiaria += 120.0;
            }
        }

        return valorDiaria;
    }

    private double calcularTaxaEstacionamento(Reserva reserva) {
        if (reserva.getHospede().isCarro()) {
            double taxa = 0;

            long dias = ChronoUnit.DAYS.between(reserva.getDataCheckIn(), reserva.getDataCheckOut());
            LocalTime horaCheckIn = reserva.getDataCheckIn().toLocalTime();
            if (horaCheckIn.isAfter(LocalTime.of(12, 0))) {
                dias += 1;
            }

            for (long i = 0; i < dias; i++) {
                LocalDate data = LocalDate.from(reserva.getDataCheckIn().plusDays(i));
                if (data.getDayOfWeek() == DayOfWeek.SATURDAY || data.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    taxa += 20.0;
                } else {
                    taxa += 15.0;
                }
            }
            return taxa;
        }
        return 0;
    }
}
