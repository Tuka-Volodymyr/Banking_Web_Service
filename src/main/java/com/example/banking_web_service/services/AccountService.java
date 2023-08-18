package com.example.banking_web_service.services;
import com.example.banking_web_service.data.AccountData;
import com.example.banking_web_service.dto.CreditCardDto;
import com.example.banking_web_service.entities.Account;
import com.example.banking_web_service.entities.CreditCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@Service
public class AccountService {
    @Autowired
    private AccountData accountData;
    @Autowired
    private EventService eventService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String addAccount(Account account){
        account.setLockStatus(false);
        account.setFailedAttempts(0);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountData.existAccount(account);
        accountData.saveAccount(account);
        eventService.createAccount(account.getEmail());
        return "Account was created by "+ account.getFullName();
    }
    public void resetAccountAttempts(String email){
        Account account=accountData.findAccountByEmail(email);
        account.setFailedAttempts(0);
        accountData.saveAccount(account);
    }

    public void increaseFailedAttempts(Account account){
        eventService.authenticationFailed(account.getEmail());
        account.setFailedAttempts(account.getFailedAttempts()+1);
        accountData.saveAccount(account);
    }
    public String lockAccount(String email) {
        Account account = accountData.findAccountByEmail(email);
        if (!account.isLockStatus()) {
            account.setLockStatus(true);
            account.setFailedAttempts(0);
            accountData.saveAccount(account);
            eventService.lock(email);
            return account.getFullName() + " your account is lock!";
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account don`t lock!");
    }
    public String unlockAccount(String email){
        Account account=accountData.findAccountByEmail(email);
        if(account.isLockStatus()){
            account.setLockStatus(false);
            account.setFailedAttempts(0);
            accountData.saveAccount(account);
            eventService.unlock(email);
            return account.getFullName()+" your account is unlock!";
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Account don`t lock!");
    }
}
