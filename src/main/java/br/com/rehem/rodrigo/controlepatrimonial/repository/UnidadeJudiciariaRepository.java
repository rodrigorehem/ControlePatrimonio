package br.com.rehem.rodrigo.controlepatrimonial.repository;

import br.com.rehem.rodrigo.controlepatrimonial.domain.UnidadeJudiciaria;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UnidadeJudiciaria entity.
 */
public interface UnidadeJudiciariaRepository extends JpaRepository<UnidadeJudiciaria,Long> {

}
