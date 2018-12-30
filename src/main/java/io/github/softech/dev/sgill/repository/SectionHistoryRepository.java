package io.github.softech.dev.sgill.repository;

import io.github.softech.dev.sgill.domain.Choice;
import io.github.softech.dev.sgill.domain.Customer;
import io.github.softech.dev.sgill.domain.Section;
import io.github.softech.dev.sgill.domain.SectionHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the SectionHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SectionHistoryRepository extends JpaRepository<SectionHistory, Long>, JpaSpecificationExecutor<SectionHistory> {
    List<SectionHistory> getSectionHistoriesByCustomer(Customer customer);

    @Query(value = "SELECT TOP 1 * FROM SectionHistory section_history WHERE section_history.customer.id = :customer_id ORDER BY section_history.id DESC", nativeQuery = true)
    SectionHistory getRecentSectionHistory(@Param("customer_id") Long id);

    @Query(value = "SELECT TOP 1 * FROM SectionHistory section_history WHERE section_history.customer.id = :customer_id AND section_history.course.id = :course_id ORDER BY section_history.timestamp DESC", nativeQuery = true)
    SectionHistory getPersistanceSectionHistory(@Param("customer_id") Long id, @Param("course_id") Long courseId);

    SectionHistory getSectionHistoryByCustomerIdAndSectionId(Long customerId, Long sectionId);

    @Query(value = "SELECT TOP 1 * FROM SectionHistory record WHERE record.customer.id = :customer_id AND record.section.id IN (SELECT id from SECTION section WHERE section.course.id = :course_id) ORDER BY id DESC", nativeQuery = true)
    SectionHistory getSectionByCourse(@Param("customer_id") Long customer_id, @Param("course_id") Long course_id);
}
