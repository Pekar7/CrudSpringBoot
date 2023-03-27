package com.example.crudspringboot.controller;

import com.example.crudspringboot.model.Card;
import com.example.crudspringboot.service.card.CardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/products")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public ResponseEntity<List<Card>> getAllProducts() {
        List<Card> products = cardService.getAllCards();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable Long id) {
        Card card = cardService.getCardById(id);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    @PostMapping()
    public Object createProduct(@RequestBody Card card, HttpServletRequest request) {
        String encodedRole = request.getHeader("Role");
        String decodedRole = new String(Base64.getDecoder().decode(encodedRole), StandardCharsets.UTF_8);
        if ("ADMIN".equals(decodedRole)) {
            Card newProduct = cardService.addCard(card);
            return ResponseEntity.ok(newProduct);
        } else {
            return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ограничение прав доступа");
        }
    }

    @PutMapping("/{id}")
    public Object updateProduct(@PathVariable Long id, @RequestBody Card card, HttpServletRequest request) {
        String encodedRole = request.getHeader("Role");
        String decodedRole = new String(Base64.getDecoder().decode(encodedRole), StandardCharsets.UTF_8);
        if ("ADMIN".equals(decodedRole)) {
            Card existingProduct = cardService.getCardById(id);
            if (existingProduct != null) {
                existingProduct.setTitle(card.getTitle());
                existingProduct.setDescription(card.getDescription());
                existingProduct.setPrice(card.getPrice());
                existingProduct.setImg(card.getImg());
                cardService.addCard(existingProduct);
                return ResponseEntity.ok("Product updated successfully.");
            } else {
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Продукт не найден");
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/{id}")
    public Object deleteProductById(@PathVariable Long id, HttpServletRequest request) {
        String encodedRole = request.getHeader("Role");
        String decodedRole = new String(Base64.getDecoder().decode(encodedRole), StandardCharsets.UTF_8);
        if ("ADMIN".equals(decodedRole)) {
            cardService.deleteCardById(id);
            return ResponseEntity.ok("Product deleted successfully.");
        } else {
            return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нет доступа");
        }
    }
}
