package com.senai.tcc.services;

import java.security.InvalidAlgorithmParameterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.senai.tcc.components.EmailHtmlModelo;
import com.senai.tcc.entities.EmailMensagem;
import com.senai.tcc.entities.UsrEmailCodigos;
import com.senai.tcc.entities.Usuario;
import com.senai.tcc.exceptions.InvalidCodigoException;
import com.senai.tcc.exceptions.InvalidCpfException;
import com.senai.tcc.exceptions.InvalidEmailException;
import com.senai.tcc.exceptions.InvalidFotoException;
import com.senai.tcc.exceptions.ProcessamentoException;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import javassist.NotFoundException;

@Service
public class RecuperaSenhaService {

	@Autowired
	private UsuarioService usrService;

	@Autowired
	private EmailService emailService;
	@Autowired
	UsrEmailCodigosService usrEmailCodigoService;

	public void enviarEmailRecuperacao(String cpf, String email)
			throws InvalidCpfException, InvalidEmailException, NotFoundException, MessagingException {

		usrService.verificarUsuarioCpfEmail(cpf, email);

		cpf = LimparCPF.limpar(cpf);

		String codigoRecuperacao = Utilitarios.gerarCodigo();

		UsrEmailCodigos usrEmailCodigo = new UsrEmailCodigos(usrService.achaUsuarioCpf(cpf), codigoRecuperacao);

		usrEmailCodigoService.salvar(usrEmailCodigo);

		emailService.enviar(gerarMensagem(cpf, email, codigoRecuperacao));

	}

	@Transactional
	public void alterarSenhaCodigoEmail(String codigo, String senha) throws InvalidCodigoException, NotFoundException,
			InvalidAlgorithmParameterException, InvalidCpfException, ProcessamentoException, InvalidFotoException {

		Utilitarios.validarSenha(senha);

		UsrEmailCodigos registroCodigo = usrEmailCodigoService.buscarRegistroCodigo(codigo);

		if (registroCodigo.isStatus()) {
			Usuario usr = registroCodigo.getUsuario();

			usr.setSenha(senha);

			usrService.salvarUsuario(usr);

			usrEmailCodigoService.atualizarStatusCodigo(usr);

		} else {
			throw new InvalidCodigoException("Codigo de recuperação já utilizado !");
		}

	}

	private EmailMensagem gerarMensagem(String cpf, String email, String codigoRecuperacao)
			throws InvalidCpfException, NotFoundException {

		String assunto = "Recuperação Senha";

		String conteudo = EmailHtmlModelo.getRedefinirSenhaTemplate(usrService.buscarNomePeloCpf(cpf),
				codigoRecuperacao);

		EmailMensagem mensagem = new EmailMensagem(email, assunto, conteudo);

		return mensagem;
	}
}
