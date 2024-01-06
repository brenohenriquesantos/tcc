package com.senai.tcc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.senai.tcc.entities.EstabComentarios;

@Repository
public interface EstabComentariosRepository extends JpaRepository<EstabComentarios, Long> {
	@Query(value = "select * from estab_comentarios where estabelecimentoid = :estabID order by id DESC", nativeQuery = true)
	List<EstabComentarios> obterComentariosEstab(@Param("estabID") Long estabID);

}
