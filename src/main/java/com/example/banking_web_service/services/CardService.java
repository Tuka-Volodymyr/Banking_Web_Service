package com.example.banking_web_service.services;

import com.example.banking_web_service.data.AccountData;
import com.example.banking_web_service.data.CardData;
import com.example.banking_web_service.dto.CreditCardDto;
import com.example.banking_web_service.dto.MoneyToCard;
import com.example.banking_web_service.entities.Account;
import com.example.banking_web_service.entities.CreditCard;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CardService {
    @Autowired
    private CardData cardData;
    @Autowired
    private AccountData accountData;
    @Autowired
    private EventService eventService;
    public CreditCardDto addCreditCard(UserDetails userDetails){
        Account account=accountData.findAccountByEmail(userDetails.getUsername());
        cardData.existCard(userDetails.getUsername());
        CreditCard creditCard=new CreditCard();
        creditCard.setOwner(account.getFullName());
        creditCard.setEmailOfOwner(account.getEmail());
        cardData.saveCreditCard(creditCard);
        eventService.createCard(userDetails.getUsername());
        return creditCard.dto();
    }
    public CreditCard getInfoAboutCard(UserDetails userDetails){
        return cardData.findCardByEmail(userDetails.getUsername());
    }
    @Transactional
    public CreditCardDto addMoney( MoneyToCard moneyToCard){
        CreditCard creditCard= cardData.findCardByCard(moneyToCard.getCard());
        creditCard.setBalance(creditCard.getBalance()+moneyToCard.getAmount());
        cardData.saveCreditCard(creditCard);
        eventService.addMoney(creditCard.getEmailOfOwner(),moneyToCard.getAmount());
        return creditCard.dto();
    }
    @Transactional
    public CreditCardDto transferMoney( MoneyToCard moneyToCard,UserDetails userDetails){
        CreditCard CardOfSubject=cardData.findCardByEmail(userDetails.getUsername());
        CreditCard CardOfObject= cardData.findCardByCard(moneyToCard.getCard());
        if(CardOfSubject.getBalance()<moneyToCard.getAmount())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Balance is smaller that suma of transaction!");
        CardOfSubject.setBalance(CardOfSubject.getBalance()-moneyToCard.getAmount());
        CardOfObject.setBalance(CardOfObject.getBalance()+moneyToCard.getAmount());
        cardData.saveCreditCard(CardOfObject);
        cardData.saveCreditCard(CardOfSubject);
        eventService.transaction(CardOfSubject.getEmailOfOwner(),CardOfObject.getEmailOfOwner(),moneyToCard.getAmount());
        return CardOfSubject.dto();
    }
}
