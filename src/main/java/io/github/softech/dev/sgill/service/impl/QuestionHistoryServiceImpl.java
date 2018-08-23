package io.github.softech.dev.sgill.service.impl;

import io.github.softech.dev.sgill.repository.search.QuestionHistorySearchRepository;
import io.github.softech.dev.sgill.service.QuestionhistoryService;
import io.github.softech.dev.sgill.domain.Questionhistory;
import io.github.softech.dev.sgill.repository.QuestionHistoryRepository;
import io.github.softech.dev.sgill.repository.search.QuestionhistorySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Questionhistory.
 */
@Service
@Transactional
public class QuestionHistoryServiceImpl implements QuestionHistoryService {

    private final Logger log = LoggerFactory.getLogger(QuestionHistoryServiceImpl.class);

    private final QuestionHistoryRepository questionhistoryRepository;

    private final QuestionHistorySearchRepository questionhistorySearchRepository;

    public QuestionhistoryServiceImpl(QuestionHistoryRepository questionhistoryRepository, QuestionHistorySearchRepository questionhistorySearchRepository) {
        this.questionhistoryRepository = questionhistoryRepository;
        this.questionhistorySearchRepository = questionhistorySearchRepository;
    }

    /**
     * Save a questionhistory.
     *
     * @param questionhistory the entity to save
     * @return the persisted entity
     */
    @Override
    public Questionhistory save(Questionhistory questionhistory) {
        log.debug("Request to save Questionhistory : {}", questionhistory);        Questionhistory result = questionhistoryRepository.save(questionhistory);
        questionhistorySearchRepository.save(result);
        return result;
    }

    /**
     * Get all the questionhistories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Questionhistory> findAll(Pageable pageable) {
        log.debug("Request to get all Questionhistories");
        return questionhistoryRepository.findAll(pageable);
    }


    /**
     * Get one questionhistory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Questionhistory> findOne(Long id) {
        log.debug("Request to get Questionhistory : {}", id);
        return questionhistoryRepository.findById(id);
    }

    /**
     * Delete the questionhistory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Questionhistory : {}", id);
        questionhistoryRepository.deleteById(id);
        questionhistorySearchRepository.deleteById(id);
    }

    /**
     * Search for the questionhistory corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Questionhistory> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Questionhistories for query {}", query);
        return questionhistorySearchRepository.search(queryStringQuery(query), pageable);    }
}
