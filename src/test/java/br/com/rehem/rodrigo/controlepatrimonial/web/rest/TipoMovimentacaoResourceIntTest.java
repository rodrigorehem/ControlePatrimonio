package br.com.rehem.rodrigo.controlepatrimonial.web.rest;

import br.com.rehem.rodrigo.controlepatrimonial.ControlePatrimonialApp;
import br.com.rehem.rodrigo.controlepatrimonial.domain.TipoMovimentacao;
import br.com.rehem.rodrigo.controlepatrimonial.repository.TipoMovimentacaoRepository;

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
 * Test class for the TipoMovimentacaoResource REST controller.
 *
 * @see TipoMovimentacaoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ControlePatrimonialApp.class)
@WebAppConfiguration
@IntegrationTest
public class TipoMovimentacaoResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAA";
    private static final String UPDATED_NOME = "BBBBB";
    private static final String DEFAULT_DESCRICAO = "AAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBB";

    @Inject
    private TipoMovimentacaoRepository tipoMovimentacaoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTipoMovimentacaoMockMvc;

    private TipoMovimentacao tipoMovimentacao;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TipoMovimentacaoResource tipoMovimentacaoResource = new TipoMovimentacaoResource();
        ReflectionTestUtils.setField(tipoMovimentacaoResource, "tipoMovimentacaoRepository", tipoMovimentacaoRepository);
        this.restTipoMovimentacaoMockMvc = MockMvcBuilders.standaloneSetup(tipoMovimentacaoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tipoMovimentacao = new TipoMovimentacao();
        tipoMovimentacao.setNome(DEFAULT_NOME);
        tipoMovimentacao.setDescricao(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createTipoMovimentacao() throws Exception {
        int databaseSizeBeforeCreate = tipoMovimentacaoRepository.findAll().size();

        // Create the TipoMovimentacao

        restTipoMovimentacaoMockMvc.perform(post("/api/tipo-movimentacaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tipoMovimentacao)))
                .andExpect(status().isCreated());

        // Validate the TipoMovimentacao in the database
        List<TipoMovimentacao> tipoMovimentacaos = tipoMovimentacaoRepository.findAll();
        assertThat(tipoMovimentacaos).hasSize(databaseSizeBeforeCreate + 1);
        TipoMovimentacao testTipoMovimentacao = tipoMovimentacaos.get(tipoMovimentacaos.size() - 1);
        assertThat(testTipoMovimentacao.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTipoMovimentacao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoMovimentacaoRepository.findAll().size();
        // set the field null
        tipoMovimentacao.setNome(null);

        // Create the TipoMovimentacao, which fails.

        restTipoMovimentacaoMockMvc.perform(post("/api/tipo-movimentacaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tipoMovimentacao)))
                .andExpect(status().isBadRequest());

        List<TipoMovimentacao> tipoMovimentacaos = tipoMovimentacaoRepository.findAll();
        assertThat(tipoMovimentacaos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipoMovimentacaos() throws Exception {
        // Initialize the database
        tipoMovimentacaoRepository.saveAndFlush(tipoMovimentacao);

        // Get all the tipoMovimentacaos
        restTipoMovimentacaoMockMvc.perform(get("/api/tipo-movimentacaos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tipoMovimentacao.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getTipoMovimentacao() throws Exception {
        // Initialize the database
        tipoMovimentacaoRepository.saveAndFlush(tipoMovimentacao);

        // Get the tipoMovimentacao
        restTipoMovimentacaoMockMvc.perform(get("/api/tipo-movimentacaos/{id}", tipoMovimentacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tipoMovimentacao.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTipoMovimentacao() throws Exception {
        // Get the tipoMovimentacao
        restTipoMovimentacaoMockMvc.perform(get("/api/tipo-movimentacaos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipoMovimentacao() throws Exception {
        // Initialize the database
        tipoMovimentacaoRepository.saveAndFlush(tipoMovimentacao);
        int databaseSizeBeforeUpdate = tipoMovimentacaoRepository.findAll().size();

        // Update the tipoMovimentacao
        TipoMovimentacao updatedTipoMovimentacao = new TipoMovimentacao();
        updatedTipoMovimentacao.setId(tipoMovimentacao.getId());
        updatedTipoMovimentacao.setNome(UPDATED_NOME);
        updatedTipoMovimentacao.setDescricao(UPDATED_DESCRICAO);

        restTipoMovimentacaoMockMvc.perform(put("/api/tipo-movimentacaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTipoMovimentacao)))
                .andExpect(status().isOk());

        // Validate the TipoMovimentacao in the database
        List<TipoMovimentacao> tipoMovimentacaos = tipoMovimentacaoRepository.findAll();
        assertThat(tipoMovimentacaos).hasSize(databaseSizeBeforeUpdate);
        TipoMovimentacao testTipoMovimentacao = tipoMovimentacaos.get(tipoMovimentacaos.size() - 1);
        assertThat(testTipoMovimentacao.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTipoMovimentacao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void deleteTipoMovimentacao() throws Exception {
        // Initialize the database
        tipoMovimentacaoRepository.saveAndFlush(tipoMovimentacao);
        int databaseSizeBeforeDelete = tipoMovimentacaoRepository.findAll().size();

        // Get the tipoMovimentacao
        restTipoMovimentacaoMockMvc.perform(delete("/api/tipo-movimentacaos/{id}", tipoMovimentacao.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TipoMovimentacao> tipoMovimentacaos = tipoMovimentacaoRepository.findAll();
        assertThat(tipoMovimentacaos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
