package io.github.softech.dev.sgill.repository;

import io.github.softech.dev.sgill.domain.CourseHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CourseHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseHistoryRepository extends JpaRepository<CourseHistory, Long>, JpaSpecificationExecutor<CourseHistory> {

}
