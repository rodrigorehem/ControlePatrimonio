package br.com.rehem.rodrigo.controlepatrimonial.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import br.com.rehem.rodrigo.controlepatrimonial.ControlePatrimonialApp;
import br.com.rehem.rodrigo.controlepatrimonial.domain.Movimentacao;
import br.com.rehem.rodrigo.controlepatrimonial.repository.MovimentacaoRepository;


/**
 * Test class for the MovimentacaoResource REST controller.
 *
 * @see MovimentacaoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ControlePatrimonialApp.class)
@WebAppConfiguration
@IntegrationTest
public class MovimentacaoResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBB";

    private static final ZonedDateTime DEFAULT_DATA_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATA_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Inject
    private MovimentacaoRepository movimentacaoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMovimentacaoMockMvc;

    private Movimentacao movimentacao;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MovimentacaoResource movimentacaoResource = new MovimentacaoResource();
        ReflectionTestUtils.setField(movimentacaoResource, "movimentacaoRepository", movimentacaoRepository);
        this.restMovimentacaoMockMvc = MockMvcBuilders.standaloneSetup(movimentacaoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        movimentacao = new Movimentacao();
        movimentacao.setDescricao(DEFAULT_DESCRICAO);
        movimentacao.setData(DEFAULT_DATA_TIME);
    }

    @Test
    @Transactional
    public void createMovimentacao() throws Exception {
        int databaseSizeBeforeCreate = movimentacaoRepository.findAll().size();

        // Create the Movimentacao

        restMovimentacaoMockMvc.perform(post("/api/movimentacaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(movimentacao)))
                .andExpect(status().isCreated());

        // Validate the Movimentacao in the database
        List<Movimentacao> movimentacaos = movimentacaoRepository.findAll();
        assertThat(movimentacaos).hasSize(databaseSizeBeforeCreate + 1);
        Movimentacao testMovimentacao = movimentacaos.get(movimentacaos.size() - 1);
        assertThat(testMovimentacao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testMovimentacao.getData()).isEqualTo(DEFAULT_DATA_TIME);
    }

    @Test
    @Transactional
    public void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = movimentacaoRepository.findAll().size();
        // set the field null
        movimentacao.setData(null);

        // Create the Movimentacao, which fails.

        restMovimentacaoMockMvc.perform(post("/api/movimentacaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(movimentacao)))
                .andExpect(status().isBadRequest());

        List<Movimentacao> movimentacaos = movimentacaoRepository.findAll();
        assertThat(movimentacaos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMovimentacaos() throws Exception {
        // Initialize the database
        movimentacaoRepository.saveAndFlush(movimentacao);

        // Get all the movimentacaos
        restMovimentacaoMockMvc.perform(get("/api/movimentacaos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(movimentacao.getId().intValue())))
                .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
                .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA_TIME.toString())));
    }

    @Test
    @Transactional
    public void getMovimentacao() throws Exception {
        // Initialize the database
        movimentacaoRepository.saveAndFlush(movimentacao);

        // Get the movimentacao
        restMovimentacaoMockMvc.perform(get("/api/movimentacaos/{id}", movimentacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(movimentacao.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMovimentacao() throws Exception {
        // Get the movimentacao
        restMovimentacaoMockMvc.perform(get("/api/movimentacaos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMovimentacao() throws Exception {
        // Initialize the database
        movimentacaoRepository.saveAndFlush(movimentacao);
        int databaseSizeBeforeUpdate = movimentacaoRepository.findAll().size();

        // Update the movimentacao
        Movimentacao updatedMovimentacao = new Movimentacao();
        updatedMovimentacao.setId(movimentacao.getId());
        updatedMovimentacao.setDescricao(UPDATED_DESCRICAO);
        updatedMovimentacao.setData(UPDATED_DATA_TIME);

        restMovimentacaoMockMvc.perform(put("/api/movimentacaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMovimentacao)))
                .andExpect(status().isOk());

        // Validate the Movimentacao in the database
        List<Movimentacao> movimentacaos = movimentacaoRepository.findAll();
        assertThat(movimentacaos).hasSize(databaseSizeBeforeUpdate);
        Movimentacao testMovimentacao = movimentacaos.get(movimentacaos.size() - 1);
        assertThat(testMovimentacao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testMovimentacao.getData()).isEqualTo(UPDATED_DATA_TIME);
    }

    @Test
    @Transactional
    public void deleteMovimentacao() throws Exception {
        // Initialize the database
        movimentacaoRepository.saveAndFlush(movimentacao);
        int databaseSizeBeforeDelete = movimentacaoRepository.findAll().size();

        // Get the movimentacao
        restMovimentacaoMockMvc.perform(delete("/api/movimentacaos/{id}", movimentacao.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Movimentacao> movimentacaos = movimentacaoRepository.findAll();
        assertThat(movimentacaos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
