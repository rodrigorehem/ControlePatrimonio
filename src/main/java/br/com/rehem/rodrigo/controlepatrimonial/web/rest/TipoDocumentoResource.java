package br.com.rehem.rodrigo.controlepatrimonial.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rehem.rodrigo.controlepatrimonial.domain.TipoDocumento;
import br.com.rehem.rodrigo.controlepatrimonial.repository.TipoDocumentoRepository;
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
 * REST controller for managing TipoDocumento.
 */
@RestController
@RequestMapping("/api")
public class TipoDocumentoResource {

    private final Logger log = LoggerFactory.getLogger(TipoDocumentoResource.class);
        
    @Inject
    private TipoDocumentoRepository tipoDocumentoRepository;
    
    /**
     * POST  /tipo-documentos : Create a new tipoDocumento.
     *
     * @param tipoDocumento the tipoDocumento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tipoDocumento, or with status 400 (Bad Request) if the tipoDocumento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tipo-documentos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TipoDocumento> createTipoDocumento(@Valid @RequestBody TipoDocumento tipoDocumento) throws URISyntaxException {
        log.debug("REST request to save TipoDocumento : {}", tipoDocumento);
        if (tipoDocumento.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tipoDocumento", "idexists", "A new tipoDocumento cannot already have an ID")).body(null);
        }
        TipoDocumento result = tipoDocumentoRepository.save(tipoDocumento);
        return ResponseEntity.created(new URI("/api/tipo-documentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tipoDocumento", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tipo-documentos : Updates an existing tipoDocumento.
     *
     * @param tipoDocumento the tipoDocumento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tipoDocumento,
     * or with status 400 (Bad Request) if the tipoDocumento is not valid,
     * or with status 500 (Internal Server Error) if the tipoDocumento couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tipo-documentos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TipoDocumento> updateTipoDocumento(@Valid @RequestBody TipoDocumento tipoDocumento) throws URISyntaxException {
        log.debug("REST request to update TipoDocumento : {}", tipoDocumento);
        if (tipoDocumento.getId() == null) {
            return createTipoDocumento(tipoDocumento);
        }
        TipoDocumento result = tipoDocumentoRepository.save(tipoDocumento);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tipoDocumento", tipoDocumento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tipo-documentos : get all the tipoDocumentos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tipoDocumentos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/tipo-documentos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TipoDocumento>> getAllTipoDocumentos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TipoDocumentos");
        Page<TipoDocumento> page = tipoDocumentoRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tipo-documentos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tipo-documentos/:id : get the "id" tipoDocumento.
     *
     * @param id the id of the tipoDocumento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tipoDocumento, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/tipo-documentos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TipoDocumento> getTipoDocumento(@PathVariable Long id) {
        log.debug("REST request to get TipoDocumento : {}", id);
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findOne(id);
        return Optional.ofNullable(tipoDocumento)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tipo-documentos/:id : delete the "id" tipoDocumento.
     *
     * @param id the id of the tipoDocumento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/tipo-documentos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTipoDocumento(@PathVariable Long id) {
        log.debug("REST request to delete TipoDocumento : {}", id);
        tipoDocumentoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tipoDocumento", id.toString())).build();
    }

}
