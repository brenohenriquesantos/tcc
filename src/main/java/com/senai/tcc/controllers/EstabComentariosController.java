package com.senai.tcc.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.senai.tcc.entities.EstabComentarios;
import com.senai.tcc.entities.Estabelecimento;
import com.senai.tcc.entities.Usuario;
import com.senai.tcc.services.EstabComentariosService;
import com.senai.tcc.services.EstabelecimentoService;
import com.senai.tcc.services.UsuarioService;

import dtos.ComentarioDTO;

@Controller
public class EstabComentariosController {
	
	@Autowired
	private EstabComentariosService comentarioService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private EstabelecimentoService estabService;
	
	
	@PostMapping("estabelecimento/comentar")
	private ResponseEntity<String> salvarComentario(@RequestBody ComentarioDTO comentario){
		try {
			
			Usuario usr = usuarioService.acharUsuario(comentario.getIdUsuario());
			Estabelecimento estab = estabService.getEstByID(comentario.getIdEstabelecimento());
			EstabComentarios novoComentario = new EstabComentarios();
			
			novoComentario.setUsr(usr);
			novoComentario.setEstabelecimento(estab);
			novoComentario.setTexto(comentario.getTexto());
			
			comentarioService.salvar(novoComentario);

			return ResponseEntity.ok("Salvo com Sucesso !");
		}catch(IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}catch (Exception ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}
	
	@GetMapping("estabelecimento/comentarios")
	public ResponseEntity<?> obterComentarios(@RequestParam Long estabID){
		try {
			List <EstabComentarios> comentarios = comentarioService.obterComentarios(estabID);
			
			return ResponseEntity.ok(comentarios);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
}
