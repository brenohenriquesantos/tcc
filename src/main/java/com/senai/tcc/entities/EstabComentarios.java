package com.senai.tcc.entities;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
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
@Getter
@Setter	
@NoArgsConstructor
@AllArgsConstructor
public class EstabComentarios {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "usuarioID")
	private Usuario usr;
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "estabelecimentoID")
	private Estabelecimento estabelecimento;
	@Column(nullable = false, length = 255)
	private String texto;
	@Column(nullable = false)
	private LocalDate data = LocalDate.now();
}
