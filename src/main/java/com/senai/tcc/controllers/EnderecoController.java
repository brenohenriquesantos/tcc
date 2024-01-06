package com.senai.tcc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.senai.tcc.entities.Endereco;
import com.senai.tcc.services.EnderecoService;

import javassist.NotFoundException;

@RestController
public class EnderecoController {
	@Autowired
	EnderecoService enderecoService;

	@GetMapping("buscarEndereco")
	public ResponseEntity<Object> buscarEndereco(@RequestParam String cep) {
		Endereco endereco;
		try {
			endereco = enderecoService.buscarEndereco(cep);

			if (endereco != null) {
				return ResponseEntity.ok(endereco);
			}else {
				return ResponseEntity.notFound().build();
			}

		} catch (NotFoundException inv) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(inv.getMessage());

		} catch (IllegalArgumentException inv) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(inv.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao buscar endereço.");
		}

	}
	
	
	
}
