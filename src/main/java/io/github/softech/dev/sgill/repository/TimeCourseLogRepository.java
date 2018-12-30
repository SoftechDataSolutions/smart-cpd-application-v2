package io.github.softech.dev.sgill.repository;

import io.github.softech.dev.sgill.domain.Choice;
import io.github.softech.dev.sgill.domain.TimeCourseLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the TimeCourseLog entity.
 */

@Repository
public interface TimeCourseLogRepository extends JpaRepository<TimeCourseLog, Long>, JpaSpecificationExecutor<TimeCourseLog> {
    @Query(value = "SELECT SUM(timespent) FROM TimeCourseLog time_course_log WHERE time_course_log.customer_id = :customer_id AND time_course_log.course_id = :course_id", nativeQuery = true)
    Long findTimeSpentbyCustomerAndCourse(@Param("customer_id") Long cust_id, @Param("course_id") Long course_id);

    TimeCourseLog findTimeCourseLogByCustomerIdAndCourseId(Long custid, Long courseid);
}
