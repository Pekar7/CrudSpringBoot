package com.example.crudspringboot.model;

import com.example.crudspringboot.dto.OrderItem;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "order_item", joinColumns = @JoinColumn(name = "order_id"))
    private List<OrderItem> order;

}