package com.senai.tcc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.senai.tcc.entities.UsrEmailCodigos;
import com.senai.tcc.entities.Usuario;
import com.senai.tcc.exceptions.InvalidCodigoException;
import com.senai.tcc.exceptions.InvalidCpfException;
import com.senai.tcc.repositories.UsrEmailCodigosRepository;
import com.senai.tcc.repositories.UsuarioRepository;

import jakarta.transaction.Transactional;
import javassist.NotFoundException;

@Service
public class UsrEmailCodigosService {

	@Autowired
	UsrEmailCodigosRepository UsrEmailRepository;

	@Autowired
	UsuarioRepository usrRepository;

	public void salvar(UsrEmailCodigos usrCodigos) throws NotFoundException {
		if (usrCodigos != null) {
			UsrEmailRepository.save(usrCodigos);
		} else {
			throw new NotFoundException("Usuario vazio");
		}

	}

	@Transactional
	public void atualizarStatusCodigo(Usuario usr) throws InvalidCpfException, NotFoundException {

		List<UsrEmailCodigos> usrCodigos = UsrEmailRepository.findByUsuario(usr);

		for (UsrEmailCodigos usrCodigo : usrCodigos) {
			usrCodigo.setStatus(false);
		}

		UsrEmailRepository.saveAll(usrCodigos);

	}

	public UsrEmailCodigos buscarRegistroCodigo(String codigo) throws NotFoundException, InvalidCodigoException {

		validarCodigo(codigo);

		Optional<UsrEmailCodigos> registroCodigo = UsrEmailRepository.findByCodigo(codigo);

		validarUsrEmailCodigos(registroCodigo);

		return registroCodigo.get();
	}

	private void validarCodigo(String codigo) throws InvalidCodigoException {

		if (codigo.isBlank()) {
			throw new InvalidCodigoException("Codigo vazio !");
		}

		if (codigo.length() < 6) {
			throw new InvalidCodigoException("Codigo invalido !");
		}
	}

	private void validarUsrEmailCodigos(Optional<UsrEmailCodigos> registroCodigo) throws NotFoundException {
		if (!registroCodigo.isPresent()) {
			throw new NotFoundException("Registro não encontrado !");
		}
	}
}
