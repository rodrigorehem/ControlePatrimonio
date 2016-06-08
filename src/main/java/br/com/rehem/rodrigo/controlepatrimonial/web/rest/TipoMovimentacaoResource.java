package br.com.rehem.rodrigo.controlepatrimonial.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rehem.rodrigo.controlepatrimonial.domain.TipoMovimentacao;
import br.com.rehem.rodrigo.controlepatrimonial.repository.TipoMovimentacaoRepository;
import br.com.rehem.rodrigo.controlepatrimonial.web.rest.util.HeaderUtil;
import br.com.rehem.rodrigo.controlepatrimonial.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TipoMovimentacao.
 */
@RestController
@RequestMapping("/api")
public class TipoMovimentacaoResource {

    private final Logger log = LoggerFactory.getLogger(TipoMovimentacaoResource.class);
        
    @Inject
    private TipoMovimentacaoRepository tipoMovimentacaoRepository;
    
    /**
     * POST  /tipo-movimentacaos : Create a new tipoMovimentacao.
     *
     * @param tipoMovimentacao the tipoMovimentacao to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tipoMovimentacao, or with status 400 (Bad Request) if the tipoMovimentacao has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tipo-movimentacaos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TipoMovimentacao> createTipoMovimentacao(@Valid @RequestBody TipoMovimentacao tipoMovimentacao) throws URISyntaxException {
        log.debug("REST request to save TipoMovimentacao : {}", tipoMovimentacao);
        if (tipoMovimentacao.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tipoMovimentacao", "idexists", "A new tipoMovimentacao cannot already have an ID")).body(null);
        }
        TipoMovimentacao result = tipoMovimentacaoRepository.save(tipoMovimentacao);
        return ResponseEntity.created(new URI("/api/tipo-movimentacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tipoMovimentacao", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tipo-movimentacaos : Updates an existing tipoMovimentacao.
     *
     * @param tipoMovimentacao the tipoMovimentacao to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tipoMovimentacao,
     * or with status 400 (Bad Request) if the tipoMovimentacao is not valid,
     * or with status 500 (Internal Server Error) if the tipoMovimentacao couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tipo-movimentacaos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TipoMovimentacao> updateTipoMovimentacao(@Valid @RequestBody TipoMovimentacao tipoMovimentacao) throws URISyntaxException {
        log.debug("REST request to update TipoMovimentacao : {}", tipoMovimentacao);
        if (tipoMovimentacao.getId() == null) {
            return createTipoMovimentacao(tipoMovimentacao);
        }
        TipoMovimentacao result = tipoMovimentacaoRepository.save(tipoMovimentacao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tipoMovimentacao", tipoMovimentacao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tipo-movimentacaos : get all the tipoMovimentacaos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tipoMovimentacaos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/tipo-movimentacaos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TipoMovimentacao>> getAllTipoMovimentacaos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TipoMovimentacaos");
        Page<TipoMovimentacao> page = tipoMovimentacaoRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tipo-movimentacaos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tipo-movimentacaos/:id : get the "id" tipoMovimentacao.
     *
     * @param id the id of the tipoMovimentacao to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tipoMovimentacao, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/tipo-movimentacaos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TipoMovimentacao> getTipoMovimentacao(@PathVariable Long id) {
        log.debug("REST request to get TipoMovimentacao : {}", id);
        TipoMovimentacao tipoMovimentacao = tipoMovimentacaoRepository.findOne(id);
        return Optional.ofNullable(tipoMovimentacao)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tipo-movimentacaos/:id : delete the "id" tipoMovimentacao.
     *
     * @param id the id of the tipoMovimentacao to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/tipo-movimentacaos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTipoMovimentacao(@PathVariable Long id) {
        log.debug("REST request to delete TipoMovimentacao : {}", id);
        tipoMovimentacaoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tipoMovimentacao", id.toString())).build();
    }

}
