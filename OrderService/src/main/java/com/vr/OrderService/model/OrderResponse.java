package com.vr.OrderService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    private long orderId;
    private Instant orderDate;
    private String orderStatus;
    private long amount;
    private ProductDetails productDetails;
    private PaymentDetails paymentDetails;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
//    public static class ProductResponse {
    public static class ProductDetails { //Note: This static class is added to use RestTemplate
        // ideally we should have a separate class for this but
        // as we are just learning about RestTemplate so we are crating static inner class to save time &
        // use it here.
        private String productName;
        private long productId;
        private long quantity;
        private long price;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PaymentDetails{
        private long paymentId;
        private PaymentMode paymentMode;
        private String paymentStatus;
        private Instant paymentDate;

    }

}
