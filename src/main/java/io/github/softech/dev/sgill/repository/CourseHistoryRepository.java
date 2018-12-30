package io.github.softech.dev.sgill.repository;

import io.github.softech.dev.sgill.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Null;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the CourseHistory entity.
 */
@Repository
public interface CourseHistoryRepository extends JpaRepository<CourseHistory, Long>, JpaSpecificationExecutor<CourseHistory> {
    CourseHistory findCourseHistoryByCourseAndCustomer(Course course, Customer customer);
    Optional<CourseHistory> findCourseHistoryByCourseIdAndCustomer_Id(Long cid, Long custId);
    List<CourseHistory> getCourseHistoriesByCustomer(Customer customer);
    @Query(value = "SELECT TOP 1  * FROM CourseHistory course_history WHERE course_history.customer.id = :customer_id ORDER BY course_history.id DESC", nativeQuery = true)
    CourseHistory findRecentCourseHistory(@Param("customer_id") Long id);
}
