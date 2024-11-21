package com.fakhri.ecommerce.order;

import com.fakhri.ecommerce.customer.CustomerClient;
import com.fakhri.ecommerce.exception.BusinessException;
import com.fakhri.ecommerce.kafka.OrderConfirmation;
import com.fakhri.ecommerce.kafka.OrderProducer;
import com.fakhri.ecommerce.orderline.OrderLineRequest;
import com.fakhri.ecommerce.orderline.OrderLineService;
import com.fakhri.ecommerce.payment.PaymentClient;
import com.fakhri.ecommerce.payment.PaymentRequest;
import com.fakhri.ecommerce.product.ProductClient;
import com.fakhri.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    public Integer createOrder(OrderRequest orderRequest) {

        //check customer (feign client)
        var customer = this.customerClient.findCustomerById(orderRequest.customerId()).orElseThrow(
                () -> new BusinessException("Cannot create order:: No customer exists with the provided ID")
        );

        //purchase the product ( rest Template )
        var purchasedProducts = productClient.purchaseProducts(orderRequest.products());

        //persist order
        var order = this.repository.save(mapper.toOrder(orderRequest));

        // persist orderLine

        for (PurchaseRequest purchaseRequest : orderRequest.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        //start payment process

        var paymentRequest = new PaymentRequest(
                orderRequest.amount(),
                orderRequest.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        // send notification using kafka

        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        orderRequest.reference(),
                        orderRequest.amount(),
                        orderRequest.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );

        return order.getId();

    }

    public List<OrderResponse> findAllOrders() {
        return this.repository.findAll()
                .stream()
                .map(this.mapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer id) {
        return this.repository.findById(id)
                .map(this.mapper::toOrderResponse)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No order found with the provided ID: %d", id)));
    }


}
