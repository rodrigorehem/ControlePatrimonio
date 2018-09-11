package br.com.rehem.rodrigo.controlepatrimonial.repository;

import br.com.rehem.rodrigo.controlepatrimonial.domain.Movimentacao;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Movimentacao entity.
 */
public interface MovimentacaoRepository extends JpaRepository<Movimentacao,Long> {

    @Query("select distinct movimentacao from Movimentacao movimentacao left join fetch movimentacao.items ")
    List<Movimentacao> findAllWithEagerRelationships();

    @Query("select movimentacao from Movimentacao movimentacao left join fetch movimentacao.items left join fetch movimentacao.documentos left join fetch movimentacao.unidadeJudiciaria where movimentacao.id =:id")
    Movimentacao findOneWithEagerRelationships(@Param("id") Long id);

}
