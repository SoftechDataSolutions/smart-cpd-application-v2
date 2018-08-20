package io.github.softech.dev.sgill.repository;

import io.github.softech.dev.sgill.domain.CourseCartBridge;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CourseCartBridge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseCartBridgeRepository extends JpaRepository<CourseCartBridge, Long>, JpaSpecificationExecutor<CourseCartBridge> {

}
