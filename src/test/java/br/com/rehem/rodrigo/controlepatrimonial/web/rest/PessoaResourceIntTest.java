package br.com.rehem.rodrigo.controlepatrimonial.web.rest;

import br.com.rehem.rodrigo.controlepatrimonial.ControlePatrimonialApp;
import br.com.rehem.rodrigo.controlepatrimonial.domain.Pessoa;
import br.com.rehem.rodrigo.controlepatrimonial.repository.PessoaRepository;

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
 * Test class for the PessoaResource REST controller.
 *
 * @see PessoaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ControlePatrimonialApp.class)
@WebAppConfiguration
@IntegrationTest
public class PessoaResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAA";
    private static final String UPDATED_NOME = "BBBBB";

    private static final Integer DEFAULT_CADASTRO = 1;
    private static final Integer UPDATED_CADASTRO = 2;

    @Inject
    private PessoaRepository pessoaRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPessoaMockMvc;

    private Pessoa pessoa;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PessoaResource pessoaResource = new PessoaResource();
        ReflectionTestUtils.setField(pessoaResource, "pessoaRepository", pessoaRepository);
        this.restPessoaMockMvc = MockMvcBuilders.standaloneSetup(pessoaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pessoa = new Pessoa();
        pessoa.setNome(DEFAULT_NOME);
        pessoa.setCadastro(DEFAULT_CADASTRO);
    }

    @Test
    @Transactional
    public void createPessoa() throws Exception {
        int databaseSizeBeforeCreate = pessoaRepository.findAll().size();

        // Create the Pessoa

        restPessoaMockMvc.perform(post("/api/pessoas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pessoa)))
                .andExpect(status().isCreated());

        // Validate the Pessoa in the database
        List<Pessoa> pessoas = pessoaRepository.findAll();
        assertThat(pessoas).hasSize(databaseSizeBeforeCreate + 1);
        Pessoa testPessoa = pessoas.get(pessoas.size() - 1);
        assertThat(testPessoa.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPessoa.getCadastro()).isEqualTo(DEFAULT_CADASTRO);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pessoaRepository.findAll().size();
        // set the field null
        pessoa.setNome(null);

        // Create the Pessoa, which fails.

        restPessoaMockMvc.perform(post("/api/pessoas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pessoa)))
                .andExpect(status().isBadRequest());

        List<Pessoa> pessoas = pessoaRepository.findAll();
        assertThat(pessoas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPessoas() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoas
        restPessoaMockMvc.perform(get("/api/pessoas?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pessoa.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].cadastro").value(hasItem(DEFAULT_CADASTRO)));
    }

    @Test
    @Transactional
    public void getPessoa() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get the pessoa
        restPessoaMockMvc.perform(get("/api/pessoas/{id}", pessoa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(pessoa.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.cadastro").value(DEFAULT_CADASTRO));
    }

    @Test
    @Transactional
    public void getNonExistingPessoa() throws Exception {
        // Get the pessoa
        restPessoaMockMvc.perform(get("/api/pessoas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePessoa() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);
        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();

        // Update the pessoa
        Pessoa updatedPessoa = new Pessoa();
        updatedPessoa.setId(pessoa.getId());
        updatedPessoa.setNome(UPDATED_NOME);
        updatedPessoa.setCadastro(UPDATED_CADASTRO);

        restPessoaMockMvc.perform(put("/api/pessoas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPessoa)))
                .andExpect(status().isOk());

        // Validate the Pessoa in the database
        List<Pessoa> pessoas = pessoaRepository.findAll();
        assertThat(pessoas).hasSize(databaseSizeBeforeUpdate);
        Pessoa testPessoa = pessoas.get(pessoas.size() - 1);
        assertThat(testPessoa.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPessoa.getCadastro()).isEqualTo(UPDATED_CADASTRO);
    }

    @Test
    @Transactional
    public void deletePessoa() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);
        int databaseSizeBeforeDelete = pessoaRepository.findAll().size();

        // Get the pessoa
        restPessoaMockMvc.perform(delete("/api/pessoas/{id}", pessoa.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Pessoa> pessoas = pessoaRepository.findAll();
        assertThat(pessoas).hasSize(databaseSizeBeforeDelete - 1);
    }
}
