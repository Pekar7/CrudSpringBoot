package com.example.crudspringboot.service.card;


import com.example.crudspringboot.model.Card;
import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<Card, Long> {

}
