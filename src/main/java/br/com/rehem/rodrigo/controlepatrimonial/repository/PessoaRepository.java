package br.com.rehem.rodrigo.controlepatrimonial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.rehem.rodrigo.controlepatrimonial.domain.Pessoa;
import br.com.rehem.rodrigo.controlepatrimonial.domain.dto.ItemMovPessoaDTO;

/**
 * Spring Data JPA repository for the Pessoa entity.
 */
public interface PessoaRepository extends JpaRepository<Pessoa,Long> {
	
    @Query("select pessoa from Pessoa pessoa where upper(pessoa.nome) like :nome")
    List<Pessoa> findByNome(@Param("nome") String nome);
    
    @Query("select new br.com.rehem.rodrigo.controlepatrimonial.domain.dto.ItemMovPessoaDTO(i,m,p) "
    		+ " from Item i inner join "
    		+ "      i.movimentacaos m inner join "
    		+ "      m.pessoa p inner join "
    		+ "      m.tipoMovimentacao tmv "
    		+ " where p.id = :id order by m.data desc ")
    List<ItemMovPessoaDTO> allItemPessoaPorMovimentacao(@Param("id") Long id);
    
    @Query(" SELECT new br.com.rehem.rodrigo.controlepatrimonial.domain.dto.ItemMovPessoaDTO(i,m2,p) FROM "
    		+ " Item i inner join "
    		+ " i.movimentacaos m2 inner join "
    		+ " m2.tipoMovimentacao tm inner join "
    		+ " m2.pessoa p "
    		+ " WHERE  "
    		+ "		tm.id = :tipoMov AND p.id = :id AND"
    		+ "		m2.data = ( SELECT max(m3.data) from Movimentacao m3 inner join m3.items i2 WHERE i2.id = i.id )  "
    		+ " order by m2.data desc ")
    List<ItemMovPessoaDTO> allItemEntreguePessoaPorMovimentacao(@Param("id") Long id, @Param("tipoMov") Long tipoMov);
    
}
