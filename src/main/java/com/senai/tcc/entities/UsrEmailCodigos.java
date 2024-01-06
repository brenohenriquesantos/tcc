package com.senai.tcc.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class UsrEmailCodigos {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
	
	@Column(nullable = false)
	private String codigo;
	
	private LocalDateTime dataCriacao = LocalDateTime.now();
	
	private boolean status = true;
	
	
	public UsrEmailCodigos(Usuario usr, String codigoRecuperacao2) {
		this.usuario = usr;
		this.codigo = codigoRecuperacao2;
	}
}
