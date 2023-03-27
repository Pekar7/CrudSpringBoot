package com.example.crudspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardDto {

    private Long id;

    private String title;

    private String description;

    private Double price;

    private String img;
}
