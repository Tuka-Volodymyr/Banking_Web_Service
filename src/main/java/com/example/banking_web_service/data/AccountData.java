package com.example.banking_web_service.data;

import com.example.banking_web_service.entities.Account;
import com.example.banking_web_service.entities.CreditCard;
import com.example.banking_web_service.exceptions.AccountNotFoundException;
import com.example.banking_web_service.exceptions.CreditCardNotFoundException;
import com.example.banking_web_service.repositories.AccountInfoRepository;
import com.example.banking_web_service.repositories.CreditCardInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AccountData {
    @Autowired
    private  AccountInfoRepository accountInfoRepository;


    public Account findAccountByEmail(String email){
        return accountInfoRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(AccountNotFoundException::new);
    }
    public void saveAccount(Account account){
        accountInfoRepository.save(account);
    }
    public void existAccount(Account account){
        Optional<Account> someAccount =accountInfoRepository.findByEmailIgnoreCase(account.getEmail());
        if(someAccount.isPresent())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email has already used!");
    }


}
