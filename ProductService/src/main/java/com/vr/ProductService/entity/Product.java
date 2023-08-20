package com.vr.ProductService.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data //By adding this all the getters and setters, equals, hashcode method and the required field constructor will be creaded bydefault
@AllArgsConstructor
@NoArgsConstructor
@Builder //This will give us the builder pattern implementation of this class
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long productId;

    @Column(name="PRODUCT_NAME")
    private String productName;
    @Column(name="PRICE")
    private long price;
    @Column(name="QUANTITY")
    private long quantity;
}
