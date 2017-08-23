package br.com.rehem.rodrigo.controlepatrimonial.web.rest;

import br.com.rehem.rodrigo.controlepatrimonial.ControlePatrimonialApp;
import br.com.rehem.rodrigo.controlepatrimonial.domain.UnidadeJudiciaria;
import br.com.rehem.rodrigo.controlepatrimonial.repository.UnidadeJudiciariaRepository;

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

import br.com.rehem.rodrigo.controlepatrimonial.domain.enumeration.Comarca;

/**
 * Test class for the UnidadeJudiciariaResource REST controller.
 *
 * @see UnidadeJudiciariaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ControlePatrimonialApp.class)
@WebAppConfiguration
@IntegrationTest
public class UnidadeJudiciariaResourceIntTest {

    private static final String DEFAULT_COJ = "AAAAA";
    private static final String UPDATED_COJ = "BBBBB";

    private static final Comarca DEFAULT_COMARCA = Comarca.SALVADOR;
    private static final Comarca UPDATED_COMARCA = Comarca.AMARGOSA;
    private static final String DEFAULT_UNIDADE = "AAAAA";
    private static final String UPDATED_UNIDADE = "BBBBB";

    @Inject
    private UnidadeJudiciariaRepository unidadeJudiciariaRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUnidadeJudiciariaMockMvc;

    private UnidadeJudiciaria unidadeJudiciaria;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UnidadeJudiciariaResource unidadeJudiciariaResource = new UnidadeJudiciariaResource();
        ReflectionTestUtils.setField(unidadeJudiciariaResource, "unidadeJudiciariaRepository", unidadeJudiciariaRepository);
        this.restUnidadeJudiciariaMockMvc = MockMvcBuilders.standaloneSetup(unidadeJudiciariaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        unidadeJudiciaria = new UnidadeJudiciaria();
        unidadeJudiciaria.setCoj(DEFAULT_COJ);
        unidadeJudiciaria.setComarca(DEFAULT_COMARCA);
        unidadeJudiciaria.setUnidade(DEFAULT_UNIDADE);
    }

    @Test
    @Transactional
    public void createUnidadeJudiciaria() throws Exception {
        int databaseSizeBeforeCreate = unidadeJudiciariaRepository.findAll().size();

        // Create the UnidadeJudiciaria

        restUnidadeJudiciariaMockMvc.perform(post("/api/unidade-judiciarias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(unidadeJudiciaria)))
                .andExpect(status().isCreated());

        // Validate the UnidadeJudiciaria in the database
        List<UnidadeJudiciaria> unidadeJudiciarias = unidadeJudiciariaRepository.findAll();
        assertThat(unidadeJudiciarias).hasSize(databaseSizeBeforeCreate + 1);
        UnidadeJudiciaria testUnidadeJudiciaria = unidadeJudiciarias.get(unidadeJudiciarias.size() - 1);
        assertThat(testUnidadeJudiciaria.getCoj()).isEqualTo(DEFAULT_COJ);
        assertThat(testUnidadeJudiciaria.getComarca()).isEqualTo(DEFAULT_COMARCA);
        assertThat(testUnidadeJudiciaria.getUnidade()).isEqualTo(DEFAULT_UNIDADE);
    }

    @Test
    @Transactional
    public void checkCojIsRequired() throws Exception {
        int databaseSizeBeforeTest = unidadeJudiciariaRepository.findAll().size();
        // set the field null
        unidadeJudiciaria.setCoj(null);

        // Create the UnidadeJudiciaria, which fails.

        restUnidadeJudiciariaMockMvc.perform(post("/api/unidade-judiciarias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(unidadeJudiciaria)))
                .andExpect(status().isBadRequest());

        List<UnidadeJudiciaria> unidadeJudiciarias = unidadeJudiciariaRepository.findAll();
        assertThat(unidadeJudiciarias).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkComarcaIsRequired() throws Exception {
        int databaseSizeBeforeTest = unidadeJudiciariaRepository.findAll().size();
        // set the field null
        unidadeJudiciaria.setComarca(null);

        // Create the UnidadeJudiciaria, which fails.

        restUnidadeJudiciariaMockMvc.perform(post("/api/unidade-judiciarias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(unidadeJudiciaria)))
                .andExpect(status().isBadRequest());

        List<UnidadeJudiciaria> unidadeJudiciarias = unidadeJudiciariaRepository.findAll();
        assertThat(unidadeJudiciarias).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = unidadeJudiciariaRepository.findAll().size();
        // set the field null
        unidadeJudiciaria.setUnidade(null);

        // Create the UnidadeJudiciaria, which fails.

        restUnidadeJudiciariaMockMvc.perform(post("/api/unidade-judiciarias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(unidadeJudiciaria)))
                .andExpect(status().isBadRequest());

        List<UnidadeJudiciaria> unidadeJudiciarias = unidadeJudiciariaRepository.findAll();
        assertThat(unidadeJudiciarias).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUnidadeJudiciarias() throws Exception {
        // Initialize the database
        unidadeJudiciariaRepository.saveAndFlush(unidadeJudiciaria);

        // Get all the unidadeJudiciarias
        restUnidadeJudiciariaMockMvc.perform(get("/api/unidade-judiciarias?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(unidadeJudiciaria.getId().intValue())))
                .andExpect(jsonPath("$.[*].coj").value(hasItem(DEFAULT_COJ.toString())))
                .andExpect(jsonPath("$.[*].comarca").value(hasItem(DEFAULT_COMARCA.toString())))
                .andExpect(jsonPath("$.[*].unidade").value(hasItem(DEFAULT_UNIDADE.toString())));
    }

    @Test
    @Transactional
    public void getUnidadeJudiciaria() throws Exception {
        // Initialize the database
        unidadeJudiciariaRepository.saveAndFlush(unidadeJudiciaria);

        // Get the unidadeJudiciaria
        restUnidadeJudiciariaMockMvc.perform(get("/api/unidade-judiciarias/{id}", unidadeJudiciaria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(unidadeJudiciaria.getId().intValue()))
            .andExpect(jsonPath("$.coj").value(DEFAULT_COJ.toString()))
            .andExpect(jsonPath("$.comarca").value(DEFAULT_COMARCA.toString()))
            .andExpect(jsonPath("$.unidade").value(DEFAULT_UNIDADE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUnidadeJudiciaria() throws Exception {
        // Get the unidadeJudiciaria
        restUnidadeJudiciariaMockMvc.perform(get("/api/unidade-judiciarias/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUnidadeJudiciaria() throws Exception {
        // Initialize the database
        unidadeJudiciariaRepository.saveAndFlush(unidadeJudiciaria);
        int databaseSizeBeforeUpdate = unidadeJudiciariaRepository.findAll().size();

        // Update the unidadeJudiciaria
        UnidadeJudiciaria updatedUnidadeJudiciaria = new UnidadeJudiciaria();
        updatedUnidadeJudiciaria.setId(unidadeJudiciaria.getId());
        updatedUnidadeJudiciaria.setCoj(UPDATED_COJ);
        updatedUnidadeJudiciaria.setComarca(UPDATED_COMARCA);
        updatedUnidadeJudiciaria.setUnidade(UPDATED_UNIDADE);

        restUnidadeJudiciariaMockMvc.perform(put("/api/unidade-judiciarias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUnidadeJudiciaria)))
                .andExpect(status().isOk());

        // Validate the UnidadeJudiciaria in the database
        List<UnidadeJudiciaria> unidadeJudiciarias = unidadeJudiciariaRepository.findAll();
        assertThat(unidadeJudiciarias).hasSize(databaseSizeBeforeUpdate);
        UnidadeJudiciaria testUnidadeJudiciaria = unidadeJudiciarias.get(unidadeJudiciarias.size() - 1);
        assertThat(testUnidadeJudiciaria.getCoj()).isEqualTo(UPDATED_COJ);
        assertThat(testUnidadeJudiciaria.getComarca()).isEqualTo(UPDATED_COMARCA);
        assertThat(testUnidadeJudiciaria.getUnidade()).isEqualTo(UPDATED_UNIDADE);
    }

    @Test
    @Transactional
    public void deleteUnidadeJudiciaria() throws Exception {
        // Initialize the database
        unidadeJudiciariaRepository.saveAndFlush(unidadeJudiciaria);
        int databaseSizeBeforeDelete = unidadeJudiciariaRepository.findAll().size();

        // Get the unidadeJudiciaria
        restUnidadeJudiciariaMockMvc.perform(delete("/api/unidade-judiciarias/{id}", unidadeJudiciaria.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UnidadeJudiciaria> unidadeJudiciarias = unidadeJudiciariaRepository.findAll();
        assertThat(unidadeJudiciarias).hasSize(databaseSizeBeforeDelete - 1);
    }
}
