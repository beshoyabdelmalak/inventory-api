package com.example.inventory.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
    private Long id;

    @NotBlank(message="Product Name should be set")
    private String name;

    @NotBlank(message="Product Brand should be set")
    private String brand;

    @NotNull(message="Product Price should be set")
    @Min(value=0, message="Price can't have a negative value")
    private BigDecimal price;

    @NotNull(message="Product quantity should be set")
    @Min(value=0, message="Price can't have a negative value")
    private Integer quantity;

}
