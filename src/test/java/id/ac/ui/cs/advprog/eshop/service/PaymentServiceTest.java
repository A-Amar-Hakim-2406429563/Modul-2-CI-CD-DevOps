package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    // REFACTOR: Magic Strings dihapus dan digantikan sepenuhnya oleh Enum

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    Order order;

    private ArrayList<Product> createProducts() {
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Test Product");
        product.setProductQuantity(1);

        ArrayList<Product> products = new ArrayList<>();
        products.add(product);
        return products;
    }

    private void mockSaveReturnsPayment() {
        doAnswer(invocation -> invocation.getArgument(0)).when(paymentRepository).save(any(Payment.class));
    }

    @BeforeEach
    void setUp() {
        order = new Order("1", createProducts(), 100L, "User");
    }

    @Test
    void testAddPayment() {
        mockSaveReturnsPayment();

        Map<String, String> data = new HashMap<>();
        data.put("address", "Jl. Margonda Raya No. 1");
        data.put("deliveryFee", "15000");

        Payment result = paymentService.addPayment(order, PaymentMethod.COD.getValue(), data);

        assertNotNull(result.getId());
        assertEquals(PaymentMethod.COD.getValue(), result.getMethod());
        assertEquals(order, result.getOrder());
        assertEquals(PaymentStatus.PENDING.getValue(), result.getStatus());
    }

    @Test
    void testAddPaymentWithValidVoucherSetsSuccessStatus() {
        mockSaveReturnsPayment();

        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP1234ABC5678");

        Payment result = paymentService.addPayment(order, PaymentMethod.VOUCHER.getValue(), data);

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
    }

    @Test
    void testAddPaymentWithInvalidVoucherSetsRejectedStatus() {
        mockSaveReturnsPayment();

        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "INVALID");

        Payment result = paymentService.addPayment(order, PaymentMethod.VOUCHER.getValue(), data);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testAddPaymentWithValidCodKeepsPendingStatus() {
        mockSaveReturnsPayment();

        Map<String, String> data = new HashMap<>();
        data.put("address", "Jl. Margonda Raya No. 1");
        data.put("deliveryFee", "15000");

        Payment result = paymentService.addPayment(order, PaymentMethod.COD.getValue(), data);

        assertEquals(PaymentStatus.PENDING.getValue(), result.getStatus());
    }

    @Test
    void testAddPaymentWithEmptyCodAddressSetsRejectedStatus() {
        mockSaveReturnsPayment();

        Map<String, String> data = new HashMap<>();
        data.put("address", "");
        data.put("deliveryFee", "15000");

        Payment result = paymentService.addPayment(order, PaymentMethod.COD.getValue(), data);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testSetStatusToSuccessUpdatesOrderStatus() {
        Payment payment = new Payment("1", PaymentMethod.VOUCHER.getValue(), order, new HashMap<>());

        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
    }

    @Test
    void testSetStatusToRejectedUpdatesOrderStatusToFailed() {
        Payment payment = new Payment("1", PaymentMethod.COD.getValue(), order, new HashMap<>());

        Payment result = paymentService.setStatus(payment, PaymentStatus.REJECTED.getValue());

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
    }

    @Test
    void testGetPaymentReturnsRepositoryResult() {
        Payment payment = new Payment("payment-1", PaymentMethod.VOUCHER.getValue(), order, new HashMap<>());
        when(paymentRepository.findById("payment-1")).thenReturn(payment);

        Payment result = paymentService.getPayment("payment-1");

        assertEquals(payment, result);
    }

    @Test
    void testGetAllPaymentsReturnsRepositoryResults() {
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment("payment-1", PaymentMethod.VOUCHER.getValue(), order, new HashMap<>()));
        payments.add(new Payment("payment-2", PaymentMethod.COD.getValue(), order, new HashMap<>()));
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = paymentService.getAllPayments();

        assertEquals(2, result.size());
    }
}