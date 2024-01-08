package com.senai.tcc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.senai.tcc.entities.Usuario;
import com.senai.tcc.exceptions.ProcessamentoException;
import com.senai.tcc.services.UsuarioService;

import javassist.NotFoundException;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioService usrService;

	@GetMapping("usuario/consultar")
	public ResponseEntity<?> obterUsuario(@RequestParam Long id) {
		try {
			Usuario usr = usrService.acharUsuario(id);
			return ResponseEntity.ok(usr);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Ocorreu um erro no servidor.");
		}
	}

	@GetMapping("/usuario/perfil/")
	public String acessarPerfil() {
		return "target/classes/templates/perfil/perfil.html";
	}

	@PutMapping("/usuario/atualizar")
	public ResponseEntity<String> atualizarUsuario(@RequestBody Usuario usr) {

		try {
			usrService.atualizarUsuario(usr);
			return ResponseEntity.ok().body("Atualizado Com Sucesso !");
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (IllegalArgumentException | ProcessamentoException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possivel atualizar.");
		}

	}
}
