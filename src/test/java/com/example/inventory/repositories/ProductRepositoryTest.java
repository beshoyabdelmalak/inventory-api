package com.example.inventory.repositories;

import com.example.inventory.models.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testFindByNameContainingOrBrandContaining() {
        Product product1 = Product.builder()
                .name("Macbook Pro")
                .brand("Apple")
                .price(1099.00)
                .quantity(120).build();

        Product product2 = Product.builder()
                .name("Airpods 2")
                .brand("Apple")
                .price(960.00)
                .quantity(23).build();

        productRepository.save(product1);
        productRepository.save(product2);

        List<Product> products = productRepository.findByNameContainingOrBrandContaining("Apple");
        assertThat(products)
                .hasSize(2).
                containsExactly(product1, product2);

        products = productRepository.findByNameContainingOrBrandContaining("macbook");
        assertThat(products)
                .hasSize(1)
                .containsExactly(product1);
    }

    @Test
    void testSaveAndFindById() {
        Product product = Product.builder()
                .name("Macbook Pro")
                .brand("Apple")
                .price(1099.00)
                .quantity(120).build();

        product = productRepository.save(product);
        assertNotNull(product.getId());

        Optional<Product> result = productRepository.findById(product.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(product);
    }

    @Test
    void testFindByIdNonExistent() {
        Optional<Product> foundProduct = productRepository.findById(1L);
        assertThat(foundProduct).isEmpty();
    }

    @Test
    void testDeleteById() {
        Product product = Product.builder()
                .name("Macbook Pro")
                .brand("Apple")
                .price(1099.00)
                .quantity(120).build();

        product = productRepository.save(product);
        Long productId = product.getId();
        assertNotNull(productId);

        productRepository.deleteById(productId);
        Optional<Product> foundProduct = productRepository.findById(productId);
        assertThat(foundProduct).isEmpty();
    }

    @Test
    void testFindAll() {
        Product product1 = Product.builder()
                .name("Macbook Pro")
                .brand("Apple")
                .price(1099.00)
                .quantity(120).build();

        Product product2 = Product.builder()
                .name("Airpods 2")
                .brand("Apple")
                .price(960.00)
                .quantity(23).build();

        productRepository.save(product1);
        productRepository.save(product2);

        List<Product> products = productRepository.findAll();
        assertThat(products)
                .hasSize(2)
                .containsExactly(product1, product2);
    }

    @Test
    public void testUpdate() {
        Product product = Product.builder()
                .name("Macbook Pro")
                .brand("Apple")
                .price(1099.00)
                .quantity(120).build();

        productRepository.save(product);

        product.setPrice(300);
        product.setQuantity(118);
        productRepository.save(product);

        Optional<Product> result = productRepository.findById(product.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(product);
    }

    @Test
    public void testFindByQuantityLessThan() {
        Product product1 = Product.builder()
                .name("Macbook Pro")
                .brand("Apple")
                .price(1099.00)
                .quantity(120).build();

        Product product2 = Product.builder()
                .name("Airpods 2")
                .brand("Apple")
                .price(960.00)
                .quantity(19).build();

        productRepository.save(product1);
        productRepository.save(product2);

        List<Product> result = productRepository.findByQuantityLessThan(20);
        assertThat(result)
                .hasSize(1)
                .containsExactly(product2);
    }
}