package com.example.crudspringboot.controller;

import com.example.crudspringboot.dto.OrderDto;
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

import java.util.ArrayList;
import java.util.List;

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
        List<Long> orders = new ArrayList<>();
        if (userInfoRepository.existsById(orderDto.getUserId())) {
            UserInfo userInfo = userInfoRepository.findById(orderDto.getUserId());
            List<Long> ordersDto = orderDto.getProductsId();
            for (int i = 0; i < ordersDto.size(); i++) {
                if (cardRepository.existsById(ordersDto.get(i))) {
                    orders.add(ordersDto.get(i));
                } else {
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нет такого продукта " + ordersDto.get(i));
                }
            }
            Order order = new Order();
            order.setUserId((long) userInfo.getId());
            order.setCardsid(orders);
            orderRepository.save(order);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нет такого пользователя " + orderDto.getUserId());
        }

    }

    @GetMapping()
    public Object getOrders() {
        return orderRepository.findAll();
    }

}
