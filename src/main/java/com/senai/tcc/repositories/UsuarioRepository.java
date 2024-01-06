package com.senai.tcc.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.senai.tcc.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByCpf(String cpf);
	
	Optional<Usuario> findByCpfAndEmail(String cpf, String email);
	
}
