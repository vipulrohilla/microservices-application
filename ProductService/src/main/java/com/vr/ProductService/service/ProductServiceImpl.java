package com.vr.ProductService.service;

import com.vr.ProductService.entity.Product;
import com.vr.ProductService.exception.ProductServiceCustomException;
import com.vr.ProductService.model.ProductRequest;
import com.vr.ProductService.model.ProductResponse;
import com.vr.ProductService.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding Product..");
        Product product = Product.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product Created");
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("Get Product for productId: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ProductServiceCustomException("Product with given id not found","PRODUCT_NOT_FOUND"));

        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product,productResponse);
//        About BeanUtils https://www.youtube.com/watch?v=2NPDOYQH-Ag
        //We can also use builder inplace of above logic>> NOTE: This will only work when there are matching properties

        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce Quantity {} for Id: {}",quantity, productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ProductServiceCustomException("Product with givent Id not found", "PRODUCT_NOT_FOUND"));

        if(product.getQuantity()< quantity) {
            throw new ProductServiceCustomException("Product does not have sufficient quantity",
                    "INSUFFICIENT_QUANTITY");
        }
        product.setQuantity(product.getQuantity()-quantity);
        productRepository.save(product);

        log.info("Product Quantity updated Successfully");


    }


}
