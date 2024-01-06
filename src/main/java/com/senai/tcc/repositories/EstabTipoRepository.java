package com.senai.tcc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.senai.tcc.entities.EstabelecimentoTipo;

@Repository
public interface EstabTipoRepository extends JpaRepository<EstabelecimentoTipo, Long> {

}
