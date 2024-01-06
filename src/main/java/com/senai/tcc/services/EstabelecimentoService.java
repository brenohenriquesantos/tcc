package com.senai.tcc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.senai.tcc.components.Base64ToByte;
import com.senai.tcc.components.ByteToBase64;
import com.senai.tcc.components.EstabFiltros;
import com.senai.tcc.entities.EstHorarioFuncionamento;
import com.senai.tcc.entities.Estabelecimento;
import com.senai.tcc.exceptions.InvalidCnpjException;
import com.senai.tcc.exceptions.NotFoundEstabelecimentos;
import com.senai.tcc.exceptions.ProcessamentoException;
import com.senai.tcc.repositories.EstabelecimentoRepository;

import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;

@Service
public class EstabelecimentoService {
	@Autowired
	private EstabelecimentoRepository estRepository;

	@Transactional
	public void salvar(Estabelecimento estabelecimento)
			throws IllegalArgumentException, InvalidCnpjException, ProcessamentoException {

		validarEstabelecimento(estabelecimento);

		estabelecimento.setImgEstabelecimento(convertBase64ToByte(estabelecimento));

		limparCep(estabelecimento);

		limparTelefone(estabelecimento);

		estRepository.save(estabelecimento);
	}

	private void limparCep(Estabelecimento est) {
		est.setCep(LimparCEP.limpar(est.getCep()));
	}

	private void limparTelefone(Estabelecimento est) {
		est.setTelefone(LimparTelefone.limpar(est.getTelefone()));
	}
	
	public List<Estabelecimento> obterAcessados() throws Exception {
		List<Estabelecimento> estabelecimentos = estRepository.obterAcessados();

		validarEstabMaisAcessados(estabelecimentos);

		estabelecimentos = conveterImgsToBase64(estabelecimentos);

		return estabelecimentos;
	}

	public List<Estabelecimento> obterAcessadosFiltrados(Long idTipo) throws Exception {
		List<Estabelecimento> estabelecimentos = estRepository.obterAcessadosFiltrados(idTipo);

		validarEstabMaisAcessados(estabelecimentos);

		estabelecimentos = conveterImgsToBase64(estabelecimentos);

		return estabelecimentos;
	}

	public Estabelecimento getEstByID(Long id) throws IllegalArgumentException, InvalidCnpjException {
		validarID(id);

		Estabelecimento est = estRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Estabelecimento não encontrado !"));

		est = conveterImgToBase64(est);

		validarEstabelecimento(est);

		return est;
	}

	private List<Estabelecimento> conveterImgsToBase64(List<Estabelecimento> estabelecimentos) {
		for (Estabelecimento estabelecimento : estabelecimentos) {
			estabelecimento.setFotoBase64(ByteToBase64.transformar(estabelecimento.getImgEstabelecimento()));
		}

		return estabelecimentos;
	}

	private Estabelecimento conveterImgToBase64(Estabelecimento estabelecimento) {

		estabelecimento.setFotoBase64(ByteToBase64.transformar(estabelecimento.getImgEstabelecimento()));

		return estabelecimento;
	}

	private void validarEstabMaisAcessados(List<Estabelecimento> estabelecimentos) throws Exception {
		if (estabelecimentos == null) {
			throw new Exception("Não há estabelecimentos");
		}
	}

	private byte[] convertBase64ToByte(Estabelecimento estabelecimento) throws ProcessamentoException {
		byte[] imgByte = Base64ToByte.transformar(estabelecimento.getFotoBase64());
		return imgByte;
	}

	private void validarEstabelecimento(Estabelecimento estabelecimento) throws IllegalArgumentException {

		if (StringUtils.isBlank(estabelecimento.getNome())) {
			throw new IllegalArgumentException("Campo nome não pode ser vazio");
		}

		if (StringUtils.isBlank(estabelecimento.getFotoBase64())) {
			throw new IllegalArgumentException("Campo imagem não pode ser vazio");
		}

		validarHorario(estabelecimento.getEstHorario());

	}

	public boolean consultarEstByCnpj(String cnpj) throws InvalidCnpjException {

		cnpj = LimparCNPJ.limpar(cnpj);

		Utilitarios.validarCNPJ(cnpj);

		return estRepository.findByCnpj(cnpj).isPresent();

	}

	private void validarHorario(EstHorarioFuncionamento horario) {

		if (StringUtils.isBlank(horario.getDiaSemana())) {
			throw new IllegalArgumentException("Campo dia de funcionamento não pode ser vazio");
		}

		if (horario.getHorarioAbertura().isAfter(horario.getHorarioFechamento())) {
			throw new IllegalArgumentException("O horario de abertura deve ser anterior ao de fechamento !");
		}
	}

	private void validarID(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("O ID não pode ser nulo !");
		}
	}

	public List<Estabelecimento> obterEstabsPeloNome(String nome) throws NotFoundEstabelecimentos {

		List<Estabelecimento> estabs = estRepository.obterEstabsPeloNome(nome);

		validarEstabelecimentos(estabs);

		estabs = conveterImgsToBase64(estabs);

		return estabs;
	}

	private void validarEstabelecimentos(List<Estabelecimento> estabs) throws NotFoundEstabelecimentos {
		if (estabs.isEmpty()) {
			throw new NotFoundEstabelecimentos("Estabelecimento não encontrado !");
		}
	}

	public List<Estabelecimento> obterEstabs() throws NotFoundEstabelecimentos {

		List<Estabelecimento> estabs = estRepository.findAll();

		validarEstabelecimentos(estabs);

		estabs = conveterImgsToBase64(estabs);

		return estabs;
	}



	public List<Estabelecimento> obterEstabsFiltrado(EstabFiltros filtros) throws NotFoundEstabelecimentos {

		List<Estabelecimento> estabs = estRepository.obterEstabsFiltrados(filtros.nome, filtros.banheiro, filtros.rampa,
				filtros.estacionamento, filtros.uf, filtros.localidade);

		validarEstabelecimentos(estabs);

		estabs = conveterImgsToBase64(estabs);

		return estabs;
	}

}
