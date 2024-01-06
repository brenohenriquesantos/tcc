package com.senai.tcc.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Endereco {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long enderecoId;
	private String logradouro;
	private String cep;
	private String bairro;
	private String localidade;
	private String uf;

	public Endereco() {

	}

	public Endereco(Endereco endereco) {
		this.logradouro = endereco.logradouro;
		this.cep = endereco.cep;
		this.bairro = endereco.bairro;
		this.localidade = endereco.localidade;
		this.uf = endereco.uf;

	}


		

}
