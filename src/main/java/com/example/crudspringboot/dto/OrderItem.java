package com.example.crudspringboot.dto;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class OrderItem {
    private Long productId;
    private Integer quantity;
}
