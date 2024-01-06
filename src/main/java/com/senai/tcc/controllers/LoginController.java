package com.senai.tcc.controllers;

import java.security.InvalidAlgorithmParameterException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.senai.tcc.components.UsuarioAutenticator;
import com.senai.tcc.services.CookieService;
import com.senai.tcc.services.UsuarioService;

import jakarta.servlet.http.HttpServletResponse;
import javassist.NotFoundException;

@Controller
public class LoginController {

	@Autowired
	UsuarioService usuarioService;

	@PostMapping("/login")
	public ResponseEntity<?> fazerLogin(@RequestBody Map<String, String> credenciais, HttpServletResponse response) {

		try {

			String cpf = credenciais.get("cpf");
			String senha = credenciais.get("senha");

			UsuarioAutenticator usr = usuarioService.login(cpf, senha);

			int segCookie = 1800;

			CookieService.setCookie(response, "usuarioID", String.valueOf(usuarioService.buscarIdPeloCpf(cpf)),
					segCookie);

			return ResponseEntity.ok(usr);
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (InvalidAlgorithmParameterException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}

	}

	@GetMapping("/login")
	public String redirecionarLogin() {
		return "login/formLogin";
	}
	
	
	@PostMapping("/deslogar")
	public ResponseEntity<?> deslogar(@RequestBody String nomeCookie,
			HttpServletResponse response) {
		
		CookieService.deletarCookie(response, nomeCookie);
		
		return ResponseEntity.ok(null);
	}
}
