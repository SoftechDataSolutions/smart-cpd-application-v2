package io.github.softech.dev.sgill.web.rest;

import io.github.softech.dev.sgill.SmartCpdApp;

import io.github.softech.dev.sgill.domain.Bookmarks;
import io.github.softech.dev.sgill.domain.Section;
import io.github.softech.dev.sgill.repository.BookmarksRepository;
import io.github.softech.dev.sgill.repository.search.BookmarksSearchRepository;
import io.github.softech.dev.sgill.service.BookmarksService;
import io.github.softech.dev.sgill.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static io.github.softech.dev.sgill.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BookmarksResource REST controller.
 *
 * @see BookmarksResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartCpdApp.class)
public class BookmarksResourceIntTest {

    private static final String DEFAULT_BOOKMARK = "AAAAAAAAAA";
    private static final String UPDATED_BOOKMARK = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    @Autowired
    private BookmarksRepository bookmarksRepository;

    

    @Autowired
    private BookmarksService bookmarksService;

    /**
     * This repository is mocked in the io.github.softech.dev.sgill.repository.search test package.
     *
     * @see io.github.softech.dev.sgill.repository.search.BookmarksSearchRepositoryMockConfiguration
     */
    @Autowired
    private BookmarksSearchRepository mockBookmarksSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBookmarksMockMvc;

    private Bookmarks bookmarks;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BookmarksResource bookmarksResource = new BookmarksResource(bookmarksService);
        this.restBookmarksMockMvc = MockMvcBuilders.standaloneSetup(bookmarksResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bookmarks createEntity(EntityManager em) {
        Bookmarks bookmarks = new Bookmarks()
            .bookmark(DEFAULT_BOOKMARK)
            .text(DEFAULT_TEXT);
        // Add required entity
        Section section = SectionResourceIntTest.createEntity(em);
        em.persist(section);
        em.flush();
        bookmarks.setSection(section);
        return bookmarks;
    }

    @Before
    public void initTest() {
        bookmarks = createEntity(em);
    }

    @Test
    @Transactional
    public void createBookmarks() throws Exception {
        int databaseSizeBeforeCreate = bookmarksRepository.findAll().size();

        // Create the Bookmarks
        restBookmarksMockMvc.perform(post("/api/bookmarks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookmarks)))
            .andExpect(status().isCreated());

        // Validate the Bookmarks in the database
        List<Bookmarks> bookmarksList = bookmarksRepository.findAll();
        assertThat(bookmarksList).hasSize(databaseSizeBeforeCreate + 1);
        Bookmarks testBookmarks = bookmarksList.get(bookmarksList.size() - 1);
        assertThat(testBookmarks.getBookmark()).isEqualTo(DEFAULT_BOOKMARK);
        assertThat(testBookmarks.getText()).isEqualTo(DEFAULT_TEXT);

        // Validate the Bookmarks in Elasticsearch
        verify(mockBookmarksSearchRepository, times(1)).save(testBookmarks);
    }

    @Test
    @Transactional
    public void createBookmarksWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bookmarksRepository.findAll().size();

        // Create the Bookmarks with an existing ID
        bookmarks.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookmarksMockMvc.perform(post("/api/bookmarks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookmarks)))
            .andExpect(status().isBadRequest());

        // Validate the Bookmarks in the database
        List<Bookmarks> bookmarksList = bookmarksRepository.findAll();
        assertThat(bookmarksList).hasSize(databaseSizeBeforeCreate);

        // Validate the Bookmarks in Elasticsearch
        verify(mockBookmarksSearchRepository, times(0)).save(bookmarks);
    }

    @Test
    @Transactional
    public void checkBookmarkIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookmarksRepository.findAll().size();
        // set the field null
        bookmarks.setBookmark(null);

        // Create the Bookmarks, which fails.

        restBookmarksMockMvc.perform(post("/api/bookmarks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookmarks)))
            .andExpect(status().isBadRequest());

        List<Bookmarks> bookmarksList = bookmarksRepository.findAll();
        assertThat(bookmarksList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookmarksRepository.findAll().size();
        // set the field null
        bookmarks.setText(null);

        // Create the Bookmarks, which fails.

        restBookmarksMockMvc.perform(post("/api/bookmarks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookmarks)))
            .andExpect(status().isBadRequest());

        List<Bookmarks> bookmarksList = bookmarksRepository.findAll();
        assertThat(bookmarksList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBookmarks() throws Exception {
        // Initialize the database
        bookmarksRepository.saveAndFlush(bookmarks);

        // Get all the bookmarksList
        restBookmarksMockMvc.perform(get("/api/bookmarks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookmarks.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookmark").value(hasItem(DEFAULT_BOOKMARK.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }
    

    @Test
    @Transactional
    public void getBookmarks() throws Exception {
        // Initialize the database
        bookmarksRepository.saveAndFlush(bookmarks);

        // Get the bookmarks
        restBookmarksMockMvc.perform(get("/api/bookmarks/{id}", bookmarks.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bookmarks.getId().intValue()))
            .andExpect(jsonPath("$.bookmark").value(DEFAULT_BOOKMARK.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingBookmarks() throws Exception {
        // Get the bookmarks
        restBookmarksMockMvc.perform(get("/api/bookmarks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBookmarks() throws Exception {
        // Initialize the database
        bookmarksService.save(bookmarks);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockBookmarksSearchRepository);

        int databaseSizeBeforeUpdate = bookmarksRepository.findAll().size();

        // Update the bookmarks
        Bookmarks updatedBookmarks = bookmarksRepository.findById(bookmarks.getId()).get();
        // Disconnect from session so that the updates on updatedBookmarks are not directly saved in db
        em.detach(updatedBookmarks);
        updatedBookmarks
            .bookmark(UPDATED_BOOKMARK)
            .text(UPDATED_TEXT);

        restBookmarksMockMvc.perform(put("/api/bookmarks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBookmarks)))
            .andExpect(status().isOk());

        // Validate the Bookmarks in the database
        List<Bookmarks> bookmarksList = bookmarksRepository.findAll();
        assertThat(bookmarksList).hasSize(databaseSizeBeforeUpdate);
        Bookmarks testBookmarks = bookmarksList.get(bookmarksList.size() - 1);
        assertThat(testBookmarks.getBookmark()).isEqualTo(UPDATED_BOOKMARK);
        assertThat(testBookmarks.getText()).isEqualTo(UPDATED_TEXT);

        // Validate the Bookmarks in Elasticsearch
        verify(mockBookmarksSearchRepository, times(1)).save(testBookmarks);
    }

    @Test
    @Transactional
    public void updateNonExistingBookmarks() throws Exception {
        int databaseSizeBeforeUpdate = bookmarksRepository.findAll().size();

        // Create the Bookmarks

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restBookmarksMockMvc.perform(put("/api/bookmarks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookmarks)))
            .andExpect(status().isBadRequest());

        // Validate the Bookmarks in the database
        List<Bookmarks> bookmarksList = bookmarksRepository.findAll();
        assertThat(bookmarksList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Bookmarks in Elasticsearch
        verify(mockBookmarksSearchRepository, times(0)).save(bookmarks);
    }

    @Test
    @Transactional
    public void deleteBookmarks() throws Exception {
        // Initialize the database
        bookmarksService.save(bookmarks);

        int databaseSizeBeforeDelete = bookmarksRepository.findAll().size();

        // Get the bookmarks
        restBookmarksMockMvc.perform(delete("/api/bookmarks/{id}", bookmarks.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Bookmarks> bookmarksList = bookmarksRepository.findAll();
        assertThat(bookmarksList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Bookmarks in Elasticsearch
        verify(mockBookmarksSearchRepository, times(1)).deleteById(bookmarks.getId());
    }

    @Test
    @Transactional
    public void searchBookmarks() throws Exception {
        // Initialize the database
        bookmarksService.save(bookmarks);
        when(mockBookmarksSearchRepository.search(queryStringQuery("id:" + bookmarks.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(bookmarks), PageRequest.of(0, 1), 1));
        // Search the bookmarks
        restBookmarksMockMvc.perform(get("/api/_search/bookmarks?query=id:" + bookmarks.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookmarks.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookmark").value(hasItem(DEFAULT_BOOKMARK.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bookmarks.class);
        Bookmarks bookmarks1 = new Bookmarks();
        bookmarks1.setId(1L);
        Bookmarks bookmarks2 = new Bookmarks();
        bookmarks2.setId(bookmarks1.getId());
        assertThat(bookmarks1).isEqualTo(bookmarks2);
        bookmarks2.setId(2L);
        assertThat(bookmarks1).isNotEqualTo(bookmarks2);
        bookmarks1.setId(null);
        assertThat(bookmarks1).isNotEqualTo(bookmarks2);
    }
}
