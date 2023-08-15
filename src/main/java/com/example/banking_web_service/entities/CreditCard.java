package com.example.banking_web_service.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

@Entity
@Setter
@Getter
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private StringBuilder cardNumber;
    private LocalDate validTo;
    private StringBuilder cod;
    private String emailOfAccount;
    public CreditCard(){
        cardNumber=new StringBuilder();
        cod=new StringBuilder();
    }
    public void createCreditCard(){
        createCod();
        createCardNumber();
        createDateValidity();
    }
    public void createDateValidity(){
        validTo=LocalDate.now();
        validTo=validTo.plusYears(8);
    }
    public void createCardNumber(){
        Random rand = new Random();
        for (int i = 0; i < 15; i++) {
            cardNumber.append(rand.nextInt(10));
        }
        cardNumber.append(luhn());
    }
    public void createCod(){
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            cod.append(rand.nextInt(10));
        }
    }
    public int luhn(){
        int[] numberOfCard=new int[15];
        for(int i=0;i<15;i++){
            numberOfCard[i]=Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
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
