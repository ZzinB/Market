package com.example.Wanted.Market.API.controller;

import com.example.Wanted.Market.API.domain.Product;
import com.example.Wanted.Market.API.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody Product product, Principal principal) {
        productService.addProduct(product, principal.getName());
        return ResponseEntity.ok("제품 등록 성공");
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @PostMapping("/{productId}/buy")
    public ResponseEntity<?> buyProduct(@PathVariable Long productId, Principal principal) {
        productService.buyProduct(productId, principal.getName());
        return ResponseEntity.ok("구매 요청 성공");
    }
}

