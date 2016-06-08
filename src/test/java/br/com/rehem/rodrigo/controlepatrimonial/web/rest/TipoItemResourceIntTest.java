package br.com.rehem.rodrigo.controlepatrimonial.web.rest;

import br.com.rehem.rodrigo.controlepatrimonial.ControlePatrimonialApp;
import br.com.rehem.rodrigo.controlepatrimonial.domain.TipoItem;
import br.com.rehem.rodrigo.controlepatrimonial.repository.TipoItemRepository;

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
 * Test class for the TipoItemResource REST controller.
 *
 * @see TipoItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ControlePatrimonialApp.class)
@WebAppConfiguration
@IntegrationTest
public class TipoItemResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAA";
    private static final String UPDATED_NOME = "BBBBB";
    private static final String DEFAULT_SIGLA = "AAA";
    private static final String UPDATED_SIGLA = "BBB";
    private static final String DEFAULT_DESCRICAO = "AAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBB";

    @Inject
    private TipoItemRepository tipoItemRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTipoItemMockMvc;

    private TipoItem tipoItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TipoItemResource tipoItemResource = new TipoItemResource();
        ReflectionTestUtils.setField(tipoItemResource, "tipoItemRepository", tipoItemRepository);
        this.restTipoItemMockMvc = MockMvcBuilders.standaloneSetup(tipoItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tipoItem = new TipoItem();
        tipoItem.setNome(DEFAULT_NOME);
        tipoItem.setSigla(DEFAULT_SIGLA);
        tipoItem.setDescricao(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createTipoItem() throws Exception {
        int databaseSizeBeforeCreate = tipoItemRepository.findAll().size();

        // Create the TipoItem

        restTipoItemMockMvc.perform(post("/api/tipo-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tipoItem)))
                .andExpect(status().isCreated());

        // Validate the TipoItem in the database
        List<TipoItem> tipoItems = tipoItemRepository.findAll();
        assertThat(tipoItems).hasSize(databaseSizeBeforeCreate + 1);
        TipoItem testTipoItem = tipoItems.get(tipoItems.size() - 1);
        assertThat(testTipoItem.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTipoItem.getSigla()).isEqualTo(DEFAULT_SIGLA);
        assertThat(testTipoItem.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoItemRepository.findAll().size();
        // set the field null
        tipoItem.setNome(null);

        // Create the TipoItem, which fails.

        restTipoItemMockMvc.perform(post("/api/tipo-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tipoItem)))
                .andExpect(status().isBadRequest());

        List<TipoItem> tipoItems = tipoItemRepository.findAll();
        assertThat(tipoItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSiglaIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoItemRepository.findAll().size();
        // set the field null
        tipoItem.setSigla(null);

        // Create the TipoItem, which fails.

        restTipoItemMockMvc.perform(post("/api/tipo-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tipoItem)))
                .andExpect(status().isBadRequest());

        List<TipoItem> tipoItems = tipoItemRepository.findAll();
        assertThat(tipoItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipoItems() throws Exception {
        // Initialize the database
        tipoItemRepository.saveAndFlush(tipoItem);

        // Get all the tipoItems
        restTipoItemMockMvc.perform(get("/api/tipo-items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tipoItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].sigla").value(hasItem(DEFAULT_SIGLA.toString())))
                .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getTipoItem() throws Exception {
        // Initialize the database
        tipoItemRepository.saveAndFlush(tipoItem);

        // Get the tipoItem
        restTipoItemMockMvc.perform(get("/api/tipo-items/{id}", tipoItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tipoItem.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.sigla").value(DEFAULT_SIGLA.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTipoItem() throws Exception {
        // Get the tipoItem
        restTipoItemMockMvc.perform(get("/api/tipo-items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipoItem() throws Exception {
        // Initialize the database
        tipoItemRepository.saveAndFlush(tipoItem);
        int databaseSizeBeforeUpdate = tipoItemRepository.findAll().size();

        // Update the tipoItem
        TipoItem updatedTipoItem = new TipoItem();
        updatedTipoItem.setId(tipoItem.getId());
        updatedTipoItem.setNome(UPDATED_NOME);
        updatedTipoItem.setSigla(UPDATED_SIGLA);
        updatedTipoItem.setDescricao(UPDATED_DESCRICAO);

        restTipoItemMockMvc.perform(put("/api/tipo-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTipoItem)))
                .andExpect(status().isOk());

        // Validate the TipoItem in the database
        List<TipoItem> tipoItems = tipoItemRepository.findAll();
        assertThat(tipoItems).hasSize(databaseSizeBeforeUpdate);
        TipoItem testTipoItem = tipoItems.get(tipoItems.size() - 1);
        assertThat(testTipoItem.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTipoItem.getSigla()).isEqualTo(UPDATED_SIGLA);
        assertThat(testTipoItem.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void deleteTipoItem() throws Exception {
        // Initialize the database
        tipoItemRepository.saveAndFlush(tipoItem);
        int databaseSizeBeforeDelete = tipoItemRepository.findAll().size();

        // Get the tipoItem
        restTipoItemMockMvc.perform(delete("/api/tipo-items/{id}", tipoItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TipoItem> tipoItems = tipoItemRepository.findAll();
        assertThat(tipoItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
