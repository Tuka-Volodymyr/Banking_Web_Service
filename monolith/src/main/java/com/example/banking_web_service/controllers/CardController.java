package com.example.banking_web_service.controllers;

import com.example.banking_web_service.model.dto.ChangeTurnoverLimit;
import com.example.banking_web_service.model.dto.MoneyToCard;
import com.example.banking_web_service.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class CardController {
    @Autowired
    private CardService cardService;
    @GetMapping("/bank/add/card")
    public ResponseEntity<?> addCard(@AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(cardService.addCreditCard(userDetails.getUsername()), HttpStatus.CREATED);
    }
    @GetMapping("/bank/card/info")
    public ResponseEntity<?> getInfoAboutCard(@AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(cardService.getInfoAboutCard(userDetails), HttpStatus.OK);
    }
    @PostMapping("/bank/card/balance/add/money")
    public ResponseEntity<?> addMoney(@RequestBody MoneyToCard moneyToCard){
        return new ResponseEntity<>(cardService.addMoney(moneyToCard), HttpStatus.OK);
    }
    @PostMapping("/bank/card/balance/transfer/money")
    public ResponseEntity<?> transferMoney(@RequestBody MoneyToCard moneyToCard,@AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(cardService.transferMoney(moneyToCard,userDetails), HttpStatus.OK);
    }
    @GetMapping("/bank/card/lock")
    public ResponseEntity<?> lockCard(@AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(cardService.lockCard(userDetails.getUsername()), HttpStatus.LOCKED);
    }
    @GetMapping("/bank/card/unlock")
    public ResponseEntity<?> unlockCard(@AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(cardService.unlockCard(userDetails.getUsername()), HttpStatus.OK);
    }
    @PostMapping("/bank/card/change/limitOfTurnover")
    public ResponseEntity<?> changeMaxTurnover(@RequestBody ChangeTurnoverLimit changeTurnoverLimit) {
        return new ResponseEntity<>(cardService.newTurnoverLimit(changeTurnoverLimit), HttpStatus.OK);
    }
}
