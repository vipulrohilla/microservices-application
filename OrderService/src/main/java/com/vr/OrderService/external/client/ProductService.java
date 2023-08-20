package com.vr.OrderService.external.client;

import com.vr.OrderService.exception.CustomException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name="PRODUCT-SERVICE/product")
public interface ProductService {

    //Here we need to add the declaration of the endpoint which we want to use
    @PutMapping("/reduceQuantity/{id}")
    public ResponseEntity<Void> reduceQuantity(@PathVariable("id") long productId,
                                               @RequestParam long quantity);

    default void fallback(Exception e){
        throw new CustomException("Product Service is not available",
                "UNAVAILABLE",
                500);
    }

}
