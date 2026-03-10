package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {

    private static final String VOUCHER_METHOD = "VOUCHER";
    private static final String COD_METHOD = "COD";
    private static final String VOUCHER_CODE_KEY = "voucherCode";
    private static final String COD_ADDRESS_KEY = "address";
    private static final String COD_DELIVERY_FEE_KEY = "deliveryFee";

    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping("/create")
    public String createOrderPage() {
        return "CreateOrder";
    }

    @PostMapping("/create")
    public String createOrderPost(
            @RequestParam String author,
            @RequestParam String productId,
            @RequestParam String productName,
            @RequestParam int productQuantity
    ) {
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName(productName);
        product.setProductQuantity(productQuantity);

        List<Product> products = new ArrayList<>();
        products.add(product);

        Order order = new Order(UUID.randomUUID().toString(), products, System.currentTimeMillis(), author);
        orderService.createOrder(order);

        return "redirect:/order/history";
    }

    @GetMapping("/history")
    public String orderHistoryPage() {
        return "OrderHistory";
    }

    @PostMapping("/history")
    public String orderHistoryPost(@RequestParam String author, Model model) {
        model.addAttribute("author", author);
        model.addAttribute("orders", orderService.findAllByAuthor(author));
        return "OrderList";
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(@PathVariable String orderId, Model model) {
        model.addAttribute("order", orderService.findById(orderId));
        return "PayOrder";
    }

    @PostMapping("/pay/{orderId}")
    public String payOrderPost(
            @PathVariable String orderId,
            @RequestParam String method,
            @RequestParam Map<String, String> requestParams,
            Model model
    ) {
        Order order = orderService.findById(orderId);
        Payment payment = paymentService.addPayment(order, method, extractPaymentData(method, requestParams));

        model.addAttribute("payment", payment);
        model.addAttribute("paymentId", payment.getId());
        return "PaymentSubmitted";
    }

    private Map<String, String> extractPaymentData(String method, Map<String, String> requestParams) {
        Map<String, String> paymentData = new HashMap<>();

        if (VOUCHER_METHOD.equalsIgnoreCase(method)) {
            paymentData.put(VOUCHER_CODE_KEY, requestParams.get(VOUCHER_CODE_KEY));
        }

        if (COD_METHOD.equalsIgnoreCase(method)) {
            paymentData.put(COD_ADDRESS_KEY, requestParams.get(COD_ADDRESS_KEY));
            paymentData.put(COD_DELIVERY_FEE_KEY, requestParams.get(COD_DELIVERY_FEE_KEY));
        }

        return paymentData;
    }
}