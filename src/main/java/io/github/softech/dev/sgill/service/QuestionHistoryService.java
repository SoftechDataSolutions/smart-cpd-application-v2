package io.github.softech.dev.sgill.service;

import io.github.softech.dev.sgill.domain.QuestionHistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing QuestionHistory.
 */
public interface QuestionHistoryService {

    /**
     * Save a questionhistory.
     *
     * @param questionhistory the entity to save
     * @return the persisted entity
     */
    QuestionHistory save(QuestionHistory questionhistory);

    /**
     * Get all the questionhistories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<QuestionHistory> findAll(Pageable pageable);


    /**
     * Get the "id" questionhistory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<QuestionHistory> findOne(Long id);

    /**
     * Delete the "id" questionhistory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the questionhistory corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<QuestionHistory> search(String query, Pageable pageable);
}
