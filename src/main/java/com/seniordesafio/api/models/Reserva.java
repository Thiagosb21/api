package com.seniordesafio.api.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hospede_id")
    @NotNull
    private Hospede hospede;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @NotNull
    private LocalDate dataReserva;

    @JsonIgnore
    private boolean checkIn;
    @JsonIgnore
    private boolean checkOut;

    @JsonIgnore
    private LocalDateTime dataCheckOut;
    @JsonIgnore
    private LocalDateTime dataCheckIn;

    @JsonIgnore
    public String getFormattedDataCheckIn() {
        if (dataCheckIn != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return dataCheckIn.format(formatter);
        }
        return null;
    }

    @JsonIgnore
    public String getFormattedDataCheckOut() {
        if (dataCheckOut != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return dataCheckOut.format(formatter);
        }
        return null;
    }


}
