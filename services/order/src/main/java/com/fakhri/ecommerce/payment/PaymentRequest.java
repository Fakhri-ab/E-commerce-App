package com.fakhri.ecommerce.payment;

import com.fakhri.ecommerce.customer.CustomerResponse;
import com.fakhri.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
