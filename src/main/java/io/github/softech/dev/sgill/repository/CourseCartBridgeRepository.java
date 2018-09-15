package io.github.softech.dev.sgill.repository;

import io.github.softech.dev.sgill.domain.CourseCartBridge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.LongFunction;


/**
 * Spring Data  repository for the CourseCartBridge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseCartBridgeRepository extends JpaRepository<CourseCartBridge, Long>, JpaSpecificationExecutor<CourseCartBridge> {
    List<CourseCartBridge> findCourseCartBridgesByCartId(Long id);
}
