package com.senai.tcc.components;

import java.util.List;

import com.senai.tcc.entities.Estabelecimento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor

public class RespostaConsultaEstab {
	private List<Estabelecimento> estabelecimentos;
	private String mensagem;	
}
