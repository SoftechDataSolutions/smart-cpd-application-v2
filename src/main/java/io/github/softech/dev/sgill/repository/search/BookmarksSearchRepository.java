package io.github.softech.dev.sgill.repository.search;

import io.github.softech.dev.sgill.domain.Bookmarks;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Bookmarks entity.
 */
public interface BookmarksSearchRepository extends ElasticsearchRepository<Bookmarks, Long> {
}
