package com.fakhri.ecommerce.kafka;

import com.fakhri.ecommerce.customer.CustomerResponse;
import com.fakhri.ecommerce.order.PaymentMethod;
import com.fakhri.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
