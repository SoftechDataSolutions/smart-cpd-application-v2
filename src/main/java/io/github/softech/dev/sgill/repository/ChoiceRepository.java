package io.github.softech.dev.sgill.repository;

import io.github.softech.dev.sgill.domain.Choice;
import io.github.softech.dev.sgill.domain.Course;
import io.github.softech.dev.sgill.domain.Question;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


/**
 * Spring Data  repository for the Choice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChoiceRepository extends JpaRepository<Choice, Long>, JpaSpecificationExecutor<Choice> {
    List<Choice> findChoicesByQuestionId(Long id);

    @Query(value = "SELECT TOP 1 * FROM CourseHistory course_history WHERE course_history.customer.id = :customer_id ORDER BY course_history.id DESC", nativeQuery = true)
    Course getRecentCourse(@Param("customer_id") Long id);
}
