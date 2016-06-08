package br.com.rehem.rodrigo.controlepatrimonial.web.rest;

import br.com.rehem.rodrigo.controlepatrimonial.ControlePatrimonialApp;
import br.com.rehem.rodrigo.controlepatrimonial.domain.Documento;
import br.com.rehem.rodrigo.controlepatrimonial.repository.DocumentoRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DocumentoResource REST controller.
 *
 * @see DocumentoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ControlePatrimonialApp.class)
@WebAppConfiguration
@IntegrationTest
public class DocumentoResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBB";

    private static final byte[] DEFAULT_ANEXO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ANEXO = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_ANEXO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ANEXO_CONTENT_TYPE = "image/png";

    @Inject
    private DocumentoRepository documentoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDocumentoMockMvc;

    private Documento documento;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DocumentoResource documentoResource = new DocumentoResource();
        ReflectionTestUtils.setField(documentoResource, "documentoRepository", documentoRepository);
        this.restDocumentoMockMvc = MockMvcBuilders.standaloneSetup(documentoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        documento = new Documento();
        documento.setDescricao(DEFAULT_DESCRICAO);
        documento.setAnexo(DEFAULT_ANEXO);
        documento.setAnexoContentType(DEFAULT_ANEXO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createDocumento() throws Exception {
        int databaseSizeBeforeCreate = documentoRepository.findAll().size();

        // Create the Documento

        restDocumentoMockMvc.perform(post("/api/documentos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(documento)))
                .andExpect(status().isCreated());

        // Validate the Documento in the database
        List<Documento> documentos = documentoRepository.findAll();
        assertThat(documentos).hasSize(databaseSizeBeforeCreate + 1);
        Documento testDocumento = documentos.get(documentos.size() - 1);
        assertThat(testDocumento.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testDocumento.getAnexo()).isEqualTo(DEFAULT_ANEXO);
        assertThat(testDocumento.getAnexoContentType()).isEqualTo(DEFAULT_ANEXO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void checkAnexoIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentoRepository.findAll().size();
        // set the field null
        documento.setAnexo(null);

        // Create the Documento, which fails.

        restDocumentoMockMvc.perform(post("/api/documentos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(documento)))
                .andExpect(status().isBadRequest());

        List<Documento> documentos = documentoRepository.findAll();
        assertThat(documentos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocumentos() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentos
        restDocumentoMockMvc.perform(get("/api/documentos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(documento.getId().intValue())))
                .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
                .andExpect(jsonPath("$.[*].anexoContentType").value(hasItem(DEFAULT_ANEXO_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].anexo").value(hasItem(Base64Utils.encodeToString(DEFAULT_ANEXO))));
    }

    @Test
    @Transactional
    public void getDocumento() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get the documento
        restDocumentoMockMvc.perform(get("/api/documentos/{id}", documento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(documento.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.anexoContentType").value(DEFAULT_ANEXO_CONTENT_TYPE))
            .andExpect(jsonPath("$.anexo").value(Base64Utils.encodeToString(DEFAULT_ANEXO)));
    }

    @Test
    @Transactional
    public void getNonExistingDocumento() throws Exception {
        // Get the documento
        restDocumentoMockMvc.perform(get("/api/documentos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocumento() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);
        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();

        // Update the documento
        Documento updatedDocumento = new Documento();
        updatedDocumento.setId(documento.getId());
        updatedDocumento.setDescricao(UPDATED_DESCRICAO);
        updatedDocumento.setAnexo(UPDATED_ANEXO);
        updatedDocumento.setAnexoContentType(UPDATED_ANEXO_CONTENT_TYPE);

        restDocumentoMockMvc.perform(put("/api/documentos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDocumento)))
                .andExpect(status().isOk());

        // Validate the Documento in the database
        List<Documento> documentos = documentoRepository.findAll();
        assertThat(documentos).hasSize(databaseSizeBeforeUpdate);
        Documento testDocumento = documentos.get(documentos.size() - 1);
        assertThat(testDocumento.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testDocumento.getAnexo()).isEqualTo(UPDATED_ANEXO);
        assertThat(testDocumento.getAnexoContentType()).isEqualTo(UPDATED_ANEXO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void deleteDocumento() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);
        int databaseSizeBeforeDelete = documentoRepository.findAll().size();

        // Get the documento
        restDocumentoMockMvc.perform(delete("/api/documentos/{id}", documento.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Documento> documentos = documentoRepository.findAll();
        assertThat(documentos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
