package com.vr.ProductService.service;

import com.vr.ProductService.model.ProductRequest;
import com.vr.ProductService.model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);
}
