package com.example.Wanted.Market.API.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long productId) {
        super("Could not find product with ID: " + productId);
    }
}

