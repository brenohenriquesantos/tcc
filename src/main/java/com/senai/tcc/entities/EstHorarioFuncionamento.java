package com.senai.tcc.entities;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class EstHorarioFuncionamento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String diaSemana;
	@Column(nullable = false)
	@JsonFormat(pattern = "HH:mm", timezone = "America/Sao_Paulo")
	private LocalTime horarioAbertura;
	@JsonFormat(pattern = "HH:mm", timezone = "America/Sao_Paulo")
	@Column(nullable = false)
	private LocalTime horarioFechamento;
}
