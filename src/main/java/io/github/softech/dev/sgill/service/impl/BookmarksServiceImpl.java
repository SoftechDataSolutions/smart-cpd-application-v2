package io.github.softech.dev.sgill.service.impl;

import io.github.softech.dev.sgill.service.BookmarksService;
import io.github.softech.dev.sgill.domain.Bookmarks;
import io.github.softech.dev.sgill.repository.BookmarksRepository;
import io.github.softech.dev.sgill.repository.search.BookmarksSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Bookmarks.
 */
@Service
@Transactional
public class BookmarksServiceImpl implements BookmarksService {

    private final Logger log = LoggerFactory.getLogger(BookmarksServiceImpl.class);

    private final BookmarksRepository bookmarksRepository;

    private final BookmarksSearchRepository bookmarksSearchRepository;

    public BookmarksServiceImpl(BookmarksRepository bookmarksRepository, BookmarksSearchRepository bookmarksSearchRepository) {
        this.bookmarksRepository = bookmarksRepository;
        this.bookmarksSearchRepository = bookmarksSearchRepository;
    }

    /**
     * Save a bookmarks.
     *
     * @param bookmarks the entity to save
     * @return the persisted entity
     */
    @Override
    public Bookmarks save(Bookmarks bookmarks) {
        log.debug("Request to save Bookmarks : {}", bookmarks);        Bookmarks result = bookmarksRepository.save(bookmarks);
        bookmarksSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the bookmarks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Bookmarks> findAll(Pageable pageable) {
        log.debug("Request to get all Bookmarks");
        return bookmarksRepository.findAll(pageable);
    }


    /**
     * Get one bookmarks by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Bookmarks> findOne(Long id) {
        log.debug("Request to get Bookmarks : {}", id);
        return bookmarksRepository.findById(id);
    }

    /**
     * Delete the bookmarks by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Bookmarks : {}", id);
        bookmarksRepository.deleteById(id);
        bookmarksSearchRepository.deleteById(id);
    }

    /**
     * Search for the bookmarks corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Bookmarks> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Bookmarks for query {}", query);
        return bookmarksSearchRepository.search(queryStringQuery(query), pageable);    }
}
