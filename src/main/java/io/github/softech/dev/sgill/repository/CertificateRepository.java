package io.github.softech.dev.sgill.repository;

import com.hazelcast.core.LifecycleEvent;
import io.github.softech.dev.sgill.domain.Certificate;
import io.github.softech.dev.sgill.domain.Course;
import io.github.softech.dev.sgill.domain.Customer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Certificate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long>, JpaSpecificationExecutor<Certificate> {
    List<Certificate> getCertificatesByCustomer(Customer customer);
    Certificate getCertificateByCoursesAndCustomer(Course course, Customer customer);
}
