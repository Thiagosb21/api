package com.seniordesafio.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Entity
@Getter
@Setter
public class Hospede {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_hospede;

    @NotBlank(message = "Nome é obrigatório.")
    private String nome;

    @NotBlank(message = "O telefone é obrigatório.")
    @Pattern(regexp = "^\\d{8}$", message = "O telefone deve ter 8 dígitos.")
    private String telefone;

    @NotBlank(message = "O CPF é obrigatório.")
    @Pattern(regexp = "^\\d{11}$", message = "O CPF deve ter 11 dígitos.")
    private String cpf;

    private boolean carro;

    @OneToMany(mappedBy = "hospede")
    @JsonIgnore
    private List<Reserva> reservas;


}
