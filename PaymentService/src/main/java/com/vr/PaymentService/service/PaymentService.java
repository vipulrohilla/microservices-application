package com.vr.PaymentService.service;

import com.vr.PaymentService.model.PaymentRequest;
import com.vr.PaymentService.model.PaymentResponse;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(String orderId);
}
