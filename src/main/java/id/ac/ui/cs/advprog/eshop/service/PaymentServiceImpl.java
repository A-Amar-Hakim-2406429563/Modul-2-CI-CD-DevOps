package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final String PAYMENT_METHOD_VOUCHER = "VOUCHER";
    private static final String PAYMENT_METHOD_COD = "COD";
    private static final String PAYMENT_STATUS_SUCCESS = "SUCCESS";
    private static final String PAYMENT_STATUS_REJECTED = "REJECTED";
    private static final String PAYMENT_STATUS_PENDING = "PENDING";
    private static final String ORDER_STATUS_FAILED = "FAILED";
    private static final String VOUCHER_CODE_KEY = "voucherCode";
    private static final String COD_ADDRESS_KEY = "address";
    private static final String COD_DELIVERY_FEE_KEY = "deliveryFee";
    private static final String VOUCHER_PREFIX = "ESHOP";
    private static final int VOUCHER_LENGTH = 16;
    private static final int VOUCHER_DIGIT_COUNT = 8;

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String status = determineInitialStatus(method, paymentData);
        Payment payment = new Payment(UUID.randomUUID().toString(), method, order, status, paymentData);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        updateRelatedOrderStatus(payment, status);

        return payment;
    }

    private void updateRelatedOrderStatus(Payment payment, String status) {
        if (PAYMENT_STATUS_SUCCESS.equals(status)) {
            payment.getOrder().setStatus(PAYMENT_STATUS_SUCCESS);
        }

        if (PAYMENT_STATUS_REJECTED.equals(status)) {
            payment.getOrder().setStatus(ORDER_STATUS_FAILED);
        }
    }

    private String determineInitialStatus(String method, Map<String, String> paymentData) {
        if (PAYMENT_METHOD_VOUCHER.equals(method)) {
            return isValidVoucher(paymentData) ? PAYMENT_STATUS_SUCCESS : PAYMENT_STATUS_REJECTED;
        }

        if (PAYMENT_METHOD_COD.equals(method) && hasBlankValue(paymentData, COD_ADDRESS_KEY, COD_DELIVERY_FEE_KEY)) {
            return PAYMENT_STATUS_REJECTED;
        }

        return PAYMENT_STATUS_PENDING;
    }

    private boolean isValidVoucher(Map<String, String> paymentData) {
        String voucherCode = paymentData.get(VOUCHER_CODE_KEY);
        if (voucherCode == null || voucherCode.length() != VOUCHER_LENGTH || !voucherCode.startsWith(VOUCHER_PREFIX)) {
            return false;
        }

        int digitCount = 0;
        for (char currentChar : voucherCode.toCharArray()) {
            if (Character.isDigit(currentChar)) {
                digitCount++;
            }
        }

        return digitCount == VOUCHER_DIGIT_COUNT;
    }

    private boolean hasBlankValue(Map<String, String> paymentData, String... keys) {
        for (String key : keys) {
            String value = paymentData.get(key);
            if (value == null || value.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
