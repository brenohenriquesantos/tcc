package com.senai.tcc.services;

import java.security.InvalidAlgorithmParameterException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


public class CriptografaSenha {
	
	
	private static final PasswordEncoder encoder = new BCryptPasswordEncoder();
	
	public static String encriptarSenha(String senha) throws InvalidAlgorithmParameterException {
		
		Utilitarios.validarSenha(senha);
		
		return encoder.encode(senha);
	}
}
