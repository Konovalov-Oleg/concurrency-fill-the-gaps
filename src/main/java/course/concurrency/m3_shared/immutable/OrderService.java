package course.concurrency.m3_shared.immutable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class OrderService {

    private final Map<Long, Order> currentOrders = new ConcurrentHashMap<>();

    private final AtomicLong nextId = new AtomicLong();

    public long createOrder(List<Item> items) {
        long id = nextId.incrementAndGet();
        currentOrders.put(id, new Order(id, items));
        return id;
    }

    public void updatePaymentInfo(long orderId, PaymentInfo paymentInfo) {
        Order order = currentOrders.computeIfPresent(orderId, (k, v) -> v.updatedPaymentInfo(paymentInfo));
        if (order != null && order.checkStatus()) {
            deliver(order);
        }
    }

    public void setPacked(long orderId) {
        Order order = currentOrders.computeIfPresent(orderId, (k, v) -> v.updatedPacked(true));
        if (order != null && order.checkStatus()) {
            deliver(order);
        }
    }

    private void deliver(Order order) {
        currentOrders.computeIfPresent(order.getId(), (k, v) -> v.updatedStatus(Order.Status.DELIVERED));
    }

    public boolean isDelivered(long orderId) {
        return currentOrders.get(orderId).getStatus().equals(Order.Status.DELIVERED);
    }
}