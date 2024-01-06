package com.senai.tcc.services;

import io.micrometer.common.util.StringUtils;

public class LimparCEP {
	public static String limpar(String cep) {
		if (StringUtils.isBlank(cep)) {
			throw new IllegalArgumentException("Cep vazio !");
		}
		
		cep = cep.replaceAll("\\D", "");
		return cep;
	}
}
