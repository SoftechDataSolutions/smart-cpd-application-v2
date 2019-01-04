package io.github.softech.dev.sgill.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.softech.dev.sgill.security.AuthoritiesConstants;
import io.github.softech.dev.sgill.security.SecurityUtils;
import io.github.softech.dev.sgill.service.ElasticsearchIndexService;
import io.github.softech.dev.sgill.web.rest.util.HeaderUtil;
import org.elasticsearch.common.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

/**
 * REST controller for managing Elasticsearch index.
 */
@RestController
@RequestMapping("/api")
public class ElasticsearchIndexResource {

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexResource.class);

    @Inject
    private ElasticsearchIndexService elasticsearchIndexService;

    /**
     * POST  /elasticsearch/index -> Reindex all Elasticsearch documents
     */
    @RequestMapping(value = "/elasticsearch/index",
        method = RequestMethod.POST,
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> reindexAll() throws URISyntaxException {
        log.info("REST request to reindex Elasticsearch by user : {}", SecurityUtils.getCurrentUserLogin());
        elasticsearchIndexService.reindexAll();
        return ResponseEntity.accepted()
            .headers(HeaderUtil.createAlert("elasticsearch.reindex.accepted", null))
            .build();
    }
}
