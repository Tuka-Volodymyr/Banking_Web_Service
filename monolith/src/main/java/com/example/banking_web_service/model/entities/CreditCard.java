package com.example.banking_web_service.model.entities;

import com.example.banking_web_service.model.dto.CreditCardDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Random;

@Entity
@Data
@AllArgsConstructor
@Builder
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String card;
    private String valid;
    private String CVV;
    private float balance;
    private String owner;
    private String emailOfOwner;
    private boolean cardIsLocked;
    private float turnover;
    private float limitOfTurnover;
    @JsonIgnore
    public Long getId() {
        return id;
    }
    @JsonIgnore
    public String getEmailOfOwner() {
        return emailOfOwner;
    }

    public CreditCardDto dto(){
        return new CreditCardDto(card,String.format("%.2f",balance));
    }
    public CreditCard(){
        turnover=0;
        limitOfTurnover=400000;
        cardIsLocked=false;
        balance=0;
        createCreditCard();
    }
    public void createCreditCard(){
        CVV=createCVV();
        card=createCardNumber();
        valid=createDateValidity();

    }
    public String createDateValidity(){
        LocalDate localDate=LocalDate.now();
        localDate=localDate.plusYears(8);
        int month=localDate.getMonthValue();
        if(month<10)return "0"+month+"/"+localDate.getYear();
        return month+"/"+localDate.getYear();
    }
    public String createCardNumber(){
        StringBuilder stringBuilderCard=new StringBuilder("400000");
        Random rand = new Random();
        for (int i = 0; i < 9; i++) {
            stringBuilderCard.append(rand.nextInt(10));
        }
        stringBuilderCard.append(luhn(stringBuilderCard));
        return stringBuilderCard.toString();
    }
    public String createCVV(){
        StringBuilder stringBuilderCod=new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            stringBuilderCod.append(rand.nextInt(10));
        }
        return stringBuilderCod.toString();
    }
    public int luhn(StringBuilder stringBuilderCard){
        int[] numberOfCard=new int[15];
        for(int i=0;i<15;i++){
            numberOfCard[i]=Integer.parseInt(String.valueOf(stringBuilderCard.charAt(i)));
        }
        for(int i=0;i<15;i+=2){
            numberOfCard[i]*=2;
            if(numberOfCard[i]>9)numberOfCard[i]-=9;
        }
        int suma= Arrays.stream(numberOfCard).sum();
        int result=suma%10;
        if(result!=0)return 10-result;
        return result;
    }
}
