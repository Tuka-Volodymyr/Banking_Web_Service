package com.example.banking_web_service.controllers;

import com.example.banking_web_service.entities.Account;
import com.example.banking_web_service.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;
    @PostMapping("/bank/addAccount")
    public ResponseEntity<?> addAccount(@Valid @RequestBody Account account){
        return accountService.addAccount(account);
    }
    @PostMapping("/bank/addCard")
    public ResponseEntity<?> addAccount(@AuthenticationPrincipal UserDetails userDetails){
        return accountService.addCreditCard(userDetails);
    }


}
