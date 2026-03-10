package id.ac.ui.cs.advprog.eshop.functional;

import id.ac.ui.cs.advprog.eshop.controller.PaymentController;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PaymentController.class)
class PaymentControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    private Payment payment;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Test Product");
        product.setProductQuantity(1);

        List<Product> products = new ArrayList<>();
        products.add(product);

        Order order = new Order("order-1", products, 1708560000L, "Amar");

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        payment = new Payment("payment-1", "VOUCHER", order, "SUCCESS", paymentData);
    }

    @Test
    void testPaymentDetailPage() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentDetailSearch"));
    }

    @Test
    void testPaymentDetailByIdPage() throws Exception {
        when(paymentService.getPayment("payment-1")).thenReturn(payment);

        mockMvc.perform(get("/payment/detail/payment-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentDetail"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void testAdminPaymentListPage() throws Exception {
        List<Payment> payments = new ArrayList<>();
        payments.add(payment);
        when(paymentService.getAllPayments()).thenReturn(payments);

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentAdminList"))
                .andExpect(model().attributeExists("payments"));
    }

    @Test
    void testAdminPaymentDetailPage() throws Exception {
        when(paymentService.getPayment("payment-1")).thenReturn(payment);

        mockMvc.perform(get("/payment/admin/detail/payment-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentAdminDetail"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void testSetPaymentStatusRedirectsToAdminDetailPage() throws Exception {
        when(paymentService.getPayment("payment-1")).thenReturn(payment);
        when(paymentService.setStatus(payment, "REJECTED")).thenReturn(payment);

        mockMvc.perform(post("/payment/admin/set-status/payment-1")
                        .param("status", "REJECTED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/detail/payment-1"));
    }
}