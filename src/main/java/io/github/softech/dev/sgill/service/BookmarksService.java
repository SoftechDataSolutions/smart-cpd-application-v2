package io.github.softech.dev.sgill.service;

import io.github.softech.dev.sgill.domain.Bookmarks;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Bookmarks.
 */
public interface BookmarksService {

    /**
     * Save a bookmarks.
     *
     * @param bookmarks the entity to save
     * @return the persisted entity
     */
    Bookmarks save(Bookmarks bookmarks);

    /**
     * Get all the bookmarks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Bookmarks> findAll(Pageable pageable);


    /**
     * Get the "id" bookmarks.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Bookmarks> findOne(Long id);

    /**
     * Delete the "id" bookmarks.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the bookmarks corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Bookmarks> search(String query, Pageable pageable);
}
