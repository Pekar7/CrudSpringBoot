package com.example.crudspringboot.service.order;

import com.example.crudspringboot.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
