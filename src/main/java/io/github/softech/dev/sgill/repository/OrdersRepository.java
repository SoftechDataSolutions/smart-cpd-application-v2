package io.github.softech.dev.sgill.repository;

import com.stripe.model.Order;
import io.github.softech.dev.sgill.domain.Cart;
import io.github.softech.dev.sgill.domain.Orders;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;


/**
 * Spring Data  repository for the Orders entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long>, JpaSpecificationExecutor<Orders> {
    List<Orders> getOrdersByCart(Cart cart);
    Orders getOrdersByCartId(Long id);
}
