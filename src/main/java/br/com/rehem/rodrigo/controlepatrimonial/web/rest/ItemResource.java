package br.com.rehem.rodrigo.controlepatrimonial.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import br.com.rehem.rodrigo.controlepatrimonial.domain.Item;
import br.com.rehem.rodrigo.controlepatrimonial.domain.data.PageableCustom;
import br.com.rehem.rodrigo.controlepatrimonial.domain.dto.ItemMovPessoaDTO;
import br.com.rehem.rodrigo.controlepatrimonial.repository.ItemRepository;
import br.com.rehem.rodrigo.controlepatrimonial.web.rest.util.HeaderUtil;
import br.com.rehem.rodrigo.controlepatrimonial.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Item.
 */
@RestController
@RequestMapping("/api")
public class ItemResource {

    private final Logger log = LoggerFactory.getLogger(ItemResource.class);
        
    @Inject
    private ItemRepository itemRepository;
    
    /**
     * POST  /items : Create a new item.
     *
     * @param item the item to create
     * @return the ResponseEntity with status 201 (Created) and with body the new item, or with status 400 (Bad Request) if the item has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/items",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Item> createItem(@Valid @RequestBody Item item) throws URISyntaxException {
        log.debug("REST request to save Item : {}", item);
        if (item.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("item", "idexists", "A new item cannot already have an ID")).body(null);
        }
        Item result = itemRepository.save(item);
        return ResponseEntity.created(new URI("/api/items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("item", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /items : Updates an existing item.
     *
     * @param item the item to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated item,
     * or with status 400 (Bad Request) if the item is not valid,
     * or with status 500 (Internal Server Error) if the item couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/items",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Item> updateItem(@Valid @RequestBody Item item) throws URISyntaxException {
        log.debug("REST request to update Item : {}", item);
        if (item.getId() == null) {
            return createItem(item);
        }
        Item result = itemRepository.save(item);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("item", item.getId().toString()))
            .body(result);
    }

    /**
     * GET  /items : get all the items.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of items in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Item>> getAllItems(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Items");
        Page<Item> page = itemRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /items/:id : get the "id" item.
     *
     * @param id the id of the item to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the item, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/items/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Item> getItem(@PathVariable Long id) {
        log.debug("REST request to get Item : {}", id);
        Item item = itemRepository.findOne(id);
        return Optional.ofNullable(item)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /pessoas : get all the pessoas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pessoas in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/itens/all",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Item>> getAllItens(@RequestParam(value = "serial") String serial,@RequestParam(value = "tombo") String tombo, @RequestParam(value = "tipoMovimentacao") Long tipoMovimentacao, @RequestParam(value = "pessoa") Long pessoa)
        throws URISyntaxException {
        log.debug("REST request to get a page of Pessoas");
        //Inverte para tazer os itens oposto aos já cadastrados
        if(tipoMovimentacao == 1)
        {
        	tipoMovimentacao = 2l;
        }else{
        	tipoMovimentacao = 1l;
        }
        List<Item> itens = itemRepository.findBySerial(serial,tipoMovimentacao,pessoa,tombo); 
        return Optional.ofNullable(itens)
                .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    
    @RequestMapping(value = "relarorio/itens/disponivel",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<List<Item>> getItensDisponivel(PageableCustom pageable)
            throws URISyntaxException {
            log.debug("REST request to get itens Disponivel");
            //List<Item> itens = itemRepository.buscarTodosItensDisponiveis(pageable);
            
            
            
            Page<Item> page = itemRepository.buscarTodosItensDisponiveis2(pageable);
            
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "api/relarorio/itens/disponivel");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        }
    
    @RequestMapping(value = "relarorio/itens/entregue",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<List<ItemMovPessoaDTO>> getItensEntregues(PageableCustom pageable)
            throws URISyntaxException {
            log.debug("REST request to get itens Disponivel");
            //List<Item> itens = itemRepository.buscarTodosItensDisponiveis(pageable);
            
            Page<ItemMovPessoaDTO> page = itemRepository.buscarTodosItensEntregue(pageable);
            
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "api/relarorio/itens/entregue");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        }
    
    /**
     * DELETE  /items/:id : delete the "id" item.
     *
     * @param id the id of the item to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/items/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        log.debug("REST request to delete Item : {}", id);
        itemRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("item", id.toString())).build();
    }
    
    
    @RequestMapping(value = "/itens/movimentacoes",
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    		@Timed
    		public ResponseEntity<List<ItemMovPessoaDTO>> getAllItensByMovimentacoes(@RequestParam(value = "id") Long id)
    				throws URISyntaxException {
    			log.debug("REST request to get a page of Pessoas All Itens");
    			List<ItemMovPessoaDTO> itensEntregue   = itemRepository.allMovimentacaoByItem(id);
    			return Optional.ofNullable(itensEntregue)
    					.map(result -> new ResponseEntity<>(
    							result,
    							HttpStatus.OK))
    					.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    			
    			/*List<ItemMovPessoaDTO> itensEntregue   = pessoaRepository.allItemPessoaPorMovimentacao(id, 1l);
    			List<ItemMovPessoaDTO> itensDevolvidos = pessoaRepository.allItemPessoaPorMovimentacao(id, 2l);
    			List<ItemMovPessoaDTO> itens = new ArrayList<ItemMovPessoaDTO>(itensEntregue);
    			List<ItemMovPessoaDTO> retornoDevolvido = new ArrayList<ItemMovPessoaDTO>();
    			
    			for (ItemMovPessoaDTO itemD : itensDevolvidos) 
    			{
    				for (ItemMovPessoaDTO itemE : itensEntregue) 
    				{
    					if(itemD.getItem().getId().toString().trim().equalsIgnoreCase(itemE.getItem().getId().toString().trim()))
    					{
    						itens.remove(itemE);
    						retornoDevolvido.add(itemE);
    						break;
    					}
    				}
    			}
    			
    			itensDevolvidos.addAll(retornoDevolvido);
    			List<List<ItemMovPessoaDTO>> retorno = new ArrayList<List<ItemMovPessoaDTO>>();
    			retorno.add(itens);
    			retorno.add(itensDevolvidos);
    			return Optional.ofNullable(retorno)
    					.map(result -> new ResponseEntity<>(
    							result,
    							HttpStatus.OK))
    					.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));*/
    		}

}
