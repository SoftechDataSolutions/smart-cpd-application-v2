package io.github.softech.dev.sgill.repository;

import io.github.softech.dev.sgill.domain.QuizHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the QuizHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Long>, JpaSpecificationExecutor<QuizHistory> {

}
