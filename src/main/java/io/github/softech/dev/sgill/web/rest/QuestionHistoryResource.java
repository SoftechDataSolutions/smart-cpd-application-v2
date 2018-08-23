package io.github.softech.dev.sgill.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.softech.dev.sgill.domain.QuestionHistory;
import io.github.softech.dev.sgill.service.QuestionHistoryService;
import io.github.softech.dev.sgill.web.rest.errors.BadRequestAlertException;
import io.github.softech.dev.sgill.web.rest.util.HeaderUtil;
import io.github.softech.dev.sgill.web.rest.util.PaginationUtil;
import io.github.softech.dev.sgill.service.dto.QuestionHistoryCriteria;
import io.github.softech.dev.sgill.service.QuestionHistoryQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing QuestionHistory.
 */
@RestController
@RequestMapping("/api")
public class QuestionHistoryResource {

    private final Logger log = LoggerFactory.getLogger(QuestionHistoryResource.class);

    private static final String ENTITY_NAME = "questionhistory";

    private final QuestionHistoryService questionhistoryService;

    private final QuestionHistoryQueryService questionhistoryQueryService;

    public QuestionHistoryResource(QuestionHistoryService questionhistoryService, QuestionHistoryQueryService questionhistoryQueryService) {
        this.questionhistoryService = questionhistoryService;
        this.questionhistoryQueryService = questionhistoryQueryService;
    }

    /**
     * POST  /questionhistories : Create a new questionhistory.
     *
     * @param questionhistory the questionhistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new questionhistory, or with status 400 (Bad Request) if the questionhistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/questionhistories")
    @Timed
    public ResponseEntity<QuestionHistory> createQuestionHistory(@Valid @RequestBody QuestionHistory questionhistory) throws URISyntaxException {
        log.debug("REST request to save QuestionHistory : {}", questionhistory);
        if (questionhistory.getId() != null) {
            throw new BadRequestAlertException("A new questionhistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestionHistory result = questionhistoryService.save(questionhistory);
        return ResponseEntity.created(new URI("/api/questionhistories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /questionhistories : Updates an existing questionhistory.
     *
     * @param questionhistory the questionhistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated questionhistory,
     * or with status 400 (Bad Request) if the questionhistory is not valid,
     * or with status 500 (Internal Server Error) if the questionhistory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/questionhistories")
    @Timed
    public ResponseEntity<QuestionHistory> updateQuestionHistory(@Valid @RequestBody QuestionHistory questionhistory) throws URISyntaxException {
        log.debug("REST request to update QuestionHistory : {}", questionhistory);
        if (questionhistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        QuestionHistory result = questionhistoryService.save(questionhistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, questionhistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /questionhistories : get all the questionhistories.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of questionhistories in body
     */
    @GetMapping("/questionhistories")
    @Timed
    public ResponseEntity<List<QuestionHistory>> getAllQuestionhistories(QuestionHistoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Questionhistories by criteria: {}", criteria);
        Page<QuestionHistory> page = questionhistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/questionhistories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /questionhistories/:id : get the "id" questionhistory.
     *
     * @param id the id of the questionhistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the questionhistory, or with status 404 (Not Found)
     */
    @GetMapping("/questionhistories/{id}")
    @Timed
    public ResponseEntity<QuestionHistory> getQuestionHistory(@PathVariable Long id) {
        log.debug("REST request to get QuestionHistory : {}", id);
        Optional<QuestionHistory> questionhistory = questionhistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionhistory);
    }

    /**
     * DELETE  /questionhistories/:id : delete the "id" questionhistory.
     *
     * @param id the id of the questionhistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/questionhistories/{id}")
    @Timed
    public ResponseEntity<Void> deleteQuestionHistory(@PathVariable Long id) {
        log.debug("REST request to delete QuestionHistory : {}", id);
        questionhistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/questionhistories?query=:query : search for the questionhistory corresponding
     * to the query.
     *
     * @param query the query of the questionhistory search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/questionhistories")
    @Timed
    public ResponseEntity<List<QuestionHistory>> searchQuestionhistories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Questionhistories for query {}", query);
        Page<QuestionHistory> page = questionhistoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/questionhistories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
