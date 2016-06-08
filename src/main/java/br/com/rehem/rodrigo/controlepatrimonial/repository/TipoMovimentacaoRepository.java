package br.com.rehem.rodrigo.controlepatrimonial.repository;

import br.com.rehem.rodrigo.controlepatrimonial.domain.TipoMovimentacao;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoMovimentacao entity.
 */
public interface TipoMovimentacaoRepository extends JpaRepository<TipoMovimentacao,Long> {

}
