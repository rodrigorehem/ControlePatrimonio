package br.com.rehem.rodrigo.controlepatrimonial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.rehem.rodrigo.controlepatrimonial.domain.Pessoa;

/**
 * Spring Data JPA repository for the Pessoa entity.
 */
public interface PessoaRepository extends JpaRepository<Pessoa,Long> {
	
    @Query("select pessoa from Pessoa pessoa where upper(pessoa.nome) like :nome")
    List<Pessoa> findByNome(@Param("nome") String nome);
}
