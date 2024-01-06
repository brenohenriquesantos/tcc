package com.senai.tcc.services;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.senai.tcc.entities.Endereco;

import javassist.NotFoundException;



@Service
public class EnderecoService {

	@Autowired
	ViaCep consumo;

	public Endereco buscarEndereco(String cep) throws IllegalArgumentException, NotFoundException {

		if (validarCep(cep)) {
			Endereco end = consumo.buscar(cep);
			return end;
		}

		throw new NotFoundException("Endereço não encontrado !");

	}

	private boolean validarCep(String cep) throws IllegalArgumentException {
		

		if(StringUtils.isBlank(cep)) {
			throw new IllegalArgumentException("CEP não pode ser nulo !");
		}
		
		if(cep.length() < 8) {
			throw new IllegalArgumentException("CEP invalido !");
		}

		return true;
	}
}
