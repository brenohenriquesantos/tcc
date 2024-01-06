package com.senai.tcc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.senai.tcc.entities.EmailMensagem;
import com.senai.tcc.exceptions.InvalidEmailException;

import io.micrometer.common.util.StringUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;



@Service
public class EmailService {
	@Autowired
	private JavaMailSender sender;

	public void enviar(EmailMensagem mensagem) throws InvalidEmailException, MessagingException {
		
		if(mensagem != null) {
			
			validarCampos(mensagem);
			
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
//			SimpleMailMessage message = new SimpleMailMessage();
			
			helper.setFrom("odintccsenai@gmail.com");
			helper.setTo(mensagem.getDestinario());
			helper.setSubject(mensagem.getAssunto());
			helper.setText(mensagem.getConteudo(), true);
			sender.send(message);	
		}else {
			throw new InvalidEmailException ("Campos vazios !");
		}
		
		
	}
	
	private void validarCampos(EmailMensagem mensagem) throws InvalidEmailException {
		if(StringUtils.isBlank(mensagem.getAssunto())) {
			throw new InvalidEmailException ("Campo Assunto vazio !");
		}
		
		if(StringUtils.isBlank(mensagem.getConteudo())) {
			throw new InvalidEmailException ("Campo Conteudo vazio !");
		}
		
		if(StringUtils.isBlank(mensagem.getDestinario())) {
			throw new InvalidEmailException ("Campo Destinario vazio !");
		}
	}
	
	
}
