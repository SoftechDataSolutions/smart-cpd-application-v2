package io.github.softech.dev.sgill.repository;

import io.github.softech.dev.sgill.domain.Bookmarks;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Bookmarks entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookmarksRepository extends JpaRepository<Bookmarks, Long> {

}
