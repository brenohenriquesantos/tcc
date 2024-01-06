package com.senai.tcc.services;

import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.senai.tcc.exceptions.InvalidCnpjException;
import com.senai.tcc.exceptions.InvalidCpfException;
import com.senai.tcc.exceptions.InvalidEmailException;
import com.senai.tcc.exceptions.InvalidFotoException;
import com.senai.tcc.exceptions.InvalidMapException;

import io.micrometer.common.util.StringUtils;

public class Utilitarios {

	public static void validarEmail(String email) throws InvalidEmailException {
		if (!StringUtils.isBlank(email)) {
			String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(email);

			if (!matcher.matches()) {
				throw new InvalidEmailException("Email invalido");
			}
		} else {
			throw new InvalidEmailException("Email vazio");
		}
	}

	public static void validarCPF(String cpf) throws InvalidCpfException {

		cpf = LimparCPF.limpar(cpf);

		if (StringUtils.isBlank(cpf)) {
			throw new InvalidCpfException("CPF vazio");
		}

		if (cpf.length() != 11) {
			throw new InvalidCpfException("CPF invalido");
		}
	}

	public static void validarCNPJ(String cnpj) throws InvalidCnpjException {
		cnpj = LimparCNPJ.limpar(cnpj);

		if (StringUtils.isBlank(cnpj)) {
			throw new InvalidCnpjException("o campo CNPJ não pode ser nulo");
		}

		if (cnpj.length() != 14) {
			throw new InvalidCnpjException("CNPJ invalido");
		}
	}

	public static String gerarCodigo() {
		SecureRandom random = new SecureRandom();

		int resetCode = random.nextInt(999999 - 100000) + 100000;
		return String.valueOf(resetCode);

	}

	public static void validarMapaCpfEmail(Map<String, String> credenciais) throws InvalidMapException {
		if (credenciais.isEmpty()) {
			throw new InvalidMapException("CPF e Email não fornecidos !");
		}

		if (!credenciais.containsKey("cpf")) {
			throw new InvalidMapException("CPF não fornecido !");
		}

		if (!credenciais.containsKey("email")) {
			throw new InvalidMapException("Email não fornecido !");
		}
	}

	public static void validarSenha(String senha) throws InvalidAlgorithmParameterException {
		if (StringUtils.isBlank(senha)) {
			throw new InvalidAlgorithmParameterException("A senha não pode ser vazia !");
		}
	}

	public static void validarImgBase64(String img64) throws InvalidFotoException {
		if (StringUtils.isBlank(img64) || img64.equals("null")) {
			throw new InvalidFotoException("A foto não pode ser vazia !");

		}

	}

	public static void validarCampoString(String nomeDoCampo, String texto) {
		if (StringUtils.isBlank(texto)) {
			throw new IllegalArgumentException("O " + nomeDoCampo + "não pode ser nulo !");
		}
	}

}
