package com.vr.OrderService.service;

import com.vr.OrderService.entity.Order;
import com.vr.OrderService.exception.CustomException;
import com.vr.OrderService.external.client.PaymentService;
import com.vr.OrderService.external.client.ProductService;
import com.vr.OrderService.external.request.PaymentRequest;
import com.vr.OrderService.external.response.PaymentResponse;
import com.vr.OrderService.model.OrderRequest;
import com.vr.OrderService.model.OrderResponse;
import com.vr.OrderService.repository.OrderRepository;
import com.vr.ProductService.model.ProductResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        //Create order entity and save the data with status Order Created
        //After that we can call product service to block products(Reduce quantity)
        //after checking whether the required quantity of order is available then we reduce the order quantity from stock and then we need to call the payment service to complete the payment as well.
       //If payment is successful then we can mark SUCCESS else CANCELLED
        log.info("Placing Order Request: {}", orderRequest);
        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());
        //In the above line we are using/calling our productService API using feign client's declarative approach
        log.info("Creating order with status CREATED");
        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();
        order = orderRepository.save(order);

        log.info("Calling Payment Service to complete the payment ");

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();

        String orderStatus = null;
        try{
            paymentService.doPayment(paymentRequest);
            log.info("Payment done Successfully. Changing the Order status to PLACED");
            orderStatus="PLACED";
        } catch (Exception e) {
            log.error("Error occured in payment. Changing order status to PAYMENT_FAILED");
            orderStatus = "PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        log.info("Order Places successfully with Order Id: {}", order.getId());

        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get order details for OrderId : {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new CustomException("Order not found for the order Id:"+orderId,"NOT_FOUND", 404));

        log.info("Invoking Product service to fetch the product for id: {}", order.getProductId());
        ProductResponse productResponse = restTemplate.getForObject(
                "http://PRODUCT-SERVICE/product/"+order.getProductId(),
                ProductResponse.class
        );
        log.info("Getting Payment information from the payment Service");
        PaymentResponse paymentResponse = restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payment/order/"+order.getId(),
                PaymentResponse.class
        );
        OrderResponse.ProductDetails productDetails
                = OrderResponse.ProductDetails.builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .build();

        OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails.builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentStatus(paymentResponse.getStatus())
                .paymentMode(paymentResponse.getPaymentMode())
                .paymentDate(paymentResponse.getPaymentDate())
                .build();
        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();

        return orderResponse;
    }
}
