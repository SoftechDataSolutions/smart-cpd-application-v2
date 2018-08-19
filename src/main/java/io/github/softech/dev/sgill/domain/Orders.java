package io.github.softech.dev.sgill.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import io.github.softech.dev.sgill.domain.enumeration.NOTIFICATIONS;

import io.github.softech.dev.sgill.domain.enumeration.PAYMENT;

/**
 * A Orders.
 */
@Entity
@Table(name = "orders")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "createddate")
    private Instant createddate;

    @Column(name = "amount")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private NOTIFICATIONS status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment")
    private PAYMENT payment;

    @OneToOne
    @JoinColumn(unique = true)
    private Cart cart;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreateddate() {
        return createddate;
    }

    public Orders createddate(Instant createddate) {
        this.createddate = createddate;
        return this;
    }

    public void setCreateddate(Instant createddate) {
        this.createddate = createddate;
    }

    public Double getAmount() {
        return amount;
    }

    public Orders amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public NOTIFICATIONS getStatus() {
        return status;
    }

    public Orders status(NOTIFICATIONS status) {
        this.status = status;
        return this;
    }

    public void setStatus(NOTIFICATIONS status) {
        this.status = status;
    }

    public PAYMENT getPayment() {
        return payment;
    }

    public Orders payment(PAYMENT payment) {
        this.payment = payment;
        return this;
    }

    public void setPayment(PAYMENT payment) {
        this.payment = payment;
    }

    public Cart getCart() {
        return cart;
    }

    public Orders cart(Cart cart) {
        this.cart = cart;
        return this;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Orders orders = (Orders) o;
        if (orders.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orders.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Orders{" +
            "id=" + getId() +
            ", createddate='" + getCreateddate() + "'" +
            ", amount=" + getAmount() +
            ", status='" + getStatus() + "'" +
            ", payment='" + getPayment() + "'" +
            "}";
    }
}
