package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
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
        data.put("voucherCode", "ESHOP1234ABC5678"); // Ini adalah voucher yang valid

        Order order = new Order(
                "order-1",
                createProducts(),
                System.currentTimeMillis(),
                "Tester"
        );

        // REFACTOR: Menggunakan Enum PaymentMethod
        Payment payment = new Payment("1", PaymentMethod.VOUCHER.getValue(), order, data);

        assertEquals("1", payment.getId());
        assertEquals(PaymentMethod.VOUCHER.getValue(), payment.getMethod());
        assertEquals(data, payment.getPaymentData());
        assertEquals(order, payment.getOrder());

        // REFACTOR: Karena voucher valid, Payment yang sudah pintar akan langsung mengesetnya jadi SUCCESS
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }
}