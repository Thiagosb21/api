package com.seniordesafio.api.services;

import com.seniordesafio.api.models.Hospede;
import com.seniordesafio.api.models.Reserva;
import com.seniordesafio.api.repositories.ReservaRepository;
import com.seniordesafio.api.repositories.HospedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private HospedeRepository hospedeRepository;

    public Reserva reserva(Reserva reserva) {
        Optional<Hospede> hospede = hospedeRepository.findById(reserva.getHospede().getId());

        reserva.setCheckIn(false);
        reserva.setCheckOut(false);
        reserva.getHospede().setNome(hospede.get().getNome());
        reserva.getHospede().setTelefone(hospede.get().getTelefone());
        reserva.getHospede().setCpf(hospede.get().getCpf());
        reserva.getHospede().setCarro(hospede.get().isCarro());
        return reservaRepository.save(reserva);
    }

    public String checkIn(Long id) throws Exception {
        try {
            Reserva reserva = reservaRepository.findById(id).orElseThrow(() -> new Exception("Reserva não encontrada"));

            LocalTime agora = LocalTime.now();

            if (reserva.isCheckIn()) {
                return "Check-in já cadastrado!";
            }

            if (agora.isBefore(LocalTime.of(14, 0))) {
                return "Alerta: Check-in só pode ser realizado após as 14h!";
            }

            reserva.setDataCheckIn(LocalDateTime.now());
            reserva.setCheckIn(true);
            reservaRepository.save(reserva);

            return String.format("Check-in realizado com sucesso! Data de Check-in: %s", reserva.getFormattedDataCheckIn());

        } catch (Exception e) {
            return "Erro ao realizar o check-in: " + e.getMessage();
        }
    }


    public String checkOut(Long id) {
        Reserva reserva = reservaRepository.findById(id).orElseThrow();

        if (reserva.isCheckOut()) {
            return "Check-out já realizado";
        }

        reserva.setDataCheckOut(LocalDateTime.now());
        reserva.setCheckOut(true);

        double totalDiaria = calcularValorDiaria(reserva);
        double totalEstac = calcularTaxaEstacionamento(reserva);

        double totalHora = 0;
        if (LocalTime.now().isAfter(LocalTime.of(12, 0))) {
            totalHora = (reserva.getDataCheckOut().getDayOfWeek() == DayOfWeek.SATURDAY || reserva.getDataCheckOut().getDayOfWeek() == DayOfWeek.SUNDAY) ? 90 : 60;
        }

        double totalCheckOut = totalDiaria + totalEstac + totalHora;

        reservaRepository.save(reserva);

        return String.format("Check-out realizado com sucesso!\n" + "Detalhes do pagamento:\n" + "- Diárias: R$ %.2f\n" + "- Estacionamento: R$ %.2f\n" + "- Check-out pós 12:00: R$ %.2f\n" + "----------------------------------\n" + "Total a pagar: R$ %.2f\n" + "Data de Check-out: %s", totalDiaria, totalEstac, totalHora, totalCheckOut, reserva.getFormattedDataCheckOut());
    }


    private double calcularValorDiaria(Reserva reserva) {
        LocalDate dataHoje = LocalDate.now();
        // deveria ser (getDataReserva, dataHoje) mas como o o checkout ta sendo feito antes do check in ele nao pode ser neagtivo
        long dias = ChronoUnit.DAYS.between(dataHoje, reserva.getDataReserva());
        double valorDiaria = 0;

        for (long i = 0; i < dias; i++) {
            LocalDate data = reserva.getDataReserva().plusDays(i);
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
            LocalDate dataHoje = LocalDate.now();
            double taxa = 0;

            for (long i = 0; i < ChronoUnit.DAYS.between(dataHoje, reserva.getDataReserva()); i++) {
                LocalDate data = reserva.getDataReserva().plusDays(i);
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

//    public double calcularValorTotal(Reserva reserva) {
//
//        double total = 0.0;
//
//        total += calcularValorDiaria(reserva);
//        total += calcularTaxaEstacionamento(reserva);
//
//        //ver necessidade desse if
//        if (reserva.isCheckOut()) {
//            if (LocalTime.now().isAfter(LocalTime.of(12, 0))) {
//                if (reserva.getDataCheckOut().getDayOfWeek() == DayOfWeek.SATURDAY || reserva.getDataCheckOut().getDayOfWeek() == DayOfWeek.SUNDAY) {
//                    total += 90;
//                } else {
//                    total += 60;
//                }
//            }
//        }
//
//        return total;
//    }
}
