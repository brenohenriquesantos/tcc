package com.senai.tcc.entities;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class EmailMensagem {
	@NotBlank(message = "Destinario vazio")
	private String destinario;
	@NotBlank(message = "Assunto vazio")
	private String assunto;
	@NotBlank(message = "Conteudo vazio")
	private String conteudo;
}
