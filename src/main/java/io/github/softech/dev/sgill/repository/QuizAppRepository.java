package io.github.softech.dev.sgill.repository;

import io.github.softech.dev.sgill.domain.QuizApp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the QuizApp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuizAppRepository extends JpaRepository<QuizApp, Long>, JpaSpecificationExecutor<QuizApp> {

}
