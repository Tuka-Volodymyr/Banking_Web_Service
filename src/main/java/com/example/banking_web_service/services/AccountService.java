package com.example.banking_web_service.services;
import com.example.banking_web_service.data.AccountData;
import com.example.banking_web_service.entities.Account;
import com.example.banking_web_service.entities.CreditCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountData accountData;
    public ResponseEntity<?> addAccount(Account account){
        accountData.saveAccount(account);
        return new ResponseEntity<>("Account was created by "+ account.getFullName(), HttpStatus.CREATED);
    }
    public ResponseEntity<?> addCreditCard(UserDetails userDetails){
        Account account=accountData.findAccountByEmail(userDetails.getUsername());
        CreditCard creditCard=new CreditCard();
        creditCard.setEmailOfAccount(account.getEmail());
        creditCard.createCreditCard();
        accountData.saveCreditCard(creditCard);
        return new ResponseEntity<>(creditCard,HttpStatus.CREATED);
    }

}
