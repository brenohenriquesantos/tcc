package com.senai.tcc.services;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.senai.tcc.components.ByteToBase64;
import com.senai.tcc.entities.EstabComentarios;
import com.senai.tcc.repositories.EstabComentariosRepository;

@Service
public class EstabComentariosService {
	@Autowired
	private EstabComentariosRepository comentarioRepository;

	public void salvar(EstabComentarios estabComentario) {

		validarEstabComentario(estabComentario);

		comentarioRepository.save(estabComentario);
	}

	private void validarEstabComentario(EstabComentarios estabComentario) {
		if (estabComentario.getEstabelecimento() == null) {
			throw new IllegalArgumentException("O ID do estabelecimento não pode ser nulo !");
		}

		if (estabComentario.getUsr() == null) {
			throw new IllegalArgumentException("O ID do usuario não pode ser nulo !");
		}

		if (StringUtils.isBlank(estabComentario.getTexto())) {
			throw new IllegalArgumentException("O texto do comentario não pode ser vazio !");
		}

		if (estabComentario.getTexto().length() > 255) {
			throw new IllegalArgumentException("O texto do comentario não pode ter mais de 255 caracteres !");
		}
	}

	public List<EstabComentarios> obterComentarios(Long estabID) {
		validarID(estabID);

		List<EstabComentarios> comentarios = comentarioRepository.obterComentariosEstab(estabID);

		popularImgs64(comentarios);

		return comentarios;
	}

	private void validarID(Long id) {
		if (id <= 0 || id == null) {
			throw new IllegalArgumentException("ID do estabelecimento Invalido !");
		}
	}

	private void popularImgs64(List<EstabComentarios> comentarios) {
		for (EstabComentarios comentario : comentarios) {
			byte[] imgByte = comentario.getUsr().getUsrImg();

			String img64 = ByteToBase64.transformar(imgByte);

			comentario.getUsr().setFotoBase64(img64);
		}
	}
}
