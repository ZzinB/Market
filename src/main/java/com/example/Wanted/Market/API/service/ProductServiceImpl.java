package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.config.ProductNotFoundException;
import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.domain.Product;
import com.example.Wanted.Market.API.domain.ProductStatus;
import com.example.Wanted.Market.API.repository.MemberRepository;
import com.example.Wanted.Market.API.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public void addProduct(Product product, String sellerUsername) {
        Member seller = memberRepository.findByUsername(sellerUsername).orElseThrow();
        product.setSeller(seller);
        product.setStatus(ProductStatus.AVAILABLE);
        productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    public void buyProduct(Long productId, String buyerUsername) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found"));
        Member buyer = memberRepository.findByUsername(buyerUsername).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (product.getStatus() != ProductStatus.AVAILABLE) {
            throw new IllegalStateException("Product is not available for purchase");
        }

        product.setBuyer(buyer);
        product.setStatus(ProductStatus.RESERVED);
        productRepository.save(product);
    }
}
