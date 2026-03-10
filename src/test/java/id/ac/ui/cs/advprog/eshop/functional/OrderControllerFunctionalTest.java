package id.ac.ui.cs.advprog.eshop.functional;

import id.ac.ui.cs.advprog.eshop.controller.OrderController;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// Mengganti import MockBean yang sudah usang dengan MockitoBean
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(OrderController.class)
class OrderControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Mengganti @MockBean
    private OrderService orderService;

    @MockitoBean // Mengganti @MockBean
    private PaymentService paymentService;

    private Order order;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Test Product");
        product.setProductQuantity(2);

        List<Product> products = new ArrayList<>();
        products.add(product);

        order = new Order("order-1", products, 1708560000L, "Amar");
    }

    @Test
    void testCreateOrderPage() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateOrder"));
    }

    @Test
    void testHistoryOrderPage() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderHistory"));
    }

    @Test
    void testPostHistoryReturnsOrdersByAuthor() throws Exception {
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        when(orderService.findAllByAuthor("Amar")).thenReturn(orders);

        mockMvc.perform(post("/order/history")
                        .param("author", "Amar"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderList"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("author", "Amar"));
    }

    @Test
    void testPayOrderPage() throws Exception {
        when(orderService.findById("order-1")).thenReturn(order);

        mockMvc.perform(get("/order/pay/order-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("PayOrder"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    void testPayOrderPostReturnsPaymentDetailPage() throws Exception {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        // FIX: Menghapus parameter "SUCCESS" karena status sudah di-handle di dalam Model
        Payment payment = new Payment("payment-1", "VOUCHER", order, paymentData);

        when(orderService.findById("order-1")).thenReturn(order);
        when(paymentService.addPayment(eq(order), eq("VOUCHER"), anyMap())).thenReturn(payment);

        mockMvc.perform(post("/order/pay/order-1")
                        .param("method", "VOUCHER")
                        .param("voucherCode", "ESHOP1234ABC5678"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentSubmitted"))
                .andExpect(model().attributeExists("payment"))
                .andExpect(model().attribute("paymentId", "payment-1"));
    }
}