package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Product;
import com.example.Wanted.Market.API.domain.ProductStatus;
import com.example.Wanted.Market.API.exception.ResourceNotFoundException;
import com.example.Wanted.Market.API.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product) {
        product.setStatus(ProductStatus.AVAILABLE); // 초기 상태 설정
        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, Product productDetails) {
        Product product = getProductById(productId);
        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setStatus(productDetails.getStatus());
        return productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
