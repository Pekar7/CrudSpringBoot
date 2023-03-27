package com.example.crudspringboot.service.card;

import com.example.crudspringboot.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card addCard(Card card) {
        cardRepository.save(card);
        return card;
    }

    public List<Card> getAllCards() {
        return (List<Card>) cardRepository.findAll();
    }

    public Card getCardById(Long id) {
        return cardRepository.findById(id).stream().findFirst().get();
    }

    public String deleteCardById(Long id){
        cardRepository.deleteById(id);
        return "Deleted";
    }
}
