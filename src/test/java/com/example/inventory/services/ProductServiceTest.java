package com.example.inventory.services;

import com.example.inventory.models.Product;
import com.example.inventory.repositories   .ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    void saveProduct() {
        Product product = new Product();

        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productService.saveProduct(product);
        assertThat(savedProduct).isNotNull();
    }

    @Test
    void getAllProducts() {
        Product product1 = new Product();
        Product product2 = new Product();

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.getAllProducts();
        assertThat(products).hasSize(2);
    }

    @Test
    void getProductById() {
        Product product = new Product();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> foundProduct = productService.getProductById(1L);
        assertThat(foundProduct).isPresent();
    }

    @Test
    void deleteProduct() {
        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void searchProducts() {
        Product product1 = new Product();
        product1.setName("Product1");

        Product product2 = new Product();
        product2.setName("Product2");

        when(productRepository.findByNameContainingOrBrandContaining("Product"))
                .thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.searchProducts("Product");
        assertThat(products).hasSize(2);
    }

    @Test
    void getLeftoverProducts() {
        Product product1 = new Product();
        product1.setQuantity(3);

        Product product2 = new Product();
        product2.setQuantity(5);

        when(productRepository.findByQuantityLessThan(5)).thenReturn(List.of(product1));

        List<Product> leftoverProducts = productService.getLeftoverProducts();
        assertThat(leftoverProducts).hasSize(1).containsExactly(product1);
    }
}
