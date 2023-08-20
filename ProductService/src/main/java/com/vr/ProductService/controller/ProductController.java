package com.vr.ProductService.controller;

import com.vr.ProductService.model.ProductRequest;
import com.vr.ProductService.model.ProductResponse;
import com.vr.ProductService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NamedStoredProcedureQueries;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService; // for calling business layer

    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping
    public ResponseEntity<Long> addProduct(@RequestBody ProductRequest productRequest){
        long productId = productService.addProduct(productRequest);
        return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer') || hasAuthority('SCOPE_internal')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") long productId) {
        ProductResponse productResponse =
                productService.getProductById(productId);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);

    }

    @PutMapping("/reduceQuantity/{id}")
    public ResponseEntity<Void> reduceQuantity(@PathVariable("id") long productId,
                                               @RequestParam long quantity) {

        productService.reduceQuantity(productId,quantity);
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
