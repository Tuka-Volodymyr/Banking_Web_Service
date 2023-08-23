package com.example.banking_web_service.controllers;

import com.example.banking_web_service.dto.ChangePassword;
import com.example.banking_web_service.entities.Account;
import com.example.banking_web_service.services.AccountService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
public class AccountController {
    @Autowired
    private AccountService accountService;
    @PostMapping("/bank/addAccount")
    public ResponseEntity<?> addAccount(@Valid @RequestBody Account account){
        return new ResponseEntity<>(accountService.addAccount(account), HttpStatus.CREATED);
    }
    @PostMapping("/bank/change/password")
    public ResponseEntity<?> changePasswordNew(@RequestParam(required=false) String code,@RequestBody ChangePassword changePassword){
        return new ResponseEntity<>(accountService.changePassword(changePassword,code),HttpStatus.OK);
    }


    @PostMapping("/bank/lock/account")
    public ResponseEntity<?> lockAccount(@RequestParam String email){
        return new ResponseEntity<>( accountService.lockAccount(email),HttpStatus.LOCKED);
    }

    @PostMapping("/bank/unlock/account")
    public ResponseEntity<?> unlockAccount(@RequestParam String email){
        return new ResponseEntity<>(accountService.unlockAccount(email), HttpStatus.OK);
    }
}
