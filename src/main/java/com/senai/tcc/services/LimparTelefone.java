package com.senai.tcc.services;

import io.micrometer.common.util.StringUtils;

public class LimparTelefone {
	public static String limpar(String numero) {
		if(StringUtils.isBlank(numero)) {
			throw new IllegalArgumentException("O numeron não pode ser nulo !");
		}
		
		numero = numero.replaceAll("\\D", "");
		
		return numero;
	}
}
