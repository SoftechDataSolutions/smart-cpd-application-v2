package io.github.softech.dev.sgill.repository;

import io.github.softech.dev.sgill.domain.Cart;
import io.github.softech.dev.sgill.domain.Customer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Max;
import java.util.List;


/**
 * Spring Data  repository for the Cart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartRepository extends JpaRepository<Cart, Long>, JpaSpecificationExecutor<Cart> {
    List<Cart> findCartsByCustomerIdAndCheckout(Long id, Boolean flag);
    List<Cart> getCartsByCustomer(Customer customer);
}
