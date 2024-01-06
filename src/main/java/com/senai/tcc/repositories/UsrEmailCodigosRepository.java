package com.senai.tcc.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.senai.tcc.entities.UsrEmailCodigos;
import com.senai.tcc.entities.Usuario;
@Repository
public interface UsrEmailCodigosRepository extends JpaRepository<UsrEmailCodigos, Long> {
	List<UsrEmailCodigos> findByUsuario(Usuario usr);
	
	Optional<UsrEmailCodigos> findByCodigo(String codigo);

}
