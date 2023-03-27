package com.example.crudspringboot.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private Long userId;
    private List<Long> productsId;
}
