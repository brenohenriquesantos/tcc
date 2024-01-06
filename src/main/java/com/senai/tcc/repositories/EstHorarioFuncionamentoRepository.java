package com.senai.tcc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.senai.tcc.entities.EstHorarioFuncionamento;

@Repository
public interface EstHorarioFuncionamentoRepository extends JpaRepository<EstHorarioFuncionamento, Long> {

}
