package com.example.banking_web_service.services;

import com.example.banking_web_service.repositories.repositoriesImpl.GlobalEventData;
import com.example.banking_web_service.repositories.repositoriesImpl.OperationEventData;
import com.example.banking_web_service.model.dto.MoneyToCard;
import com.example.banking_web_service.model.entities.Account;
import com.example.banking_web_service.model.entities.CreditCard;
import com.example.banking_web_service.model.entities.GlobalEvent;
import com.example.banking_web_service.model.entities.OperationEvent;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {
    @Autowired
    private GlobalEventData globalEventData;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private OperationEventData operationEventData;
    public List<GlobalEvent> getGlobalEvents(){
       return globalEventData.findAllEvent();
    }
    public List<OperationEvent> getOperationEvents(String email){
        return operationEventData.findOperationEventByEmail(email,email);
    }
    public GlobalEvent createAccount(String email){
        GlobalEvent globalEvent =new GlobalEvent(
                LocalDate.now().toString(),
                "CREATE_ACCOUNT",
                "Anonymous",
                email,
                request.getRequestURI()
        );
        globalEventData.saveGlobalEvent(globalEvent);
        return globalEvent;
    }
    public void createCard(String email){
        GlobalEvent globalEvent =new GlobalEvent(
                LocalDate.now().toString(),
                "CREATE_CARD",
                email,
                email,
                request.getRequestURI()
        );
        globalEventData.saveGlobalEvent(globalEvent);
    }
    public void changePassword(String email){
        GlobalEvent globalEvent =new GlobalEvent(
                LocalDate.now().toString(),
                "CHANGE_PASSWORD",
                email,
                email,
                request.getRequestURI()
        );
        globalEventData.saveGlobalEvent(globalEvent);
    }

    public void addMoney(CreditCard card,MoneyToCard amount){
        OperationEvent operationEvent =new OperationEvent(
                LocalDate.now().toString(),
                "ADD_MONEY",
                amount.getAmount(),
                card.getBalance(),
                0,
                card.getEmailOfOwner(),
                card.getEmailOfOwner(),
                request.getRequestURI()
        );
        operationEventData.saveOperationEvent(operationEvent);
    }

    public OperationEvent transaction(CreditCard subject, CreditCard object, MoneyToCard moneyToCard){
        OperationEvent operationEvent =new OperationEvent(
                LocalDate.now().toString(),
                "TRANSACTION",
                moneyToCard.getAmount(),
                subject.getBalance(),
                object.getBalance(),
                subject.getEmailOfOwner(),
                object.getEmailOfOwner(),
                request.getRequestURI()
        );
        operationEventData.saveOperationEvent(operationEvent);
        return operationEvent;
    }

    public void authenticationFailed(String email){
        GlobalEvent globalEvent =new GlobalEvent(
                LocalDate.now().toString(),
                "LOGIN_FAILED",
                email,
                email,
                request.getRequestURI()
        );
        globalEventData.saveGlobalEvent(globalEvent);
    }
    public void authorizationFailed(){
        UserDetails userDetails=getUserDetails();
        GlobalEvent globalEvent =new GlobalEvent(
                LocalDate.now().toString(),
                "ACCESS_DENIED",
                userDetails.getUsername(),
                userDetails.getUsername(),
                request.getRequestURI()
        );
        globalEventData.saveGlobalEvent(globalEvent);
    }
    public GlobalEvent lockAccount(Account account){
        UserDetails userDetails=getUserDetails();
        GlobalEvent globalEvent =new GlobalEvent(
                LocalDate.now().toString(),
                "LOCK_ACCOUNT",
                userDetails.getUsername(),
                String.format("Lock account: %s",account.getFullName()),
                request.getRequestURI()
        );
        globalEventData.saveGlobalEvent(globalEvent);
        return globalEvent;
    }
    public GlobalEvent unlockAccount(Account account){
        UserDetails userDetails=getUserDetails();
        GlobalEvent globalEvent =new GlobalEvent(
                LocalDate.now().toString(),
                "UNLOCK_ACCOUNT",
                userDetails.getUsername(),
                String.format("Unlock account: %s",account.getFullName()),
                request.getRequestURI()
        );
        globalEventData.saveGlobalEvent(globalEvent);
        return globalEvent;
    }
    public GlobalEvent lockCard(CreditCard creditCard){
        GlobalEvent globalEvent =new GlobalEvent(
                LocalDate.now().toString(),
                "LOCK_CARD",
                creditCard.getEmailOfOwner(),
                String.format("Lock card: %s",creditCard.getCard()),
                request.getRequestURI()
        );
        globalEventData.saveGlobalEvent(globalEvent);
        return globalEvent;
    }
    public GlobalEvent unlockCard(CreditCard creditCard){
        GlobalEvent globalEvent =new GlobalEvent(
                LocalDate.now().toString(),
                "UNLOCK_CARD",
                creditCard.getEmailOfOwner(),
                String.format("Unlock card: %s",creditCard.getCard()),
                request.getRequestURI()
        );
        globalEventData.saveGlobalEvent(globalEvent);
        return globalEvent;
    }
    public GlobalEvent newLimitOfTurnover(CreditCard creditCard){
        UserDetails userDetails=getUserDetails();
        GlobalEvent globalEvent =new GlobalEvent(
                LocalDate.now().toString(),
                "LIMIT_OF_TURNOVER_ENLARGED",
                userDetails.getUsername(),
                String.format("%s your limit of turnover: %s",creditCard.getEmailOfOwner(),creditCard.getLimitOfTurnover()),
                request.getRequestURI()
        );
        globalEventData.saveGlobalEvent(globalEvent);
        return globalEvent;
    }
    public void bruteForce(String email){
        GlobalEvent globalEvent =new GlobalEvent(
                LocalDate.now().toString(),
                "BRUTE_FORCE",
                email,
                request.getRequestURI(),
                request.getRequestURI()
        );
        globalEventData.saveGlobalEvent(globalEvent);
    }
    private UserDetails getUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
