package com.vr.OrderService.service;

import com.vr.OrderService.model.OrderRequest;
import com.vr.OrderService.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
