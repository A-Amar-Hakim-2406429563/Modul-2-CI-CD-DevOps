package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/detail")
    public String paymentDetailPage() {
        return "PaymentDetailSearch";
    }

    @GetMapping("/detail/{paymentId}")
    public String paymentDetailByIdPage(@PathVariable String paymentId, Model model) {
        model.addAttribute("payment", paymentService.getPayment(paymentId));
        return "PaymentDetail";
    }

    @GetMapping("/admin/list")
    public String adminPaymentListPage(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        return "PaymentAdminList";
    }

    @GetMapping("/admin/detail/{paymentId}")
    public String adminPaymentDetailPage(@PathVariable String paymentId, Model model) {
        model.addAttribute("payment", paymentService.getPayment(paymentId));
        return "PaymentAdminDetail";
    }

    @PostMapping("/admin/set-status/{paymentId}")
    public String setPaymentStatus(@PathVariable String paymentId, @RequestParam String status) {
        Payment payment = paymentService.getPayment(paymentId);
        paymentService.setStatus(payment, status);
        return "redirect:/payment/admin/detail/" + paymentId;
    }
}