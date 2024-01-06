package com.senai.tcc.services;

import org.springframework.stereotype.Service;

@Service
public class LimparCPF {
	
	public static String limpar(String cpf) {
		if(cpf == null) {
			return null;
		}
		
		return cpf.replaceAll("[^0-9]", "");
	}
}
