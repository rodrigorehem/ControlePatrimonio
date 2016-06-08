package br.com.rehem.rodrigo.controlepatrimonial.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rehem.rodrigo.controlepatrimonial.domain.TipoItem;
import br.com.rehem.rodrigo.controlepatrimonial.repository.TipoItemRepository;
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
 * REST controller for managing TipoItem.
 */
@RestController
@RequestMapping("/api")
public class TipoItemResource {

    private final Logger log = LoggerFactory.getLogger(TipoItemResource.class);
        
    @Inject
    private TipoItemRepository tipoItemRepository;
    
    /**
     * POST  /tipo-items : Create a new tipoItem.
     *
     * @param tipoItem the tipoItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tipoItem, or with status 400 (Bad Request) if the tipoItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tipo-items",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TipoItem> createTipoItem(@Valid @RequestBody TipoItem tipoItem) throws URISyntaxException {
        log.debug("REST request to save TipoItem : {}", tipoItem);
        if (tipoItem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tipoItem", "idexists", "A new tipoItem cannot already have an ID")).body(null);
        }
        TipoItem result = tipoItemRepository.save(tipoItem);
        return ResponseEntity.created(new URI("/api/tipo-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tipoItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tipo-items : Updates an existing tipoItem.
     *
     * @param tipoItem the tipoItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tipoItem,
     * or with status 400 (Bad Request) if the tipoItem is not valid,
     * or with status 500 (Internal Server Error) if the tipoItem couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tipo-items",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TipoItem> updateTipoItem(@Valid @RequestBody TipoItem tipoItem) throws URISyntaxException {
        log.debug("REST request to update TipoItem : {}", tipoItem);
        if (tipoItem.getId() == null) {
            return createTipoItem(tipoItem);
        }
        TipoItem result = tipoItemRepository.save(tipoItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tipoItem", tipoItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tipo-items : get all the tipoItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tipoItems in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/tipo-items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TipoItem>> getAllTipoItems(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TipoItems");
        Page<TipoItem> page = tipoItemRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tipo-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tipo-items/:id : get the "id" tipoItem.
     *
     * @param id the id of the tipoItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tipoItem, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/tipo-items/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TipoItem> getTipoItem(@PathVariable Long id) {
        log.debug("REST request to get TipoItem : {}", id);
        TipoItem tipoItem = tipoItemRepository.findOne(id);
        return Optional.ofNullable(tipoItem)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tipo-items/:id : delete the "id" tipoItem.
     *
     * @param id the id of the tipoItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/tipo-items/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTipoItem(@PathVariable Long id) {
        log.debug("REST request to delete TipoItem : {}", id);
        tipoItemRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tipoItem", id.toString())).build();
    }

}
