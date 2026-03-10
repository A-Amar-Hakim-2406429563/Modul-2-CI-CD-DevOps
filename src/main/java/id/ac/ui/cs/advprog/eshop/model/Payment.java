package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Payment {

    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;
    private Order order;

    public Payment(String id, String method, Order order, Map<String, String> paymentData) {
        this(id, method, order, "PENDING", paymentData);
    }

    public Payment(String id, String method, Order order, String status, Map<String, String> paymentData) {
        this.id = id;
        this.method = method;
        this.order = order;
        this.status = status;
        this.paymentData = paymentData;
    }
}