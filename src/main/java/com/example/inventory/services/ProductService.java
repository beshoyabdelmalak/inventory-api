package com.example.inventory.services;

import com.example.inventory.models.Product;
import com.example.inventory.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingOrBrandContaining(query);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getLeftoverProducts() {
        int LEFTOVER_PRODUCT_QUANTITY_THRESHOLD = 5;
        return productRepository.findByQuantityLessThan(LEFTOVER_PRODUCT_QUANTITY_THRESHOLD);
    }
}
