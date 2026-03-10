package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentTest {
    private List<Product> createProducts() {
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Test Product");
        product.setProductQuantity(1);

        List<Product> products = new ArrayList<>();
        products.add(product);
        return products;
    }

    @Test
    void testCreatePayment() {

        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP1234ABC5678");

        Order order = new Order(
                "order-1",
            createProducts(),
                System.currentTimeMillis(),
                "Tester"
        );

        Payment payment = new Payment("1", "VOUCHER", order, data);

        assertEquals("1", payment.getId());
        assertEquals("VOUCHER", payment.getMethod());
        assertEquals(data, payment.getPaymentData());
        assertEquals(order, payment.getOrder());
        assertEquals("PENDING", payment.getStatus());
    }
}
