package com.seniordesafio.api.repositories;

import com.seniordesafio.api.models.Hospede;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HospedeRepository extends JpaRepository<Hospede, Long> {
    List<Hospede> findByReservas_CheckInFalse();
    List<Hospede> findDistinctByReservas_CheckOutFalse();
    List<Hospede> findByNome(String nome);
    List<Hospede> findByCpf(String cpf);
    List<Hospede> findByTelefone(String telefone);
}