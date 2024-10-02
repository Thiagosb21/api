package com.seniordesafio.api.repositories;

import com.seniordesafio.api.models.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

}
