package io.github.softech.dev.sgill.repository;

import io.github.softech.dev.sgill.domain.TimeCourseLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TimeCourseLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeCourseLogRepository extends JpaRepository<TimeCourseLog, Long>, JpaSpecificationExecutor<TimeCourseLog> {

}
