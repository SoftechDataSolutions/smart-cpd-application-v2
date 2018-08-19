package io.github.softech.dev.sgill.service.dto;

import java.io.Serializable;
import io.github.softech.dev.sgill.domain.enumeration.NOTIFICATIONS;
import io.github.softech.dev.sgill.domain.enumeration.PAYMENT;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the Orders entity. This class is used in OrdersResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /orders?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrdersCriteria implements Serializable {
    /**
     * Class for filtering NOTIFICATIONS
     */
    public static class NOTIFICATIONSFilter extends Filter<NOTIFICATIONS> {
    }

    /**
     * Class for filtering PAYMENT
     */
    public static class PAYMENTFilter extends Filter<PAYMENT> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private InstantFilter createddate;

    private DoubleFilter amount;

    private NOTIFICATIONSFilter status;

    private PAYMENTFilter payment;

    private LongFilter cartId;

    public OrdersCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getCreateddate() {
        return createddate;
    }

    public void setCreateddate(InstantFilter createddate) {
        this.createddate = createddate;
    }

    public DoubleFilter getAmount() {
        return amount;
    }

    public void setAmount(DoubleFilter amount) {
        this.amount = amount;
    }

    public NOTIFICATIONSFilter getStatus() {
        return status;
    }

    public void setStatus(NOTIFICATIONSFilter status) {
        this.status = status;
    }

    public PAYMENTFilter getPayment() {
        return payment;
    }

    public void setPayment(PAYMENTFilter payment) {
        this.payment = payment;
    }

    public LongFilter getCartId() {
        return cartId;
    }

    public void setCartId(LongFilter cartId) {
        this.cartId = cartId;
    }

    @Override
    public String toString() {
        return "OrdersCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (createddate != null ? "createddate=" + createddate + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (payment != null ? "payment=" + payment + ", " : "") +
                (cartId != null ? "cartId=" + cartId + ", " : "") +
            "}";
    }

}
