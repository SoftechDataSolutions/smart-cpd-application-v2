package io.github.softech.dev.sgill.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of BookmarksSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class BookmarksSearchRepositoryMockConfiguration {

    @MockBean
    private BookmarksSearchRepository mockBookmarksSearchRepository;

}
