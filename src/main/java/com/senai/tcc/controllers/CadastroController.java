package com.senai.tcc.controllers;

import java.security.InvalidAlgorithmParameterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.senai.tcc.entities.Usuario;
import com.senai.tcc.exceptions.InvalidCpfException;
import com.senai.tcc.exceptions.InvalidFotoException;
import com.senai.tcc.exceptions.ProcessamentoException;
import com.senai.tcc.services.UsuarioService;
import com.senai.tcc.services.Utilitarios;

@Controller
public class CadastroController {

	@Autowired
	UsuarioService usuarioService;

	@GetMapping("/cadastro")
	public String redirecionarForm1() {
		return "templates/cadastro/formCadastro";
	}

	@GetMapping("/cadastro/")
	public String redirecionarForm2() {
		return "cadastro/formCadastro2";
	}

	@PostMapping("/cadastrarUsuario")
	public ResponseEntity<String> cadastrarUsuario(@RequestBody Usuario usr) {
		try {

			usuarioService.salvarUsuario(usr);
			return ResponseEntity.ok("Cadastrado Com Sucesso !");

		} catch (InvalidAlgorithmParameterException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (InvalidCpfException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (ProcessamentoException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (InvalidFotoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (DataIntegrityViolationException dive) {
			// banco de dados.
			return ResponseEntity.badRequest().body("Ocorreu um erro interno.");
		}
	}

	@PostMapping("/verificarCPF")
	public ResponseEntity<String> verificarCPF(@RequestBody String cpf) {

		try {

			Utilitarios.validarCPF(cpf);

			boolean retorno;
			retorno = usuarioService.verificarCPF(cpf);

			if (retorno) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("CPF já esta cadastrado !");
			} else {
				return ResponseEntity.ok(null);
			}
		} catch (InvalidCpfException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}

	}

}
