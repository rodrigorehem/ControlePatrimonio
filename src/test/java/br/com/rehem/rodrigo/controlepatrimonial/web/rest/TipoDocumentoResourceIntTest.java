package br.com.rehem.rodrigo.controlepatrimonial.web.rest;

import br.com.rehem.rodrigo.controlepatrimonial.ControlePatrimonialApp;
import br.com.rehem.rodrigo.controlepatrimonial.domain.TipoDocumento;
import br.com.rehem.rodrigo.controlepatrimonial.repository.TipoDocumentoRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TipoDocumentoResource REST controller.
 *
 * @see TipoDocumentoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ControlePatrimonialApp.class)
@WebAppConfiguration
@IntegrationTest
public class TipoDocumentoResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAA";
    private static final String UPDATED_NOME = "BBBBB";
    private static final String DEFAULT_DESCRICAO = "AAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBB";

    @Inject
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTipoDocumentoMockMvc;

    private TipoDocumento tipoDocumento;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TipoDocumentoResource tipoDocumentoResource = new TipoDocumentoResource();
        ReflectionTestUtils.setField(tipoDocumentoResource, "tipoDocumentoRepository", tipoDocumentoRepository);
        this.restTipoDocumentoMockMvc = MockMvcBuilders.standaloneSetup(tipoDocumentoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tipoDocumento = new TipoDocumento();
        tipoDocumento.setNome(DEFAULT_NOME);
        tipoDocumento.setDescricao(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createTipoDocumento() throws Exception {
        int databaseSizeBeforeCreate = tipoDocumentoRepository.findAll().size();

        // Create the TipoDocumento

        restTipoDocumentoMockMvc.perform(post("/api/tipo-documentos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tipoDocumento)))
                .andExpect(status().isCreated());

        // Validate the TipoDocumento in the database
        List<TipoDocumento> tipoDocumentos = tipoDocumentoRepository.findAll();
        assertThat(tipoDocumentos).hasSize(databaseSizeBeforeCreate + 1);
        TipoDocumento testTipoDocumento = tipoDocumentos.get(tipoDocumentos.size() - 1);
        assertThat(testTipoDocumento.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTipoDocumento.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoDocumentoRepository.findAll().size();
        // set the field null
        tipoDocumento.setNome(null);

        // Create the TipoDocumento, which fails.

        restTipoDocumentoMockMvc.perform(post("/api/tipo-documentos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tipoDocumento)))
                .andExpect(status().isBadRequest());

        List<TipoDocumento> tipoDocumentos = tipoDocumentoRepository.findAll();
        assertThat(tipoDocumentos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipoDocumentos() throws Exception {
        // Initialize the database
        tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentos
        restTipoDocumentoMockMvc.perform(get("/api/tipo-documentos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tipoDocumento.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getTipoDocumento() throws Exception {
        // Initialize the database
        tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get the tipoDocumento
        restTipoDocumentoMockMvc.perform(get("/api/tipo-documentos/{id}", tipoDocumento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tipoDocumento.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTipoDocumento() throws Exception {
        // Get the tipoDocumento
        restTipoDocumentoMockMvc.perform(get("/api/tipo-documentos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipoDocumento() throws Exception {
        // Initialize the database
        tipoDocumentoRepository.saveAndFlush(tipoDocumento);
        int databaseSizeBeforeUpdate = tipoDocumentoRepository.findAll().size();

        // Update the tipoDocumento
        TipoDocumento updatedTipoDocumento = new TipoDocumento();
        updatedTipoDocumento.setId(tipoDocumento.getId());
        updatedTipoDocumento.setNome(UPDATED_NOME);
        updatedTipoDocumento.setDescricao(UPDATED_DESCRICAO);

        restTipoDocumentoMockMvc.perform(put("/api/tipo-documentos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTipoDocumento)))
                .andExpect(status().isOk());

        // Validate the TipoDocumento in the database
        List<TipoDocumento> tipoDocumentos = tipoDocumentoRepository.findAll();
        assertThat(tipoDocumentos).hasSize(databaseSizeBeforeUpdate);
        TipoDocumento testTipoDocumento = tipoDocumentos.get(tipoDocumentos.size() - 1);
        assertThat(testTipoDocumento.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTipoDocumento.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void deleteTipoDocumento() throws Exception {
        // Initialize the database
        tipoDocumentoRepository.saveAndFlush(tipoDocumento);
        int databaseSizeBeforeDelete = tipoDocumentoRepository.findAll().size();

        // Get the tipoDocumento
        restTipoDocumentoMockMvc.perform(delete("/api/tipo-documentos/{id}", tipoDocumento.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TipoDocumento> tipoDocumentos = tipoDocumentoRepository.findAll();
        assertThat(tipoDocumentos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
