package com.senai.tcc.services;

import org.springframework.stereotype.Service;

import io.micrometer.common.util.StringUtils;

@Service
public class LimparCNPJ {
	
	public static String limpar(String cnpj) {
		if(StringUtils.isBlank(cnpj)) {
			return null;
		}
		
		return cnpj.replaceAll("[^\\d]", "");
	}
}
