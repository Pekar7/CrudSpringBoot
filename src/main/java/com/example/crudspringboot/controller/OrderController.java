package com.example.crudspringboot.controller;

import com.example.crudspringboot.dto.OrderDto;
import com.example.crudspringboot.dto.OrderItem;
import com.example.crudspringboot.dto.OrderItemDto;
import com.example.crudspringboot.model.Order;
import com.example.crudspringboot.model.UserInfo;
import com.example.crudspringboot.service.card.CardRepository;
import com.example.crudspringboot.service.order.OrderRepository;
import com.example.crudspringboot.service.userInfo.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final UserInfoRepository userInfoRepository;
    private final CardRepository cardRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderController(UserInfoRepository infoRepository, CardRepository cardRepository, OrderRepository orderRepository) {
        this.userInfoRepository = infoRepository;
        this.cardRepository = cardRepository;
        this.orderRepository = orderRepository;
    }

    @PostMapping()
    public Object addOrder(@RequestBody OrderDto orderDto) {
        if (userInfoRepository.existsById(orderDto.getUserId())) {
            UserInfo userInfo = userInfoRepository.findById(orderDto.getUserId());

            List<OrderItemDto> orderItemDtos = orderDto.getOrder();
            List<OrderItem> orderItems = new ArrayList<>();
            for (OrderItemDto orderItemDto : orderItemDtos) {
                if (!cardRepository.existsById(orderItemDto.getProductId())) {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет продукта " + orderItemDto.getProductId());
                }
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(orderItemDto.getProductId());
                orderItem.setQuantity(orderItemDto.getQuantity());
                orderItems.add(orderItem);
            }

            Order order = new Order();
            order.setId((long) userInfo.getId());
            order.setUserId((long) userInfo.getId());
            order.setOrder(orderItems);
            orderRepository.save(order);

            return new ResponseEntity<>(orderDto, HttpStatus.OK);
        } else {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с id "+orderDto.getUserId()+" не найден" );
        }
    }

    @GetMapping("/{orderId}")
    public Object getOrder(@PathVariable Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Заказ с id " + orderId + " не найден");
        }
    }

    @PutMapping("/{id}")
    public Object updateOrder(@PathVariable Long id, @RequestBody OrderDto orderDto) {
        if (orderRepository.existsById(id)) {
            Order order = orderRepository.findById(id).orElseThrow();
            List<OrderItemDto> orderItemDtos = orderDto.getOrder();
            List<OrderItem> orderItems = new ArrayList<>();
            for (OrderItemDto orderItemDto : orderItemDtos) {
                if (!cardRepository.existsById(orderItemDto.getProductId())) {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет продукта " + orderItemDto.getProductId());
                }
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(orderItemDto.getProductId());
                orderItem.setQuantity(orderItemDto.getQuantity());
                orderItems.add(orderItem);
            }
            order.setOrder(orderItems);
            orderRepository.save(order);
            return new ResponseEntity<>(orderDto, HttpStatus.OK);
        } else {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет заказа " + id);
        }
    }

    @DeleteMapping("/{orderId}")
    public Object deleteOrder(@PathVariable Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            orderRepository.deleteById(orderId);
            return new ResponseEntity<>(orderId, HttpStatus.OK);
        } else {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Заказ с id " + orderId + " не найден");
        }
    }
}
