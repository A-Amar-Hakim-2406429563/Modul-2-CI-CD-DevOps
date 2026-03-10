package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import lombok.Getter;

import java.util.Map;

@Getter
public class Payment {

    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;
    private Order order;

    public Payment(String id, String method, Order order, Map<String, String> paymentData) {
        this.id = id;
        this.method = method;
        this.order = order;
        this.paymentData = paymentData;

        // REFACTOR: Logika validasi dipindah dari Service ke Model (mengatasi Feature Envy)
        if (PaymentMethod.VOUCHER.getValue().equals(method)) {
            this.status = validateVoucher(paymentData) ? PaymentStatus.SUCCESS.getValue() : PaymentStatus.REJECTED.getValue();
        } else if (PaymentMethod.COD.getValue().equals(method)) {
            this.status = validateCOD(paymentData) ? PaymentStatus.PENDING.getValue() : PaymentStatus.REJECTED.getValue();
        } else {
            throw new IllegalArgumentException("Invalid payment method");
        }
    }

    // REFACTOR: Setter manual dengan validasi Enum
    public void setStatus(String status) {
        if (PaymentStatus.contains(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Invalid payment status");
        }
    }

    private boolean validateVoucher(Map<String, String> paymentData) {
        if (paymentData == null || !paymentData.containsKey("voucherCode")) {
            return false;
        }
        String voucherCode = paymentData.get("voucherCode");
        if (voucherCode == null || voucherCode.length() != 16 || !voucherCode.startsWith("ESHOP")) {
            return false;
        }

        int digitCount = 0;
        for (char c : voucherCode.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCount++;
            }
        }
        return digitCount == 8;
    }

    private boolean validateCOD(Map<String, String> paymentData) {
        if (paymentData == null) {
            return false;
        }
        String address = paymentData.get("address");
        String deliveryFee = paymentData.get("deliveryFee");

        return address != null && !address.trim().isEmpty() &&
                deliveryFee != null && !deliveryFee.trim().isEmpty();
    }
}