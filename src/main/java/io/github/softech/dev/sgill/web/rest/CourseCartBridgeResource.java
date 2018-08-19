package io.github.softech.dev.sgill.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.softech.dev.sgill.domain.CourseCartBridge;
import io.github.softech.dev.sgill.service.CourseCartBridgeService;
import io.github.softech.dev.sgill.web.rest.errors.BadRequestAlertException;
import io.github.softech.dev.sgill.web.rest.util.HeaderUtil;
import io.github.softech.dev.sgill.web.rest.util.PaginationUtil;
import io.github.softech.dev.sgill.service.dto.CourseCartBridgeCriteria;
import io.github.softech.dev.sgill.service.CourseCartBridgeQueryService;
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
 * REST controller for managing CourseCartBridge.
 */
@RestController
@RequestMapping("/api")
public class CourseCartBridgeResource {

    private final Logger log = LoggerFactory.getLogger(CourseCartBridgeResource.class);

    private static final String ENTITY_NAME = "courseCartBridge";

    private final CourseCartBridgeService courseCartBridgeService;

    private final CourseCartBridgeQueryService courseCartBridgeQueryService;

    public CourseCartBridgeResource(CourseCartBridgeService courseCartBridgeService, CourseCartBridgeQueryService courseCartBridgeQueryService) {
        this.courseCartBridgeService = courseCartBridgeService;
        this.courseCartBridgeQueryService = courseCartBridgeQueryService;
    }

    /**
     * POST  /course-cart-bridges : Create a new courseCartBridge.
     *
     * @param courseCartBridge the courseCartBridge to create
     * @return the ResponseEntity with status 201 (Created) and with body the new courseCartBridge, or with status 400 (Bad Request) if the courseCartBridge has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/course-cart-bridges")
    @Timed
    public ResponseEntity<CourseCartBridge> createCourseCartBridge(@RequestBody CourseCartBridge courseCartBridge) throws URISyntaxException {
        log.debug("REST request to save CourseCartBridge : {}", courseCartBridge);
        if (courseCartBridge.getId() != null) {
            throw new BadRequestAlertException("A new courseCartBridge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseCartBridge result = courseCartBridgeService.save(courseCartBridge);
        return ResponseEntity.created(new URI("/api/course-cart-bridges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /course-cart-bridges : Updates an existing courseCartBridge.
     *
     * @param courseCartBridge the courseCartBridge to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated courseCartBridge,
     * or with status 400 (Bad Request) if the courseCartBridge is not valid,
     * or with status 500 (Internal Server Error) if the courseCartBridge couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/course-cart-bridges")
    @Timed
    public ResponseEntity<CourseCartBridge> updateCourseCartBridge(@RequestBody CourseCartBridge courseCartBridge) throws URISyntaxException {
        log.debug("REST request to update CourseCartBridge : {}", courseCartBridge);
        if (courseCartBridge.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CourseCartBridge result = courseCartBridgeService.save(courseCartBridge);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, courseCartBridge.getId().toString()))
            .body(result);
    }

    /**
     * GET  /course-cart-bridges : get all the courseCartBridges.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of courseCartBridges in body
     */
    @GetMapping("/course-cart-bridges")
    @Timed
    public ResponseEntity<List<CourseCartBridge>> getAllCourseCartBridges(CourseCartBridgeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CourseCartBridges by criteria: {}", criteria);
        Page<CourseCartBridge> page = courseCartBridgeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/course-cart-bridges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /course-cart-bridges/:id : get the "id" courseCartBridge.
     *
     * @param id the id of the courseCartBridge to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the courseCartBridge, or with status 404 (Not Found)
     */
    @GetMapping("/course-cart-bridges/{id}")
    @Timed
    public ResponseEntity<CourseCartBridge> getCourseCartBridge(@PathVariable Long id) {
        log.debug("REST request to get CourseCartBridge : {}", id);
        Optional<CourseCartBridge> courseCartBridge = courseCartBridgeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseCartBridge);
    }

    /**
     * DELETE  /course-cart-bridges/:id : delete the "id" courseCartBridge.
     *
     * @param id the id of the courseCartBridge to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/course-cart-bridges/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourseCartBridge(@PathVariable Long id) {
        log.debug("REST request to delete CourseCartBridge : {}", id);
        courseCartBridgeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/course-cart-bridges?query=:query : search for the courseCartBridge corresponding
     * to the query.
     *
     * @param query the query of the courseCartBridge search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/course-cart-bridges")
    @Timed
    public ResponseEntity<List<CourseCartBridge>> searchCourseCartBridges(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CourseCartBridges for query {}", query);
        Page<CourseCartBridge> page = courseCartBridgeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/course-cart-bridges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
