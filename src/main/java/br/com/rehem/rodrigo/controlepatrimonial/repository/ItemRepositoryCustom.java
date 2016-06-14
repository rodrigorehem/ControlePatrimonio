package br.com.rehem.rodrigo.controlepatrimonial.repository;

import org.springframework.data.domain.Page;

import br.com.rehem.rodrigo.controlepatrimonial.domain.Item;
import br.com.rehem.rodrigo.controlepatrimonial.domain.data.PageableCustom;
import br.com.rehem.rodrigo.controlepatrimonial.domain.dto.ItemMovPessoaDTO;

public interface ItemRepositoryCustom 
{
	
	Page<Item> buscarTodosItensDisponiveis2(PageableCustom pageable);

	Page<ItemMovPessoaDTO> buscarTodosItensEntregue(PageableCustom pageable);

}
