package com.senai.tcc.controllers;

import java.security.InvalidAlgorithmParameterException;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.senai.tcc.exceptions.InvalidCodigoException;
import com.senai.tcc.exceptions.InvalidCpfException;
import com.senai.tcc.exceptions.InvalidEmailException;
import com.senai.tcc.exceptions.InvalidMapException;
import com.senai.tcc.services.LimparCPF;
import com.senai.tcc.services.RecuperaSenhaService;
import com.senai.tcc.services.Utilitarios;

import javassist.NotFoundException;

@Controller
public class RecuperacaoController {

	@Autowired
	RecuperaSenhaService recuperaService;
	

	@GetMapping("/recuperarSenha")
	public String formRecuperarSenha() {
		return "recuperacao/formRecuperarSenha";
	}

	@PostMapping("/recuperarSenha/enviar")
	public ResponseEntity<String> enviarCodigo(@RequestBody Map<String, String> credenciais) {

		try {

			Utilitarios.validarMapaCpfEmail(credenciais);

			String cpf = LimparCPF.limpar(credenciais.get("cpf"));

			String email = credenciais.get("email");

			recuperaService.enviarEmailRecuperacao(cpf, email);

			return ResponseEntity.status(HttpStatus.OK)
					.body("Codigo de Redefinição enviado para " + "o Email informado !");

		} catch (InvalidMapException | InvalidEmailException | InvalidCpfException erro) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro.getMessage());
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado !");

		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao enviar, tente novamente.");
		}

	}
	


	@PostMapping("/recuperarSenha/recuperar")
	public ResponseEntity<String> recuperarSenha(@RequestBody Map<String, String> credenciais) {

		try {

			String codigo = Optional.ofNullable(credenciais.get("codigo"))
					.orElseThrow(() -> new IllegalArgumentException("Codigo não encontrado !"));

			String senha = Optional.ofNullable(credenciais.get("senha"))
					.orElseThrow(() -> new IllegalArgumentException("Senha não encontrada !"));

			recuperaService.alterarSenhaCodigoEmail(codigo, senha);

			return ResponseEntity.ok("Senha Alterada Com Sucesso !");
		} catch (InvalidAlgorithmParameterException | InvalidCodigoException | InvalidCpfException
				| IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (NotFoundException found) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(found.getMessage());
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao alterar, tente novamente.");
		}
	}
}
