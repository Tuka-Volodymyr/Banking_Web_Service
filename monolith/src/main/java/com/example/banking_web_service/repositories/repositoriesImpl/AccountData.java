package com.example.banking_web_service.repositories.repositoriesImpl;

import com.example.banking_web_service.model.entities.Account;
import com.example.banking_web_service.model.exceptions.AccountNotFoundException;
import com.example.banking_web_service.repositories.AccountInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

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
    public boolean accountTableEmpty(){
        List<Account> accounts=accountInfoRepository.findAll();
        return accounts.isEmpty();
    }


}
