package io.github.softech.dev.sgill.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.softech.dev.sgill.domain.Bookmarks;
import io.github.softech.dev.sgill.service.BookmarksService;
import io.github.softech.dev.sgill.web.rest.errors.BadRequestAlertException;
import io.github.softech.dev.sgill.web.rest.util.HeaderUtil;
import io.github.softech.dev.sgill.web.rest.util.PaginationUtil;
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
 * REST controller for managing Bookmarks.
 */
@RestController
@RequestMapping("/api")
public class BookmarksResource {

    private final Logger log = LoggerFactory.getLogger(BookmarksResource.class);

    private static final String ENTITY_NAME = "bookmarks";

    private final BookmarksService bookmarksService;

    public BookmarksResource(BookmarksService bookmarksService) {
        this.bookmarksService = bookmarksService;
    }

    /**
     * POST  /bookmarks : Create a new bookmarks.
     *
     * @param bookmarks the bookmarks to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bookmarks, or with status 400 (Bad Request) if the bookmarks has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bookmarks")
    @Timed
    public ResponseEntity<Bookmarks> createBookmarks(@Valid @RequestBody Bookmarks bookmarks) throws URISyntaxException {
        log.debug("REST request to save Bookmarks : {}", bookmarks);
        if (bookmarks.getId() != null) {
            throw new BadRequestAlertException("A new bookmarks cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bookmarks result = bookmarksService.save(bookmarks);
        return ResponseEntity.created(new URI("/api/bookmarks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bookmarks : Updates an existing bookmarks.
     *
     * @param bookmarks the bookmarks to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bookmarks,
     * or with status 400 (Bad Request) if the bookmarks is not valid,
     * or with status 500 (Internal Server Error) if the bookmarks couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bookmarks")
    @Timed
    public ResponseEntity<Bookmarks> updateBookmarks(@Valid @RequestBody Bookmarks bookmarks) throws URISyntaxException {
        log.debug("REST request to update Bookmarks : {}", bookmarks);
        if (bookmarks.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Bookmarks result = bookmarksService.save(bookmarks);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bookmarks.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bookmarks : get all the bookmarks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bookmarks in body
     */
    @GetMapping("/bookmarks")
    @Timed
    public ResponseEntity<List<Bookmarks>> getAllBookmarks(Pageable pageable) {
        log.debug("REST request to get a page of Bookmarks");
        Page<Bookmarks> page = bookmarksService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bookmarks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bookmarks/:id : get the "id" bookmarks.
     *
     * @param id the id of the bookmarks to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bookmarks, or with status 404 (Not Found)
     */
    @GetMapping("/bookmarks/{id}")
    @Timed
    public ResponseEntity<Bookmarks> getBookmarks(@PathVariable Long id) {
        log.debug("REST request to get Bookmarks : {}", id);
        Optional<Bookmarks> bookmarks = bookmarksService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookmarks);
    }

    /**
     * DELETE  /bookmarks/:id : delete the "id" bookmarks.
     *
     * @param id the id of the bookmarks to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bookmarks/{id}")
    @Timed
    public ResponseEntity<Void> deleteBookmarks(@PathVariable Long id) {
        log.debug("REST request to delete Bookmarks : {}", id);
        bookmarksService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/bookmarks?query=:query : search for the bookmarks corresponding
     * to the query.
     *
     * @param query the query of the bookmarks search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/bookmarks")
    @Timed
    public ResponseEntity<List<Bookmarks>> searchBookmarks(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Bookmarks for query {}", query);
        Page<Bookmarks> page = bookmarksService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bookmarks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
