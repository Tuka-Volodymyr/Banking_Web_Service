package com.example.banking_web_service.data;

import com.example.banking_web_service.entities.Account;
import com.example.banking_web_service.entities.CreditCard;
import com.example.banking_web_service.exceptions.CreditCardNotFoundException;
import com.example.banking_web_service.repositories.CreditCardInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class CardData {
    private final CreditCardInfoRepository creditCardInfoRepository;
    @Autowired
    public CardData(CreditCardInfoRepository creditCardInfoRepository) {
        this.creditCardInfoRepository = creditCardInfoRepository;
    }

    public void saveCreditCard(CreditCard creditCard){
        creditCardInfoRepository.save(creditCard);
    }

    public CreditCard findCardByEmail(String emailOfOwner){
        return creditCardInfoRepository
                .findByEmailOfOwner(emailOfOwner)
                .orElseThrow(CreditCardNotFoundException::new);
    }
    public CreditCard findCardByCard(String card){
        return creditCardInfoRepository
                .findByCard(card)
                .orElseThrow(CreditCardNotFoundException::new);
    }
    public void existCard(String email){
        Optional<CreditCard> card =creditCardInfoRepository.findByEmailOfOwner(email);
        if(card.isPresent())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User has already had credit card!");
    }
}
