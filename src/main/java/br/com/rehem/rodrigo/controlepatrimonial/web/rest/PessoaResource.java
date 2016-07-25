package br.com.rehem.rodrigo.controlepatrimonial.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
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

import br.com.rehem.rodrigo.controlepatrimonial.domain.Pessoa;
import br.com.rehem.rodrigo.controlepatrimonial.domain.dto.ItemMovPessoaDTO;
import br.com.rehem.rodrigo.controlepatrimonial.repository.PessoaRepository;
import br.com.rehem.rodrigo.controlepatrimonial.web.rest.util.HeaderUtil;
import br.com.rehem.rodrigo.controlepatrimonial.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Pessoa.
 */
@RestController
@RequestMapping("/api")
public class PessoaResource {

	private final Logger log = LoggerFactory.getLogger(PessoaResource.class);

	@Inject
	private PessoaRepository pessoaRepository;

	/**
	 * POST  /pessoas : Create a new pessoa.
	 *
	 * @param pessoa the pessoa to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new pessoa, or with status 400 (Bad Request) if the pessoa has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/pessoas",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Pessoa> createPessoa(@Valid @RequestBody Pessoa pessoa) throws URISyntaxException {
		log.debug("REST request to save Pessoa : {}", pessoa);
		if (pessoa.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("pessoa", "idexists", "A new pessoa cannot already have an ID")).body(null);
		}
		Pessoa result = pessoaRepository.save(pessoa);
		return ResponseEntity.created(new URI("/api/pessoas/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("pessoa", result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /pessoas : Updates an existing pessoa.
	 *
	 * @param pessoa the pessoa to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated pessoa,
	 * or with status 400 (Bad Request) if the pessoa is not valid,
	 * or with status 500 (Internal Server Error) if the pessoa couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/pessoas",
			method = RequestMethod.PUT,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Pessoa> updatePessoa(@Valid @RequestBody Pessoa pessoa) throws URISyntaxException {
		log.debug("REST request to update Pessoa : {}", pessoa);
		if (pessoa.getId() == null) {
			return createPessoa(pessoa);
		}
		Pessoa result = pessoaRepository.save(pessoa);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert("pessoa", pessoa.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /pessoas : get all the pessoas.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of pessoas in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@RequestMapping(value = "/pessoas",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Pessoa>> getAllPessoas(Pageable pageable)
			throws URISyntaxException {
		log.debug("REST request to get a page of Pessoas");
		Page<Pessoa> page = pessoaRepository.findAll(pageable); 
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pessoas");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /pessoas : get all the pessoas.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of pessoas in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@RequestMapping(value = "/pessoas/all",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Pessoa>> getAllPessoas(@RequestParam(value = "nome") String nome)
			throws URISyntaxException {
		log.debug("REST request to get a page of Pessoas");
		Order o = new Order("nome");
		Sort sort = new Sort(o);
		List<Pessoa> pessoas = pessoaRepository.findByNome("%"+nome.trim().toUpperCase()+"%"); 
		return Optional.ofNullable(pessoas)
				.map(result -> new ResponseEntity<>(
						result,
						HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	
	@RequestMapping(value = "/pessoas/itens",
	method = RequestMethod.GET,
	produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<List<ItemMovPessoaDTO>>> getAllItensPessoas(@RequestParam(value = "id") Long id)
			throws URISyntaxException {
		log.debug("REST request to get a page of Pessoas All Itens");
		
		List<ItemMovPessoaDTO> itensEntregue   = pessoaRepository.allItemPessoaPorMovimentacao(id, 1l);
		List<ItemMovPessoaDTO> itensDevolvidos = pessoaRepository.allItemPessoaPorMovimentacao(id, 2l);
		List<ItemMovPessoaDTO> itens = new ArrayList<ItemMovPessoaDTO>(itensEntregue);
		List<ItemMovPessoaDTO> retornoDevolvido = new ArrayList<ItemMovPessoaDTO>();
		
/*		for (ItemMovPessoaDTO itemD : itensDevolvidos) 
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
		
		itensDevolvidos.addAll(retornoDevolvido);*/
		List<List<ItemMovPessoaDTO>> retorno = new ArrayList<List<ItemMovPessoaDTO>>();
		retorno.add(itensEntregue);
		retorno.add(itensDevolvidos);
		return Optional.ofNullable(retorno)
				.map(result -> new ResponseEntity<>(
						result,
						HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	/**
	 * GET  /pessoas/:id : get the "id" pessoa.
	 *
	 * @param id the id of the pessoa to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the pessoa, or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/pessoas/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Pessoa> getPessoa(@PathVariable Long id) {
		log.debug("REST request to get Pessoa : {}", id);
		Pessoa pessoa = pessoaRepository.findOne(id);
		return Optional.ofNullable(pessoa)
				.map(result -> new ResponseEntity<>(
						result,
						HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE  /pessoas/:id : delete the "id" pessoa.
	 *
	 * @param id the id of the pessoa to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@RequestMapping(value = "/pessoas/{id}",
			method = RequestMethod.DELETE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deletePessoa(@PathVariable Long id) {
		log.debug("REST request to delete Pessoa : {}", id);
		pessoaRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pessoa", id.toString())).build();
	}

}
