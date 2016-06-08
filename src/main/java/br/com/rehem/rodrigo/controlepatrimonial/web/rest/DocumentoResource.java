package br.com.rehem.rodrigo.controlepatrimonial.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rehem.rodrigo.controlepatrimonial.domain.Documento;
import br.com.rehem.rodrigo.controlepatrimonial.repository.DocumentoRepository;
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
 * REST controller for managing Documento.
 */
@RestController
@RequestMapping("/api")
public class DocumentoResource {

    private final Logger log = LoggerFactory.getLogger(DocumentoResource.class);
        
    @Inject
    private DocumentoRepository documentoRepository;
    
    /**
     * POST  /documentos : Create a new documento.
     *
     * @param documento the documento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new documento, or with status 400 (Bad Request) if the documento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/documentos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Documento> createDocumento(@Valid @RequestBody Documento documento) throws URISyntaxException {
        log.debug("REST request to save Documento : {}", documento);
        if (documento.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("documento", "idexists", "A new documento cannot already have an ID")).body(null);
        }
        Documento result = documentoRepository.save(documento);
        return ResponseEntity.created(new URI("/api/documentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("documento", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /documentos : Updates an existing documento.
     *
     * @param documento the documento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated documento,
     * or with status 400 (Bad Request) if the documento is not valid,
     * or with status 500 (Internal Server Error) if the documento couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/documentos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Documento> updateDocumento(@Valid @RequestBody Documento documento) throws URISyntaxException {
        log.debug("REST request to update Documento : {}", documento);
        if (documento.getId() == null) {
            return createDocumento(documento);
        }
        Documento result = documentoRepository.save(documento);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("documento", documento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /documentos : get all the documentos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of documentos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/documentos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Documento>> getAllDocumentos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Documentos");
        Page<Documento> page = documentoRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/documentos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /documentos/:id : get the "id" documento.
     *
     * @param id the id of the documento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the documento, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/documentos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Documento> getDocumento(@PathVariable Long id) {
        log.debug("REST request to get Documento : {}", id);
        Documento documento = documentoRepository.findOne(id);
        return Optional.ofNullable(documento)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /documentos/:id : delete the "id" documento.
     *
     * @param id the id of the documento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/documentos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDocumento(@PathVariable Long id) {
        log.debug("REST request to delete Documento : {}", id);
        documentoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("documento", id.toString())).build();
    }

}
