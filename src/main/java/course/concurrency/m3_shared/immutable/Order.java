package course.concurrency.m3_shared.immutable;

import java.util.Collections;
import java.util.List;

import static course.concurrency.m3_shared.immutable.Order.Status.NEW;

public class Order {

    public enum Status {NEW, DELIVERED}

    private final Long id;

    private final List<Item> items;

    private final PaymentInfo paymentInfo;

    private final boolean isPacked;

    private final Status status;

    public Order(Long id, List<Item> items, PaymentInfo paymentInfo, boolean isPacked, Status status) {
        this.id = id;
        this.items = Collections.unmodifiableList(items);
        this.paymentInfo = paymentInfo;
        this.isPacked = isPacked;
        this.status = status;
    }

    public Order(Long id, List<Item> items) {
        this(id, items, null, false, NEW);
    }

    public boolean checkStatus() {
        return items != null && !items.isEmpty() && paymentInfo != null && isPacked;
    }

    public Long getId() {
        return id;
    }

    public List<Item> getItems() {
        return items;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public boolean isPacked() {
        return isPacked;
    }

    public Status getStatus() {
        return status;
    }

    public Order updatedPaymentInfo(PaymentInfo paymentInfo) {
        return new Order(this.getId(), this.getItems(), paymentInfo, this.isPacked(), this.getStatus());
    }

    public Order updatedPacked(boolean isPacked) {
        return new Order(this.getId(), this.getItems(), this.getPaymentInfo(), isPacked, this.getStatus());
    }

    public Order updatedStatus(Status status) {
        return new Order(this.getId(), this.getItems(), this.getPaymentInfo(), this.isPacked(), status);
    }
}