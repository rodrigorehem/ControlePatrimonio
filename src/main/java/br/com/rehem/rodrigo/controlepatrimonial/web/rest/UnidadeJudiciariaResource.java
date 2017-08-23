package br.com.rehem.rodrigo.controlepatrimonial.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rehem.rodrigo.controlepatrimonial.domain.UnidadeJudiciaria;
import br.com.rehem.rodrigo.controlepatrimonial.repository.UnidadeJudiciariaRepository;
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
 * REST controller for managing UnidadeJudiciaria.
 */
@RestController
@RequestMapping("/api")
public class UnidadeJudiciariaResource {

    private final Logger log = LoggerFactory.getLogger(UnidadeJudiciariaResource.class);
        
    @Inject
    private UnidadeJudiciariaRepository unidadeJudiciariaRepository;
    
    /**
     * POST  /unidade-judiciarias : Create a new unidadeJudiciaria.
     *
     * @param unidadeJudiciaria the unidadeJudiciaria to create
     * @return the ResponseEntity with status 201 (Created) and with body the new unidadeJudiciaria, or with status 400 (Bad Request) if the unidadeJudiciaria has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/unidade-judiciarias",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UnidadeJudiciaria> createUnidadeJudiciaria(@Valid @RequestBody UnidadeJudiciaria unidadeJudiciaria) throws URISyntaxException {
        log.debug("REST request to save UnidadeJudiciaria : {}", unidadeJudiciaria);
        if (unidadeJudiciaria.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("unidadeJudiciaria", "idexists", "A new unidadeJudiciaria cannot already have an ID")).body(null);
        }
        UnidadeJudiciaria result = unidadeJudiciariaRepository.save(unidadeJudiciaria);
        return ResponseEntity.created(new URI("/api/unidade-judiciarias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("unidadeJudiciaria", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /unidade-judiciarias : Updates an existing unidadeJudiciaria.
     *
     * @param unidadeJudiciaria the unidadeJudiciaria to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated unidadeJudiciaria,
     * or with status 400 (Bad Request) if the unidadeJudiciaria is not valid,
     * or with status 500 (Internal Server Error) if the unidadeJudiciaria couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/unidade-judiciarias",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UnidadeJudiciaria> updateUnidadeJudiciaria(@Valid @RequestBody UnidadeJudiciaria unidadeJudiciaria) throws URISyntaxException {
        log.debug("REST request to update UnidadeJudiciaria : {}", unidadeJudiciaria);
        if (unidadeJudiciaria.getId() == null) {
            return createUnidadeJudiciaria(unidadeJudiciaria);
        }
        UnidadeJudiciaria result = unidadeJudiciariaRepository.save(unidadeJudiciaria);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("unidadeJudiciaria", unidadeJudiciaria.getId().toString()))
            .body(result);
    }

    /**
     * GET  /unidade-judiciarias : get all the unidadeJudiciarias.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of unidadeJudiciarias in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/unidade-judiciarias",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UnidadeJudiciaria>> getAllUnidadeJudiciarias(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of UnidadeJudiciarias");
        Page<UnidadeJudiciaria> page = unidadeJudiciariaRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/unidade-judiciarias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /unidade-judiciarias/:id : get the "id" unidadeJudiciaria.
     *
     * @param id the id of the unidadeJudiciaria to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the unidadeJudiciaria, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/unidade-judiciarias/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UnidadeJudiciaria> getUnidadeJudiciaria(@PathVariable Long id) {
        log.debug("REST request to get UnidadeJudiciaria : {}", id);
        UnidadeJudiciaria unidadeJudiciaria = unidadeJudiciariaRepository.findOne(id);
        return Optional.ofNullable(unidadeJudiciaria)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /unidade-judiciarias/:id : delete the "id" unidadeJudiciaria.
     *
     * @param id the id of the unidadeJudiciaria to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/unidade-judiciarias/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUnidadeJudiciaria(@PathVariable Long id) {
        log.debug("REST request to delete UnidadeJudiciaria : {}", id);
        unidadeJudiciariaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("unidadeJudiciaria", id.toString())).build();
    }

}
