package io.github.softech.dev.sgill.repository;

import io.github.softech.dev.sgill.domain.Course;
import io.github.softech.dev.sgill.domain.CourseHistory;
import io.github.softech.dev.sgill.domain.Customer;
import io.github.softech.dev.sgill.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the CourseHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseHistoryRepository extends JpaRepository<CourseHistory, Long>, JpaSpecificationExecutor<CourseHistory> {
    CourseHistory findCourseHistoryByCourseAndCustomer(Course course, Customer customer);
    Optional<CourseHistory> findCourseHistoryByCourseIdAndCustomer_Id(Long cid, Long custId);
}
