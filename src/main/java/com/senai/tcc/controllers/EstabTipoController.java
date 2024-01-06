package com.senai.tcc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.senai.tcc.services.EstabTipoService;

@Controller
public class EstabTipoController {
	@Autowired
	private EstabTipoService estabTipoService;
	
	
	@GetMapping("tipos/estabs")
	public ResponseEntity<?> obterTipoEstabs(){
		try {
			return ResponseEntity.ok(estabTipoService.obterTiposEstabs());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
}
