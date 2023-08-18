package com.example.banking_web_service.services;

import com.example.banking_web_service.data.GlobalEventData;
import com.example.banking_web_service.data.OperationEventData;
import com.example.banking_web_service.entities.GlobalEvent;
import com.example.banking_web_service.entities.OperationEvent;
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
    public List<OperationEvent> getOperationEvents(UserDetails userDetails){
        return operationEventData.findOperationEventByEmail(userDetails.getUsername(),userDetails.getUsername());
    }

    public void createAccount(String email){
        GlobalEvent globalEvent =new GlobalEvent(
                LocalDate.now().toString(),
                "CREATE_ACCOUNT",
                "Anonymous",
                email,
                request.getRequestURI()
        );
        globalEventData.saveGlobalEvent(globalEvent);
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

    public void addMoney(String email,float amount){
        OperationEvent operationEvent =new OperationEvent(
                LocalDate.now().toString(),
                "ADD_MONEY",
                amount,
                email,
                email,
                request.getRequestURI()
        );
        operationEventData.saveOperationEvent(operationEvent);
    }

    public void transaction(String subject,String object,float amount){
        OperationEvent operationEvent =new OperationEvent(
                LocalDate.now().toString(),
                "DO_TRANSACTION",
                amount,
                subject,
                object,
                request.getRequestURI()
        );
        operationEventData.saveOperationEvent(operationEvent);
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
    public void lock(String email){
        UserDetails userDetails=getUserDetails();
        GlobalEvent globalEvent =new GlobalEvent(
                LocalDate.now().toString(),
                "LOCK_USER",
                userDetails.getUsername(),
                String.format("Lock user: %s",email),
                request.getRequestURI()
        );
        globalEventData.saveGlobalEvent(globalEvent);
    }
    public void unlock(String email){
        UserDetails userDetails=getUserDetails();
        GlobalEvent globalEvent =new GlobalEvent(
                LocalDate.now().toString(),
                "UNLOCK_USER",
                userDetails.getUsername(),
                String.format("Unlock user: %s",email),
                request.getRequestURI()
        );
        globalEventData.saveGlobalEvent(globalEvent);
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
