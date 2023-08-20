package com.vr.OrderService.external.client;

import com.vr.OrderService.exception.CustomException;
import com.vr.OrderService.external.request.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name="PAYMENT-SERVICE/payment")
public interface PaymentService {

    @PostMapping
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);

    //as this is the interface we can use the java 8 feature for creating the default methods here to define the fallback method
    default void fallback(Exception e) {
        throw new CustomException("Payment Service is not available","UNAVAILABLE",500);
    }
}
