package com.example.crudspringboot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "card_id")
    @ElementCollection
    private List<Long> cardsid = new ArrayList<>();
}