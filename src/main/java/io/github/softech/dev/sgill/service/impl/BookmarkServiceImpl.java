package io.github.softech.dev.sgill.service.impl;

import io.github.softech.dev.sgill.domain.Section;
import io.github.softech.dev.sgill.repository.SectionRepository;
import io.github.softech.dev.sgill.repository.search.SectionSearchRepository;
import io.github.softech.dev.sgill.service.BookmarkService;
import io.github.softech.dev.sgill.domain.Bookmark;
import io.github.softech.dev.sgill.repository.BookmarkRepository;
import io.github.softech.dev.sgill.repository.search.BookmarkSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Bookmark.
 */
@Service
@Transactional
public class BookmarkServiceImpl implements BookmarkService {

    private final Logger log = LoggerFactory.getLogger(BookmarkServiceImpl.class);

    private final BookmarkRepository bookmarkRepository;

    private final BookmarkSearchRepository bookmarkSearchRepository;

    private final SectionRepository sectionRepository;

    private final SectionSearchRepository sectionSearchRepository;

    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository, BookmarkSearchRepository bookmarkSearchRepository,
                               SectionRepository sectionRepository, SectionSearchRepository sectionSearchRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.bookmarkSearchRepository = bookmarkSearchRepository;
        this.sectionRepository = sectionRepository;
        this.sectionSearchRepository = sectionSearchRepository;
    }

    /**
     * Save a bookmark.
     *
     * @param bookmark the entity to save
     * @return the persisted entity
     */
    @Override
    public Bookmark save(Bookmark bookmark) {
        log.debug("Request to save Bookmark : {}", bookmark);
        Bookmark result = bookmarkRepository.save(bookmark);
        bookmarkSearchRepository.save(result);
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
    public Page<Bookmark> findAll(Pageable pageable) {
        log.debug("Request to get all Bookmarks");
        return bookmarkRepository.findAll(pageable);
    }


    /**
     * Get one bookmark by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Bookmark> findOne(Long id) {
        log.debug("Request to get Bookmark : {}", id);
        return bookmarkRepository.findById(id);
    }

    /**
     * Delete the bookmark by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Bookmark : {}", id);
        bookmarkRepository.deleteById(id);
        bookmarkSearchRepository.deleteById(id);
    }

    /**
     * Search for the bookmark corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Bookmark> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Bookmarks for query {}", query);
        return bookmarkSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Bookmark> findbySectionName(String name, Pageable pageable) {
        log.debug("Request to search for a page of Bookmarks for query {}", name);
        return bookmarkRepository.findBookmarksByModule(name, pageable);
    }
}
