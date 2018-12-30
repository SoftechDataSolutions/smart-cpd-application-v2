package io.github.softech.dev.sgill.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.softech.dev.sgill.domain.TimeCourseLog;
import io.github.softech.dev.sgill.repository.TimeCourseLogRepository;
import io.github.softech.dev.sgill.service.TimeCourseLogService;
import io.github.softech.dev.sgill.web.rest.errors.BadRequestAlertException;
import io.github.softech.dev.sgill.web.rest.util.HeaderUtil;
import io.github.softech.dev.sgill.web.rest.util.PaginationUtil;
import io.github.softech.dev.sgill.service.dto.TimeCourseLogCriteria;
import io.github.softech.dev.sgill.service.TimeCourseLogQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TimeCourseLog.
 */
@RestController
@RequestMapping("/api")
public class TimeCourseLogResource {

    private final Logger log = LoggerFactory.getLogger(TimeCourseLogResource.class);

    private static final String ENTITY_NAME = "timeCourseLog";

    private final TimeCourseLogService timeCourseLogService;

    private final TimeCourseLogQueryService timeCourseLogQueryService;

    private final TimeCourseLogRepository timeCourseLogRepository;

    public TimeCourseLogResource(TimeCourseLogService timeCourseLogService, TimeCourseLogQueryService timeCourseLogQueryService, TimeCourseLogRepository timeCourseLogRepository) {
        this.timeCourseLogService = timeCourseLogService;
        this.timeCourseLogQueryService = timeCourseLogQueryService;
        this.timeCourseLogRepository = timeCourseLogRepository;
    }

    /**
     * POST  /time-course-logs : Create a new timeCourseLog.
     *
     * @param timeCourseLog the timeCourseLog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new timeCourseLog, or with status 400 (Bad Request) if the timeCourseLog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/time-course-logs")
    @Timed
    public ResponseEntity<TimeCourseLog> createTimeCourseLog(@RequestBody TimeCourseLog timeCourseLog) throws URISyntaxException {
        log.debug("REST request to save TimeCourseLog : {}", timeCourseLog);
        if (timeCourseLog.getId() != null) {
            throw new BadRequestAlertException("A new timeCourseLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TimeCourseLog result = timeCourseLogService.save(timeCourseLog);
        return ResponseEntity.created(new URI("/api/time-course-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /time-course-logs : Updates an existing timeCourseLog.
     *
     * @param timeCourseLog the timeCourseLog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated timeCourseLog,
     * or with status 400 (Bad Request) if the timeCourseLog is not valid,
     * or with status 500 (Internal Server Error) if the timeCourseLog couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/time-course-logs")
    @Timed
    public ResponseEntity<TimeCourseLog> updateTimeCourseLog(@RequestBody TimeCourseLog timeCourseLog) throws URISyntaxException {
        log.debug("REST request to update TimeCourseLog : {}", timeCourseLog);
        if (timeCourseLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TimeCourseLog result = timeCourseLogService.save(timeCourseLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, timeCourseLog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /time-course-logs : get all the timeCourseLogs.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of timeCourseLogs in body
     */
    @GetMapping("/time-course-logs")
    @Timed
    public ResponseEntity<List<TimeCourseLog>> getAllTimeCourseLogs(TimeCourseLogCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TimeCourseLogs by criteria: {}", criteria);
        Page<TimeCourseLog> page = timeCourseLogQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/time-course-logs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /time-course-logs/:id : get the "id" timeCourseLog.
     *
     * @param id the id of the timeCourseLog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the timeCourseLog, or with status 404 (Not Found)
     */
    @GetMapping("/time-course-logs/{id}")
    @Timed
    public ResponseEntity<TimeCourseLog> getTimeCourseLog(@PathVariable Long id) {
        log.debug("REST request to get TimeCourseLog : {}", id);
        Optional<TimeCourseLog> timeCourseLog = timeCourseLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timeCourseLog);
    }

    @GetMapping("{courseid}/time-course-logs/{customerid}")
    @Timed
    public Long getSpentTimeCourseLogs(@PathVariable Long courseid, @PathVariable Long customerid) {
        log.debug("REST request to get Time Spent by Course ID : {}", courseid);
        log.debug("REST request to get Time Spent by Customer ID : {}", customerid);
        return timeCourseLogRepository.findTimeSpentbyCustomerAndCourse(customerid, courseid);
    }

    /**
     * DELETE  /time-course-logs/:id : delete the "id" timeCourseLog.
     *
     * @param id the id of the timeCourseLog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/time-course-logs/{id}")
    @Timed
    public ResponseEntity<Void> deleteTimeCourseLog(@PathVariable Long id) {
        log.debug("REST request to delete TimeCourseLog : {}", id);
        timeCourseLogService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/time-course-logs?query=:query : search for the timeCourseLog corresponding
     * to the query.
     *
     * @param query the query of the timeCourseLog search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/time-course-logs")
    @Timed
    public ResponseEntity<List<TimeCourseLog>> searchTimeCourseLogs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TimeCourseLogs for query {}", query);
        Page<TimeCourseLog> page = timeCourseLogService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/time-course-logs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
