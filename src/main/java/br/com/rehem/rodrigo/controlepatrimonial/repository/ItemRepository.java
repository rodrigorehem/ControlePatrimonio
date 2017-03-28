package br.com.rehem.rodrigo.controlepatrimonial.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;

import br.com.rehem.rodrigo.controlepatrimonial.domain.Item;
import br.com.rehem.rodrigo.controlepatrimonial.domain.dto.ItemMovPessoaDTO;

/**
 * Spring Data JPA repository for the Item entity.
 */
public interface ItemRepository extends JpaRepository<Item,Long>, ItemRepositoryCustom
{
	
	@Query(" SELECT i FROM Item i where "
			+ " upper(i.serial) like :serial AND "
			+ " upper(i.tombo) like :tombo AND "
			+ "( "
			+ "    ("
			+ "        i.id not in (SELECT DISTINCT ism.id FROM Item ism inner join ism.movimentacaos m ) "
			+ "    ) "
			+ "    OR "
			+ "    ( i.id in ( "
			+ "					SELECT DISTINCT icm.id FROM Item icm inner join icm.movimentacaos m2 inner join m2.tipoMovimentacao tm WHERE  "
			+ "						tm.id = :tipoMovimentacao AND"
			+ "						m2.data = ( SELECT max(m3.data) from Movimentacao m3 inner join m3.items i2 WHERE i2.id = icm.id ) "
			+ "				 )"
			+ "    ) "
			+ " )")
	List<Item> findBySerial(@Param("serial") String serial, @Param("tipoMovimentacao") Long tipoMovimentacao,@Param("tombo") String tombo);
	
	
	@Query("select i from Item i left join fetch i.movimentacaos ")
	List<Item> buscarTodosItensComMovimentacaoDocumento();
	
	@Query(" SELECT i FROM Item i  "
			+ "where "
			+ "    ("
			+ "        i.id not in (SELECT DISTINCT ism.id FROM Item ism inner join ism.movimentacaos m ) "
			+ "    ) "
			+ "    OR "
			+ "    ( i.id in ( "
			+ "					SELECT DISTINCT icm.id FROM Item icm inner join icm.movimentacaos m2 inner join m2.tipoMovimentacao tm WHERE  "
			+ "						tm.id = 2 AND"
			+ "						m2.data = ( SELECT max(m3.data) from Movimentacao m3 inner join m3.items i2 WHERE i2.id = icm.id ) "
			+ "				 )"
			+ "    ) "
			)
			
	 Page<Item> buscarTodosItensDisponiveis(@PageableDefault Pageable pageable);
	
    @Query("select new br.com.rehem.rodrigo.controlepatrimonial.domain.dto.ItemMovPessoaDTO(i,m,p) "
    		+ " from Item i inner join "
    		+ "      i.movimentacaos m inner join "
    		+ "      m.pessoa p inner join "
    		+ "      m.tipoMovimentacao tmv "
    		+ " where i.id = :id ORDER BY m.data DESC ")
    List<ItemMovPessoaDTO> allMovimentacaoByItem(@Param("id") Long id);

}
