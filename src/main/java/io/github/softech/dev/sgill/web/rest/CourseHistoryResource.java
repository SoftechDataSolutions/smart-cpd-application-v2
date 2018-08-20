package io.github.softech.dev.sgill.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.softech.dev.sgill.domain.CourseHistory;
import io.github.softech.dev.sgill.service.CourseHistoryService;
import io.github.softech.dev.sgill.web.rest.errors.BadRequestAlertException;
import io.github.softech.dev.sgill.web.rest.util.HeaderUtil;
import io.github.softech.dev.sgill.web.rest.util.PaginationUtil;
import io.github.softech.dev.sgill.service.dto.CourseHistoryCriteria;
import io.github.softech.dev.sgill.service.CourseHistoryQueryService;
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
 * REST controller for managing CourseHistory.
 */
@RestController
@RequestMapping("/api")
public class CourseHistoryResource {

    private final Logger log = LoggerFactory.getLogger(CourseHistoryResource.class);

    private static final String ENTITY_NAME = "courseHistory";

    private final CourseHistoryService courseHistoryService;

    private final CourseHistoryQueryService courseHistoryQueryService;

    public CourseHistoryResource(CourseHistoryService courseHistoryService, CourseHistoryQueryService courseHistoryQueryService) {
        this.courseHistoryService = courseHistoryService;
        this.courseHistoryQueryService = courseHistoryQueryService;
    }

    /**
     * POST  /course-histories : Create a new courseHistory.
     *
     * @param courseHistory the courseHistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new courseHistory, or with status 400 (Bad Request) if the courseHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/course-histories")
    @Timed
    public ResponseEntity<CourseHistory> createCourseHistory(@RequestBody CourseHistory courseHistory) throws URISyntaxException {
        log.debug("REST request to save CourseHistory : {}", courseHistory);
        if (courseHistory.getId() != null) {
            throw new BadRequestAlertException("A new courseHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseHistory result = courseHistoryService.save(courseHistory);
        return ResponseEntity.created(new URI("/api/course-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /course-histories : Updates an existing courseHistory.
     *
     * @param courseHistory the courseHistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated courseHistory,
     * or with status 400 (Bad Request) if the courseHistory is not valid,
     * or with status 500 (Internal Server Error) if the courseHistory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/course-histories")
    @Timed
    public ResponseEntity<CourseHistory> updateCourseHistory(@RequestBody CourseHistory courseHistory) throws URISyntaxException {
        log.debug("REST request to update CourseHistory : {}", courseHistory);
        if (courseHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CourseHistory result = courseHistoryService.save(courseHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, courseHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /course-histories : get all the courseHistories.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of courseHistories in body
     */
    @GetMapping("/course-histories")
    @Timed
    public ResponseEntity<List<CourseHistory>> getAllCourseHistories(CourseHistoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CourseHistories by criteria: {}", criteria);
        Page<CourseHistory> page = courseHistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/course-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /course-histories/:id : get the "id" courseHistory.
     *
     * @param id the id of the courseHistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the courseHistory, or with status 404 (Not Found)
     */
    @GetMapping("/course-histories/{id}")
    @Timed
    public ResponseEntity<CourseHistory> getCourseHistory(@PathVariable Long id) {
        log.debug("REST request to get CourseHistory : {}", id);
        Optional<CourseHistory> courseHistory = courseHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseHistory);
    }

    /**
     * DELETE  /course-histories/:id : delete the "id" courseHistory.
     *
     * @param id the id of the courseHistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/course-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourseHistory(@PathVariable Long id) {
        log.debug("REST request to delete CourseHistory : {}", id);
        courseHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/course-histories?query=:query : search for the courseHistory corresponding
     * to the query.
     *
     * @param query the query of the courseHistory search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/course-histories")
    @Timed
    public ResponseEntity<List<CourseHistory>> searchCourseHistories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CourseHistories for query {}", query);
        Page<CourseHistory> page = courseHistoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/course-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
