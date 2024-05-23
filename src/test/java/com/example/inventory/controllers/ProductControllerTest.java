package com.example.inventory.controllers;

import com.example.inventory.models.Product;
import com.example.inventory.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllProducts() throws Exception {
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

        Mockito.when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(product1.getName())))
                .andExpect(jsonPath("$[0].brand", is(product1.getBrand())))
                .andExpect(jsonPath("$[0].price", is(product1.getPrice())))
                .andExpect(jsonPath("$[0].quantity", is(product1.getQuantity())))
                .andExpect(jsonPath("$[1].name", is(product2.getName())))
                .andExpect(jsonPath("$[1].brand", is(product2.getBrand())))
                .andExpect(jsonPath("$[1].price", is(product2.getPrice())))
                .andExpect(jsonPath("$[1].quantity", is(product2.getQuantity())));
    }

    @Test
    @WithAnonymousUser
    void getAllProductsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getProductById() throws Exception {
        Product product = Product.builder()
                .name("Macbook Pro")
                .brand("Apple")
                .price(1099.00)
                .quantity(120).build();

        Mockito.when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(product.getName())))
                .andExpect(jsonPath("$.brand", is(product.getBrand())))
                .andExpect(jsonPath("$.price", is(product.getPrice())))
                .andExpect(jsonPath("$.quantity", is(product.getQuantity())));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getProductByIdNonExistent() throws Exception {
        Product product = Product.builder()
                .name("Macbook Pro")
                .brand("Apple")
                .price(1099.00)
                .quantity(120).build();

        Mockito.when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/v1/products/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createProduct() throws Exception {
        Product product = Product.builder()
                .name("Macbook Pro")
                .brand("Apple")
                .price(1099.00)
                .quantity(120).build();

        Mockito.when(productService.saveProduct(Mockito.any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(product.getName())))
                .andExpect(jsonPath("$.brand", is(product.getBrand())))
                .andExpect(jsonPath("$.price", is(product.getPrice())))
                .andExpect(jsonPath("$.quantity", is(product.getQuantity())));
    }

    @Test
    @WithMockUser(roles = "USER")
    void createProductUnauthorized() throws Exception {
        Product product = Product.builder()
                .name("Macbook Pro")
                .brand("Apple")
                .price(1099.00)
                .quantity(120).build();

        Mockito.when(productService.saveProduct(Mockito.any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateProductNonExistent() throws Exception {
        Product product = Product.builder()
                .name("Macbook Pro")
                .brand("Apple")
                .price(1099.00)
                .quantity(120).build();

        Mockito.when(productService.saveProduct(Mockito.any(Product.class))).thenReturn(product);

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateProduct() throws Exception {
        Product product = Product.builder()
                .name("Macbook Pro")
                .brand("Apple")
                .price(1099.00)
                .quantity(120).build();

        Mockito.when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        product.setQuantity(100);
        Mockito.when(productService.saveProduct(Mockito.any(Product.class))).thenReturn(product);

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(product.getName())))
                .andExpect(jsonPath("$.brand", is(product.getBrand())))
                .andExpect(jsonPath("$.price", is(product.getPrice())))
                .andExpect(jsonPath("$.quantity", is(product.getQuantity())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteProduct() throws Exception {
        mockMvc.perform(delete("/api/v1/products/1")
                        .with(csrf()))
                .andExpect(status().isOk());

        Mockito.verify(productService, Mockito.times(1)).deleteProduct(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void searchProducts() throws Exception {
        Product product = Product.builder()
                .name("Macbook Pro")
                .brand("Apple")
                .price(1099.00)
                .quantity(120).build();

        Mockito.when(productService.searchProducts("macbook")).thenReturn(List.of(product));

        mockMvc.perform(get("/api/v1/products/search").param("query", "macbook"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(product.getName())))
                .andExpect(jsonPath("$[0].brand", is(product.getBrand())))
                .andExpect(jsonPath("$[0].price", is(product.getPrice())))
                .andExpect(jsonPath("$[0].quantity", is(product.getQuantity())));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getLeftoverProducts() throws Exception {
        Product product = Product.builder()
                .name("Airpods 2")
                .brand("Apple")
                .price(960.00)
                .quantity(3).build();

        Mockito.when(productService.getLeftoverProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/v1/products/leftovers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(product.getName())))
                .andExpect(jsonPath("$[0].brand", is(product.getBrand())))
                .andExpect(jsonPath("$[0].price", is(product.getPrice())))
                .andExpect(jsonPath("$[0].quantity", is(product.getQuantity())));
    }
}
