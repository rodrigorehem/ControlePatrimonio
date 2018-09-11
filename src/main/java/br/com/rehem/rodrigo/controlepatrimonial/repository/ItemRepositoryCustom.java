package br.com.rehem.rodrigo.controlepatrimonial.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import br.com.rehem.rodrigo.controlepatrimonial.domain.Item;
import br.com.rehem.rodrigo.controlepatrimonial.domain.data.PageableCustom;
import br.com.rehem.rodrigo.controlepatrimonial.domain.dto.ItemMovPessoaDTO;

public interface ItemRepositoryCustom 
{
	List<Item> findBySerial(@Param("serial") String serial, @Param("tipoMovimentacao") Long tipoMovimentacao, @Param("pessoa") Long pessoa, @Param("tombo") String tombo);
	
	Page<Item> buscarTodosItensDisponiveis2(PageableCustom pageable);

	Page<ItemMovPessoaDTO> buscarTodosItensEntregue(PageableCustom pageable);

	Page<ItemMovPessoaDTO> buscarTodosItensEmprestados(PageableCustom pageable);

}
