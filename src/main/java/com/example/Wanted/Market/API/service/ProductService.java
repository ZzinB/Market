package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Product;

import java.util.List;

public interface ProductService {
    void addProduct(Product product, String sellerUsername);

    List<Product> getAllProducts();

    Product getProduct(Long productId);

    void buyProduct(Long productId, String buyerUsername, String paymentMethod);

    void updateProduct(Long productId, Product updatedProduct);

    void deleteProduct(Long productId);
}
