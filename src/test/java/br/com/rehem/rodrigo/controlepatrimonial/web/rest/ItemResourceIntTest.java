package br.com.rehem.rodrigo.controlepatrimonial.web.rest;

import br.com.rehem.rodrigo.controlepatrimonial.ControlePatrimonialApp;
import br.com.rehem.rodrigo.controlepatrimonial.domain.Item;
import br.com.rehem.rodrigo.controlepatrimonial.repository.ItemRepository;

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

import br.com.rehem.rodrigo.controlepatrimonial.domain.enumeration.EstadoItem;

/**
 * Test class for the ItemResource REST controller.
 *
 * @see ItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ControlePatrimonialApp.class)
@WebAppConfiguration
@IntegrationTest
public class ItemResourceIntTest {

    private static final String DEFAULT_SERIAL = "AAAAA";
    private static final String UPDATED_SERIAL = "BBBBB";
    private static final String DEFAULT_MODELO = "AAAAA";
    private static final String UPDATED_MODELO = "BBBBB";

    private static final EstadoItem DEFAULT_ESTADO = EstadoItem.NOVO;
    private static final EstadoItem UPDATED_ESTADO = EstadoItem.USADO;
    private static final String DEFAULT_NUMERO = "AAAAA";
    private static final String UPDATED_NUMERO = "BBBBB";

    @Inject
    private ItemRepository itemRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restItemMockMvc;

    private Item item;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ItemResource itemResource = new ItemResource();
        ReflectionTestUtils.setField(itemResource, "itemRepository", itemRepository);
        this.restItemMockMvc = MockMvcBuilders.standaloneSetup(itemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        item = new Item();
        item.setSerial(DEFAULT_SERIAL);
        item.setModelo(DEFAULT_MODELO);
        item.setEstado(DEFAULT_ESTADO);
        item.setNumero(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    public void createItem() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().size();

        // Create the Item

        restItemMockMvc.perform(post("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isCreated());

        // Validate the Item in the database
        List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(databaseSizeBeforeCreate + 1);
        Item testItem = items.get(items.size() - 1);
        assertThat(testItem.getSerial()).isEqualTo(DEFAULT_SERIAL);
        assertThat(testItem.getModelo()).isEqualTo(DEFAULT_MODELO);
        assertThat(testItem.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testItem.getNumero()).isEqualTo(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    public void checkModeloIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setModelo(null);

        // Create the Item, which fails.

        restItemMockMvc.perform(post("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isBadRequest());

        List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setEstado(null);

        // Create the Item, which fails.

        restItemMockMvc.perform(post("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isBadRequest());

        List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItems() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the items
        restItemMockMvc.perform(get("/api/items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
                .andExpect(jsonPath("$.[*].serial").value(hasItem(DEFAULT_SERIAL.toString())))
                .andExpect(jsonPath("$.[*].modelo").value(hasItem(DEFAULT_MODELO.toString())))
                .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
                .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())));
    }

    @Test
    @Transactional
    public void getItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get the item
        restItemMockMvc.perform(get("/api/items/{id}", item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(item.getId().intValue()))
            .andExpect(jsonPath("$.serial").value(DEFAULT_SERIAL.toString()))
            .andExpect(jsonPath("$.modelo").value(DEFAULT_MODELO.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingItem() throws Exception {
        // Get the item
        restItemMockMvc.perform(get("/api/items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item
        Item updatedItem = new Item();
        updatedItem.setId(item.getId());
        updatedItem.setSerial(UPDATED_SERIAL);
        updatedItem.setModelo(UPDATED_MODELO);
        updatedItem.setEstado(UPDATED_ESTADO);
        updatedItem.setNumero(UPDATED_NUMERO);

        restItemMockMvc.perform(put("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedItem)))
                .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(databaseSizeBeforeUpdate);
        Item testItem = items.get(items.size() - 1);
        assertThat(testItem.getSerial()).isEqualTo(UPDATED_SERIAL);
        assertThat(testItem.getModelo()).isEqualTo(UPDATED_MODELO);
        assertThat(testItem.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testItem.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void deleteItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);
        int databaseSizeBeforeDelete = itemRepository.findAll().size();

        // Get the item
        restItemMockMvc.perform(delete("/api/items/{id}", item.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(databaseSizeBeforeDelete - 1);
    }
}
