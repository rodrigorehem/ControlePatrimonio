package br.com.rehem.rodrigo.controlepatrimonial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.rehem.rodrigo.controlepatrimonial.domain.UnidadeJudiciaria;

/**
 * Spring Data JPA repository for the UnidadeJudiciaria entity.
 */
public interface UnidadeJudiciariaRepository extends JpaRepository<UnidadeJudiciaria,Long> {

	 @Query("select unidadeJudiciaria from UnidadeJudiciaria unidadeJudiciaria where remove_acentos(upper(unidadeJudiciaria.unidade)) like remove_acentos(upper( :unidade )) AND remove_acentos(upper(unidadeJudiciaria.comarca)) like remove_acentos(upper( :comarca ))")
	    List<UnidadeJudiciaria> findByUnidade(@Param("comarca") String comarca, @Param("unidade") String unidade);
}
