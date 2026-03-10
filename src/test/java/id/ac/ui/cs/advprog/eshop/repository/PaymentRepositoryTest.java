package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentRepositoryTest {

    PaymentRepository repository;

    @BeforeEach
    void setUp() {
        repository = new PaymentRepository();
    }

    @Test
    void testSavePayment() {
        Payment payment = new Payment("1", "VOUCHER", new HashMap<>());

        repository.save(payment);

        Payment result = repository.findById("1");

        assertEquals("1", result.getId());
    }

    @Test
    void testFindAll() {
        repository.save(new Payment("1", "VOUCHER", new HashMap<>()));
        repository.save(new Payment("2", "COD", new HashMap<>()));

        List<Payment> payments = repository.findAll();

        assertEquals(2, payments.size());
    }
}
