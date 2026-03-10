package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentRepositoryTest {

    PaymentRepository repository;
    Order order;

    private List<Product> createProducts() {
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Test Product");
        product.setProductQuantity(1);

        List<Product> products = new ArrayList<>();
        products.add(product);
        return products;
    }

    @BeforeEach
    void setUp() {
        repository = new PaymentRepository();
        order = new Order("order-1", createProducts(), 100L, "Tester");
    }

    @Test
    void testSavePayment() {
        // REFACTOR: Menggunakan Enum PaymentMethod.VOUCHER
        Payment payment = new Payment("1", PaymentMethod.VOUCHER.getValue(), order, new HashMap<>());

        repository.save(payment);

        Payment result = repository.findById("1");

        assertEquals("1", result.getId());
    }

    @Test
    void testFindAll() {
        // REFACTOR: Menggunakan Enum untuk VOUCHER dan COD
        repository.save(new Payment("1", PaymentMethod.VOUCHER.getValue(), order, new HashMap<>()));
        repository.save(new Payment("2", PaymentMethod.COD.getValue(), order, new HashMap<>()));

        List<Payment> payments = repository.findAll();

        assertEquals(2, payments.size());
    }
}