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
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import br.com.rehem.rodrigo.controlepatrimonial.domain.Documento;
import br.com.rehem.rodrigo.controlepatrimonial.domain.Movimentacao;
import br.com.rehem.rodrigo.controlepatrimonial.repository.MovimentacaoRepository;
import br.com.rehem.rodrigo.controlepatrimonial.web.rest.util.HeaderUtil;
import br.com.rehem.rodrigo.controlepatrimonial.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Movimentacao.
 */
@RestController
@RequestMapping("/api")
public class MovimentacaoResource {

    private final Logger log = LoggerFactory.getLogger(MovimentacaoResource.class);
        
    @Inject
    private MovimentacaoRepository movimentacaoRepository;
    
    /**
     * POST  /movimentacaos : Create a new movimentacao.
     *
     * @param movimentacao the movimentacao to create
     * @return the ResponseEntity with status 201 (Created) and with body the new movimentacao, or with status 400 (Bad Request) if the movimentacao has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/movimentacaos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Movimentacao> createMovimentacao(@Valid @RequestBody Movimentacao movimentacao) throws URISyntaxException {
        log.debug("REST request to save Movimentacao : {}", movimentacao);
        if (movimentacao.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("movimentacao", "idexists", "A new movimentacao cannot already have an ID")).body(null);
        }
        Movimentacao result = movimentacaoRepository.save(movimentacao);
        return ResponseEntity.created(new URI("/api/movimentacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("movimentacao", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /movimentacaos : Updates an existing movimentacao.
     *
     * @param movimentacao the movimentacao to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated movimentacao,
     * or with status 400 (Bad Request) if the movimentacao is not valid,
     * or with status 500 (Internal Server Error) if the movimentacao couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/movimentacaos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Movimentacao> updateMovimentacao(@Valid @RequestBody Movimentacao movimentacao) throws URISyntaxException {
        log.debug("REST request to update Movimentacao : {}", movimentacao);
        if (movimentacao.getId() == null) {
            return createMovimentacao(movimentacao);
        }
        Movimentacao result = movimentacaoRepository.save(movimentacao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("movimentacao", movimentacao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /movimentacaos : get all the movimentacaos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of movimentacaos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/movimentacaos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Movimentacao>> getAllMovimentacaos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Movimentacaos");
        Page<Movimentacao> page = movimentacaoRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/movimentacaos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /movimentacaos/:id : get the "id" movimentacao.
     *
     * @param id the id of the movimentacao to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the movimentacao, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/movimentacaos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Movimentacao> getMovimentacao(@PathVariable Long id) {
        log.debug("REST request to get Movimentacao : {}", id);
        Movimentacao movimentacao = movimentacaoRepository.findOneWithEagerRelationships(id);
        for (Documento doc : movimentacao.getDocumentos()) {
        	doc.setMovimentacao(null);
		}        
        return Optional.ofNullable(movimentacao)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /movimentacaos/:id : delete the "id" movimentacao.
     *
     * @param id the id of the movimentacao to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/movimentacaos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMovimentacao(@PathVariable Long id) {
        log.debug("REST request to delete Movimentacao : {}", id);
        movimentacaoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("movimentacao", id.toString())).build();
    }

}
