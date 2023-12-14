package mp.sitili.modules.payment_order.entities;

import mp.sitili.modules.order.entities.Order;
import mp.sitili.modules.payment_cc.entities.PaymentCC;

import javax.persistence.*;

@Entity
@Table(name = "payment_order")
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private PaymentCC paymentCC;

    public PaymentOrder(Long id, Order order, PaymentCC paymentCC) {
        this.id = id;
        this.order = order;
        this.paymentCC = paymentCC;
    }

    public PaymentOrder() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public PaymentCC getPaymentCC() {
        return paymentCC;
    }

    public void setPaymentCC(PaymentCC paymentCC) {
        this.paymentCC = paymentCC;
    }
}
