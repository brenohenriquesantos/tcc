package com.senai.tcc.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.senai.tcc.entities.Estabelecimento;

@Repository
public interface EstabelecimentoRepository extends JpaRepository<Estabelecimento, Long> {

	@Query(value = "select top(3) * from estabelecimento " + " where ativo = 'S'", nativeQuery = true)
	List<Estabelecimento> obterAcessados();

	@Query(value = "Select TOP 3 * \r\n" + "FROM estabelecimento \r\n"
			+ "where ativo = 'S' and (tipo_id = :tipoId or :tipoId is null) ", nativeQuery = true)
	List<Estabelecimento> obterAcessadosFiltrados(@Param("tipoId") Long tipoId);
	
	

	@Query(value = "select * from estabelecimento\r\n" + "where cnpj = :cnpj and ativo = 'S'", nativeQuery = true)
	Optional<Estabelecimento> findByCnpj(@Param("cnpj") String cnpj);

	@Query(value = "select * from estabelecimento where nome LIKE  %:nome% and ativo = 'S'", nativeQuery = true)
	List<Estabelecimento> obterEstabsPeloNome(@Param("nome") String nome);

	@Query(value = "select top(3) * from estabelecimento, endereco " + "where ativo = 'S' and "
			+ "estabelecimento.enderecoid = endereco.endereco_id and" + "(nome like %:nome% or :nome is null) and "
			+ "(banheiro_acessivel = :banheiro or :banheiro is null) and "
			+ "(rampa_acessivel = :rampa or :rampa is null) and "
			+ "(estacionamento_acessivel = :estac or :estac is null) and " + "(uf like %:uf% or :uf is null) and "
			+ " (localidade like %:localidade% or :localidade is null)", nativeQuery = true)
	List<Estabelecimento> obterEstabsFiltrados(@Param("nome") String nome, @Param("banheiro") String banheiro,
			@Param("rampa") String rampa, @Param("estac") String estac, @Param("uf") String uf,
			@Param("localidade") String localidade);

}