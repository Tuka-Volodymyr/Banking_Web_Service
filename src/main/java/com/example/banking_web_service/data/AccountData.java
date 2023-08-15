package com.example.banking_web_service.data;

import com.example.banking_web_service.entities.Account;
import com.example.banking_web_service.entities.CreditCard;
import com.example.banking_web_service.exceptions.AccountNotFoundException;
import com.example.banking_web_service.repositories.AccountInfoRepository;
import com.example.banking_web_service.repositories.CreditCardInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class AccountData {
    @Autowired
    private  AccountInfoRepository accountInfoRepository;
    @Autowired
    private CreditCardInfoRepository creditCardInfoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account findAccountByEmail(String email){
        return accountInfoRepository
                .findByEmail(email)
                .orElseThrow(AccountNotFoundException::new);
    }
    public void saveAccount(Account account){
        existAccount(account);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountInfoRepository.save(account);
    }
    public void existAccount(Account account){
        Optional<Account> someAccount =accountInfoRepository.findByEmail(account.getEmail());
        if(someAccount.isPresent())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email has already used!");
    }
    public void saveCreditCard(CreditCard creditCard){
        creditCardInfoRepository.save(creditCard);
    }

}
