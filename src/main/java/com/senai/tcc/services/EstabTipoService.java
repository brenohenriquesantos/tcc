package com.senai.tcc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.senai.tcc.entities.EstabelecimentoTipo;
import com.senai.tcc.repositories.EstabTipoRepository;

@Service
public class EstabTipoService {
	@Autowired
	private EstabTipoRepository estabTipoRepository;

	public List<EstabelecimentoTipo> obterTiposEstabs() {
		return estabTipoRepository.findAll();
	}
}
