package com.example.banking_web_service.services;

import com.example.banking_web_service.repositories.repositoriesImpl.AccountData;
import com.example.banking_web_service.repositories.repositoriesImpl.CardData;
import com.example.banking_web_service.model.dto.ChangeTurnoverLimit;
import com.example.banking_web_service.model.dto.CreditCardDto;
import com.example.banking_web_service.model.dto.MoneyToCard;
import com.example.banking_web_service.model.entities.Account;
import com.example.banking_web_service.model.entities.CreditCard;
import com.example.banking_web_service.model.entities.GlobalEvent;
import com.example.banking_web_service.model.entities.OperationEvent;
import com.example.banking_web_service.model.exceptions.CardIsLockException;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Data
public class CardService {
    @Autowired
    private CardData cardData;
    @Autowired
    private AccountData accountData;
    @Autowired
    private AccountService accountService;
    @Autowired
    private EventService eventService;
    public CreditCardDto addCreditCard(String email){
        Account account=accountData.findAccountByEmail(email);
        cardData.existCard(email);
        CreditCard creditCard=new CreditCard();
        creditCard.setOwner(account.getFullName());
        creditCard.setEmailOfOwner(account.getEmail());
        cardData.saveCreditCard(creditCard);
        eventService.createCard(email);
        return creditCard.dto();
    }
    public CreditCard getInfoAboutCard(UserDetails userDetails){
        return cardData.findCardByEmail(userDetails.getUsername());
    }

    public CreditCardDto addMoney( MoneyToCard moneyToCard){
        CreditCard creditCard=cardData.findCardByCard(moneyToCard.getCard());
        checkTurnover(creditCard.getEmailOfOwner());
        return addMoneyTransaction(moneyToCard,creditCard);
    }
    @Transactional
    public CreditCardDto addMoneyTransaction(MoneyToCard moneyToCard,CreditCard creditCard){
        cardIsLock(creditCard);
        creditCard.setBalance(creditCard.getBalance()+moneyToCard.getAmount());
        creditCard.setTurnover(creditCard.getTurnover()+moneyToCard.getAmount());
        cardData.saveCreditCard(creditCard);
        eventService.addMoney(creditCard,moneyToCard);
        return creditCard.dto();
    }
    public OperationEvent transferMoney(MoneyToCard moneyToCard, UserDetails userDetails){
        CreditCard cardOfSubject=cardData.findCardByEmail(userDetails.getUsername());
        CreditCard cardOfObject= cardData.findCardByCard(moneyToCard.getCard());
        checkTurnover(cardOfObject.getEmailOfOwner());
        return transferMoneyTransaction(moneyToCard,cardOfSubject,cardOfObject);
    }
    @Transactional
    public OperationEvent transferMoneyTransaction(MoneyToCard moneyToCard,CreditCard cardOfSubject,CreditCard cardOfObject){
        cardIsLock(cardOfObject);
        cardIsLock(cardOfSubject);
        if(cardOfSubject.getBalance()<moneyToCard.getAmount())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Balance is smaller that suma of transaction!");
        cardOfSubject.setBalance(cardOfSubject.getBalance()-moneyToCard.getAmount());
        cardOfObject.setBalance(cardOfObject.getBalance()+moneyToCard.getAmount());
        cardOfObject.setTurnover(cardOfObject.getTurnover()+moneyToCard.getAmount());
        cardData.saveCreditCard(cardOfObject);
        cardData.saveCreditCard(cardOfSubject);
        return eventService.transaction(cardOfSubject,cardOfObject,moneyToCard);
    }
    public void checkTurnover(String email){
        CreditCard creditCard=cardData.findCardByEmail(email);
        if(creditCard.getTurnover()>creditCard.getLimitOfTurnover()){
            accountService.lockAccount(email);
            lockCard(email);
            throw new ResponseStatusException(HttpStatus.LOCKED,
                    String.format("""
                            %s your account and card was locked.
                            Turnover on your card is %.2f that bigger than %.2f ua.
                            Present a document about earnings!""",
                            creditCard.getOwner(),creditCard.getTurnover(),creditCard.getLimitOfTurnover()));
        }
    }
    public GlobalEvent newTurnoverLimit(ChangeTurnoverLimit changeTurnoverLimit){
        CreditCard creditCard=cardData.findCardByEmail(changeTurnoverLimit.getEmail());
        creditCard.setLimitOfTurnover(changeTurnoverLimit.getLimitOfTurnover());
        cardData.saveCreditCard(creditCard);
        return eventService.newLimitOfTurnover(creditCard);
    }
    public void cardIsLock(CreditCard creditCard){
        if(creditCard.isCardIsLocked())throw new CardIsLockException();
    }
    public GlobalEvent lockCard(String email){
        CreditCard creditCard=cardData.findCardByEmail(email);
        if(creditCard.isCardIsLocked())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Card has already locked");
        creditCard.setCardIsLocked(true);
        cardData.saveCreditCard(creditCard);
        return eventService.lockCard(creditCard);
    }
    public GlobalEvent unlockCard(String email){
        CreditCard creditCard=cardData.findCardByEmail(email);
        if(!creditCard.isCardIsLocked())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Card has already unlocked");
        creditCard.setCardIsLocked(false);
        cardData.saveCreditCard(creditCard);
        return eventService.unlockCard(creditCard);
    }
}
