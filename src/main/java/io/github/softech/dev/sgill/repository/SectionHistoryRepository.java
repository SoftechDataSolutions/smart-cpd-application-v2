package io.github.softech.dev.sgill.repository;

import io.github.softech.dev.sgill.domain.SectionHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SectionHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SectionHistoryRepository extends JpaRepository<SectionHistory, Long>, JpaSpecificationExecutor<SectionHistory> {

}
